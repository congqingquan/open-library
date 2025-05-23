package org.cqq.openlibrary.storage.local;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.cqq.openlibrary.common.constants.Constants;
import org.cqq.openlibrary.common.exception.server.ServerException;
import org.cqq.openlibrary.storage.Storage;
import org.cqq.openlibrary.storage.enums.StorageTypeEnum;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.UUID;

/**
 * 本地存储
 *
 * @author Qingquan
 */
@Slf4j
@AllArgsConstructor
public class LocalStorage implements Storage {
    
    private final String rootDir;
    
    private final String writeDir;
    
    private final String protocol;
    
    private final String host;
    
    private final Integer port;
    
    @Override
    public StorageTypeEnum supportType() {
        return StorageTypeEnum.LOCAL;
    }
    
    @Override
    public URL getURL(String objectName) {
        String writeDir = this.writeDir;
        writeDir = writeDir.startsWith(File.separator) ? writeDir.substring(File.separator.length()) : writeDir;
        writeDir = writeDir.endsWith(File.separator) ? writeDir.substring(0, writeDir.lastIndexOf(File.separator)) : writeDir;
        try {
            return new URL(
                    protocol,
                    host,
                    port,
                    "/" + writeDir + "/" + objectName
            );
        } catch (MalformedURLException e) {
            throw new ServerException(String.format("Getting URL failed, object name [%s]", objectName), e);
        }
    }
    
    @Override
    public SaveResult save(String filename, InputStream fileStream) {
        String filenameExtension = StringUtils.getFilenameExtension(filename);
        String newFilename = UUID.randomUUID().toString().replace("-", "") + Constants.DOT + filenameExtension;
        
        // 1. 构建存储路径
        Path storagePath = Path.of(rootDir, writeDir, newFilename);
        if (Files.exists(storagePath)) {
            throw new ServerException("File exist");
        }
        // 2. 检查创建父目录
        Path pathParent = storagePath.getParent();
        if (!Files.exists(pathParent)) {
            boolean mkdirs = pathParent.toFile().mkdirs();
            if (!mkdirs) {
                throw new ServerException("Create file dic failed");
            }
        }
        // 3. 写入
        try {
            Files.write(storagePath, fileStream.readAllBytes(), StandardOpenOption.CREATE);
        } catch (IOException e) {
            log.error("Uploading failed", e);
            throw new ServerException("Uploading failed");
        }
        // 4. 组装返回结果
        return SaveResult.builder()
                .rootDir(rootDir)
                .writeDir(writeDir)
                .originFilename(filename)
                .filename(newFilename)
                .filenameExtension(filenameExtension)
                .url(getURL(newFilename).toString())
                .build();
    }
    
    @Override
    public Boolean remove(Object objectName) {
        return Path.of(rootDir, writeDir, objectName.toString()).toFile().delete();
    }
}
