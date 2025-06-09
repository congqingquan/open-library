package org.cqq.openlibrary.storage;

import lombok.Builder;
import lombok.Data;
import org.cqq.openlibrary.common.util.CollectionUtils;
import org.cqq.openlibrary.common.util.EnumUtils;
import org.cqq.openlibrary.storage.enums.StorageTypeEnum;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 存储接口
 *
 * @author Qingquan
 */
public interface Storage {
    
    StorageTypeEnum supportType();
    
    String getDomain();
    
    URL getURL(String objectName);
    
    SaveResult save(String filename, InputStream fileStream);
    
    Boolean remove(Object objectName);
    
    class Manager {
        
        private final Map<StorageTypeEnum, Storage> serviceMap = new HashMap<>();
        
        public Manager(List<Storage> storages) {
            if (CollectionUtils.isEmpty(storages)) {
                return;
            }
            for (Storage storage : storages) {
                serviceMap.put(storage.supportType(), storage);
            }
        }
        
        public Storage get(StorageTypeEnum storageTypeEnum) {
            return serviceMap.get(storageTypeEnum);
        }
        
        public Storage get(String storageType) {
            return EnumUtils.equalMatch(
                            StorageTypeEnum.values(),
                            StorageTypeEnum::name,
                            storageType
                    )
                    .map(serviceMap::get)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid storage type"));
        }
    }
    
    @Data
    @Builder(toBuilder = true)
    class SaveResult {
        
        private String rootDir;
        
        private String writeDir;
        
        private String originFilename;
        
        private String filename;
        
        private String filenameExtension;
        
        private String url;
    }
}
