package com.iyzico.challenge.configuration.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "http.iyzico")
public class IyzicoProperties {

    private String url;
    private String apiKey;
    private String secretKey;

}
