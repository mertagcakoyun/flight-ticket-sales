package com.iyzico.challenge.configuration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class IyzicoConfigTest {

    @Autowired
    private IyzicoConfig iyzicoConfig;

    @Test
    public void contextLoads() {
        assertNotNull(iyzicoConfig);
    }
}