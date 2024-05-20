package com.ischool.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.Formatter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/*
 * 格式化参数日期格式
 * */
// @Configuration
public class WebConfig implements WebMvcConfigurer {

    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addFormatter(new LocalDateFormatter());
        registry.addFormatter(new LocalDateTimeFormatter());
    }

    static class LocalDateFormatter implements Formatter<LocalDate> {
        @Override
        public LocalDate parse(String text, Locale locale) {
            return LocalDate.parse(text, DateTimeFormatter.ofPattern(DATE_FORMAT));
        }

        @Override
        public String print(LocalDate object, Locale locale) {
            return DateTimeFormatter.ofPattern(DATE_FORMAT).format(object);
        }
    }

    static class LocalDateTimeFormatter implements Formatter<LocalDateTime> {
        @Override
        public LocalDateTime parse(String text, Locale locale) {
            return LocalDateTime.parse(text, DateTimeFormatter.ofPattern(DATE_TIME_FORMAT));
        }

        @Override
        public String print(LocalDateTime object, Locale locale) {
            return DateTimeFormatter.ofPattern(DATE_TIME_FORMAT).format(object);
        }
    }
}
