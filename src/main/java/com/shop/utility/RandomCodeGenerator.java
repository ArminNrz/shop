package com.shop.utility;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
@Slf4j
public class RandomCodeGenerator {

    public String generate() {
        Random random = new Random();
        String code = String.format("%04d", random.nextInt(10000));
        log.info("Code: {}", code);
        return code;
    }
}
