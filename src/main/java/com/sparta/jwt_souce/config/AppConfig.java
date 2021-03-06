package com.sparta.jwt_souce.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@Configuration
public class AppConfig {

    @Bean
    @Primary //같은 타입의 빈이 여러 개 있을 때, @Qualifier을 지정해 주지 않을 경우를 대비, 해당 빈을 우선적으로 선택하라는 뜻
    public ObjectMapper objectMapper() {
        return new Jackson2ObjectMapperBuilder()
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .modules(new JavaTimeModule())
                .timeZone("Asia/Seoul")
                .build();
    }
}
