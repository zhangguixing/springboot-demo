package com.myth.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

/**
 * @author zhangguixing Email:guixingzhang@qq.com
 */
@Data
public class B {

    @JsonBackReference
    private A a;
}
