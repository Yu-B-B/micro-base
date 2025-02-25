package com.demo.intercepter;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;

@Component
public class FeignRequestInterceptor implements RequestInterceptor {

    /**
     * @param requestTemplate 请求的详细信息
     */
    @Override
    public void apply(RequestTemplate requestTemplate) {
        requestTemplate.header("TOKEN","asidjhfasdf");
    }
}
