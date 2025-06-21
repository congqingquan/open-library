package org.cqq.openlibrary.web.wrapper;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * HttpServletRequestWrapper extension
 *
 * @author Qingquan
 */
public class HttpServletRequestWrapperExt extends HttpServletRequestWrapper {
    
    private final byte[] body;
    
    public HttpServletRequestWrapperExt(HttpServletRequest request) throws IOException {
        super(request);
        body = request.getInputStream().readAllBytes();
    }
    
    @Override
    public ServletInputStream getInputStream() {
        return new ByteArrayServletInputStream(body);
    }
    
    @Override
    public BufferedReader getReader() {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }
    
    private static class ByteArrayServletInputStream extends ServletInputStream {
        
        private final ByteArrayInputStream buffer;
        
        public ByteArrayServletInputStream(byte[] contents) {
            this.buffer = new ByteArrayInputStream(contents);
        }
        
        @Override
        public int read() {
            return buffer.read();
        }
        
        @Override
        public boolean isFinished() {
            return buffer.available() == 0;
        }
        
        @Override
        public boolean isReady() {
            return true;
        }
        
        @Override
        public void setReadListener(ReadListener listener) {
        }
    }
}