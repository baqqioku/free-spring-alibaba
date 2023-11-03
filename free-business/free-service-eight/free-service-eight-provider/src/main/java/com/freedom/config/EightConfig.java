package com.freedom.config;


import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "eight")
@Setter
@Getter
@Data
@ToString
public class EightConfig {

    private String name;
    private Integer age;
}
