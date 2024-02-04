package com.myth.model;

import lombok.Data;

import java.util.List;

/**
 * @author zhangguixing Email:guixingzhang@qq.com
 */
@Data
public class A<T> {

//    @JsonManagedReference
//    private B b;

    private String content;

    private List<T> list;
}
