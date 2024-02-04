package com.myth.common.filter;

import org.springframework.util.StreamUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;


/**
 * <http request封装，获取请求报文>
 *
 * @author zhangguixing Email:guixingzhang@qq.com
 */
public class CustomHttpServletRequestWrapper extends HttpServletRequestWrapper {

    /**
     * 保存 BODY 内容
     **/
    private final byte[] body;
    /**
     * 缓存 BODY 的输入流，供接口读取 BODY
     **/
    private final CustomServletInputStream inputStream;

    public CustomHttpServletRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);

        body = StreamUtils.copyToByteArray(request.getInputStream());
        inputStream = new CustomServletInputStream(body);
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        if (inputStream != null) {
            return inputStream;
        }
        return super.getInputStream();
    }

    public String getBody() {
        return new String(body, StandardCharsets.UTF_8);
    }

    private static class CustomServletInputStream extends ServletInputStream {

        private final ByteArrayInputStream inputStream;

        public CustomServletInputStream(byte[] body) {
            this.inputStream = new ByteArrayInputStream(body);
        }

        @Override
        public boolean isFinished() {
            return inputStream.available() == 0;
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setReadListener(ReadListener readListener) {

        }

        @Override
        public int read() {
            return inputStream.read();
        }
    }
}