package com.wzy.loan.base.config;

import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.BuilderDefaults;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author：wzy
 * @date：2022/2/8-02-08-14:37
 */
@Configuration
//localDateTime类型数据的返回数据格式配置， Date类型的格式需要另外配置
public class localDateTimeConfig {
    @Value("${spring.jackson.date-format:yyyy-MM-dd HH:mm:ss}")
    private String pattern;

    public LocalDateTimeSerializer localDateTimeSerializer(){
        return new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(pattern));
    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer(){
        return builder -> builder.serializerByType(LocalDateTime.class,localDateTimeSerializer());
    }

}
