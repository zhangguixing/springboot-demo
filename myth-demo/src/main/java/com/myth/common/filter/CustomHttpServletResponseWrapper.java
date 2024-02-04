package com.myth.common.filter;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * <http response封装，获取响应报文>
 *
 * @author zhangguixing Email:guixingzhang@qq.com
 */
public class CustomHttpServletResponseWrapper extends HttpServletResponseWrapper {
    private final CustomServletOutputStream servletOutputStream;

    public CustomHttpServletResponseWrapper(HttpServletResponse response) throws IOException {
        super(response);
        servletOutputStream = new CustomServletOutputStream(response.getOutputStream());
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        if (servletOutputStream != null) {
            return servletOutputStream;
        }
        return super.getOutputStream();
    }

    public String getBody() {
        return servletOutputStream.getBody();
    }

    //内部类，对ServletOutputStream进行包装，指定输出流的输出端
    private static class CustomServletOutputStream extends ServletOutputStream {
        private final ByteArrayOutputStream outputStream;
        private final OutputStream responseOutputStream;

        public CustomServletOutputStream(OutputStream outputStream) {
            super();
            this.outputStream = new ByteArrayOutputStream();
            this.responseOutputStream = outputStream;
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setWriteListener(WriteListener writeListener) {

        }

        @Override
        public void write(int b) throws IOException {
            responseOutputStream.write(b);
            outputStream.write(b);
        }

        public String getBody() {
            return outputStream.toString();
        }

        @Override
        public void flush() throws IOException {
            responseOutputStream.flush();
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            outputStream.write(b, off, len);
            responseOutputStream.write(b, off, len);
        }

        @Override
        public void close() throws IOException {
            outputStream.close();
            responseOutputStream.close();
            super.close();
        }
    }
}

