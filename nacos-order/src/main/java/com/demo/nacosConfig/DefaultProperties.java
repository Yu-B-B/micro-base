package com.demo.nacosConfig;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("server")
@Data
public class DefaultProperties {
    private String port;
    private String name;
}
