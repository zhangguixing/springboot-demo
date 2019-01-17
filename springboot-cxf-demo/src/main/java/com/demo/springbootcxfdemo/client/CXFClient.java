package com.demo.springbootcxfdemo.client;

import com.demo.springbootcxfdemo.service.UserService;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;

/**
 * @Author Guixing
 * @Date 2019/1/17 14:53
 * @Description
 */
public class CXFClient {

    public static void main(String[] args) {
        try {
            // 接口地址
            String address = "http://127.0.0.1:8080/soap/user?wsdl";
            // 代理工厂
            JaxWsProxyFactoryBean jaxWsProxyFactoryBean = new JaxWsProxyFactoryBean();
            // 设置代理地址
            jaxWsProxyFactoryBean.setAddress(address);
            // 设置接口类型
            jaxWsProxyFactoryBean.setServiceClass(UserService.class);
            // 创建一个代理接口实现
            UserService us = (UserService) jaxWsProxyFactoryBean.create();
            // 数据准备
            String name = "zhang";
            // 调用代理接口的方法调用并返回结果
            String result = us.getHello(name);
            System.out.println("返回结果:" + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
