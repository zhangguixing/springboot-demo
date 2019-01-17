package com.demo.springbootcxfdemo.service;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

//指定webservice命名空间
@WebService(targetNamespace = "http://service.springbootcxfdemo.demo.com")
public interface UserService {

    @WebMethod(operationName = "getHello")//在wsdl文档中显示的方法名，可不指定默认与方法相同；@WebMethod可不指定
    String getHello(@WebParam(name = "name") String name);

}
