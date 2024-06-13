package com.iyzico.challenge.configuration;

import com.iyzico.challenge.configuration.properties.IyzicoProperties;
import com.iyzipay.Options;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class IyzicoConfig {


    private final IyzicoProperties iyzicoProperties;

    @Autowired
    public IyzicoConfig(IyzicoProperties iyzicoProperties) {
        this.iyzicoProperties = iyzicoProperties;
    }


    @Bean
    public Options connectionOptions() {
        Options options = new Options();
        options.setBaseUrl(iyzicoProperties.getUrl());
        options.setApiKey(iyzicoProperties.getApiKey());
        options.setSecretKey(iyzicoProperties.getSecretKey());
        return options;
    }
}
