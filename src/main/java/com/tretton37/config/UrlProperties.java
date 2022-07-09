package com.tretton37.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties("params.url")
public class UrlProperties {
    private String url = "https://1337.tech/";
}