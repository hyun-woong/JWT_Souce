package com.sparta.jwt_souce.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.Rfc6265CookieProcessor;
import org.apache.tomcat.util.http.SameSiteCookies;
import org.springframework.boot.web.embedded.tomcat.TomcatContextCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final ObjectMapper objectMapper;

    //CORS issue 관련 코드
    //Preflight란?
    //사전적 의미로는, 미리 보내는 것, 사전 전달

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") //CORS를 적용할 URL패턴을 정의
                .allowedOrigins("http://localhost:3000", "rewind-f8918.firebaseapp.com", "http://wooseokhan.shop") //자원공유 허용 부분
                // 다 허용하고 싶으면, .addMapping("/**") .allowedOrigins("*")
                .allowedMethods("POST", "GET", "PUT", "DELETE", "HEAD", "OPTIONS") // CRUD 허용 부분
                .allowedHeaders("*") //어떤 헤더들을 허용할 것인지
                .maxAge(3000) // preflight 요청에 대한 응답을 브라우저에서 캐싱하는 시간(3000초 동안 저장)
                //서버 설정을 통해 preflight 결과의 캐시를 일정 기간 동안 저장시킬 수 있다. 캐시 정보가 살아있는 동안 cross-origin 요청에 대해 preflight 요청 생략 가능
                .allowCredentials(true); //쿠키 요청을 허용한다(다른 도메인 서버에 인증하는 경우에만 사용해야하며, true 설정시 보안상 이슈가 발생할 수 있다.)
                                        //보통은 false 값을 주는 듯
    }

    @Bean
    public TomcatContextCustomizer sameSiteCookesConfig() {
        return context -> {
            final Rfc6265CookieProcessor cookieProcessor = new Rfc6265CookieProcessor();
            cookieProcessor.setSameSiteCookies(SameSiteCookies.NONE.getValue());
            context.setCookieProcessor(cookieProcessor);
        };
    }

    @Bean
    //특수문자를 처리하는 작업, Java Object -> JSON으로 변환
    //이게 맞나...
    public MappingJackson2HttpMessageConverter jsonEscapeConverter() {
        ObjectMapper copy = objectMapper.copy();
        copy.getFactory().setCharacterEscapes(new HtmlCharacterEscapes());
        return new MappingJackson2HttpMessageConverter(copy);
    }
}
