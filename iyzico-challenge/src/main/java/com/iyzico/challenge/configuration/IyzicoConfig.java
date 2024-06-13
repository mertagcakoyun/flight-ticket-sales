package com.iyzico.challenge.configuration;

import com.iyzipay.Options;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IyzicoConfig {

    @Value("${http.iyzico.url}")
    private String baseUrl;

    @Value("${http.iyzico.api-key}")
    private String apiKey;

    @Value("${http.iyzico.secret-key}")
    private String secretKey;

    @Bean
    public Options connectionOptions() {
        Options options = new Options();
        options.setBaseUrl(baseUrl);
        options.setApiKey(apiKey);
        options.setSecretKey(secretKey);
        return options;
    }
}
