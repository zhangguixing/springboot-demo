package com.myth.user.job;

import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.stereotype.Component;

/**
 * xxl-job案例
 *
 * @author zhangguixing Email:guixingzhang@qq.com
 */
@Component
public class DemoJobHandler {

    @XxlJob("demoJobHandler")
    public void demo() {
        System.out.println("demo xxl job");
    }
}
