package org.cqq.openlibrary.common.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.Base64;

/**
 * Base64 utils
 *
 * @author Qingquan
 */
public class Base64Utils {
    
    private Base64Utils() {}
    
    public static final Base64.Encoder ENCODER = Base64.getEncoder();
    
    public static final Base64.Decoder DECODER = Base64.getDecoder();
    
    // ********************** encode **********************
    
    public static byte[] encode(URL url) throws IOException {
        URLConnection urlConnection = url.openConnection();
        urlConnection.connect();
        return encode(urlConnection.getInputStream());
    }
    
    public static byte[] encode(InputStream inputStream) {
        try (inputStream; ByteArrayOutputStream outStream = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, len);
            }
            return encode(outStream.toByteArray());
        } catch (IOException exception) {
            throw new RuntimeException("Input stream base64 encode error", exception);
        }
    }
    
    public static byte[] encode(byte[] data) {
        return ENCODER.encode(data);
    }
    
    public static String encodeToString(String data, Charset charset) {
        return new String(ENCODER.encode(data.getBytes(charset)), charset);
    }
    
    // ********************** decode **********************
    
    public static byte[] decode(byte[] data) {
        return DECODER.decode(data);
    }
    
    public static String decode(String dataBase64, Charset charset) {
        return new String(DECODER.decode(dataBase64.getBytes(charset)), charset);
    }
}