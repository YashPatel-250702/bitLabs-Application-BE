package com.talentstream.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.nio.charset.StandardCharsets;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@ControllerAdvice
public class ResponseSizeLogger implements ResponseBodyAdvice<Object> {

    private static final Logger logger = LoggerFactory.getLogger(ResponseSizeLogger.class);

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  org.springframework.http.server.ServerHttpRequest request,
                                  ServerHttpResponse response) {
        try {
            if (body != null && response instanceof ServletServerHttpResponse) {
                String data = objectMapper.writeValueAsString(body);
                int sizeInBytes = data.getBytes(StandardCharsets.UTF_8).length;
                double sizeInKB = sizeInBytes / 1024.0;

                String path = request.getURI().getPath();
                logger.info("API [{}] response size: {} bytes (~{} KB)", path, sizeInBytes, String.format("%.2f", sizeInKB));
            }
        } catch (Exception e) {
            logger.warn("Failed to log response size", e);
        }

        return body;
    }

}
