package org.cqq.openlibrary.common.util;

import org.cqq.openlibrary.common.exception.server.IORuntimeException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * IO Utils
 *
 * @author Qingquan
 */
public class IOUtils {
    
    private IOUtils() {}
    
    public static String readString(InputStream inputStream, Charset charset) {
        StringBuilder builder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, charset))) {
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append(System.lineSeparator());
            }
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
        return builder.toString();
    }
}
