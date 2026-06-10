package com.springboot.swmlopsbe.global.config.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "cloud.aws.s3")
public class S3Properties {

    private String bucket;
    private String region;
    private String accessKey;
    private String secretKey;
}
