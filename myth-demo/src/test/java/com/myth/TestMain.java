package com.myth;

import org.junit.jupiter.api.Test;

import java.text.MessageFormat;

/**
 * @author zhangguixing Email:guixingzhang@qq.com
 */
public class TestMain {

    @Test
    public void test1(){
        System.out.println(MessageFormat.format("hello {1} , {2}, ddd{0}", "1", "2", "3"));
    }
}
