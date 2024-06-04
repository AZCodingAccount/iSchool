package com.ischool.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import com.ischool.model.BaseResponse;
import com.ischool.model.ErrorCode;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*
 * openapi3全局配置
 * */
@Configuration
@EnableKnife4j
public class Knife4jConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        OpenAPI openAPI = new OpenAPI()
                // .components(new Components()) // 确保 Components 已初始化
                .info(new Info()
                        .title("iSchool项目API 文档")
                        .version("1.0")
                        .description("API 文档")
                        .contact(new Contact()
                                .name("Ljx")
                                .email("han892577@qq.com")
                                .url("https://www.bugdesigner.cn")));

        // addGlobalResponses(openAPI);
        return openAPI;
    }

    // private void addGlobalResponses(OpenAPI openAPI) {
    //     openAPI.getComponents().addResponses("400", createApiResponse(ErrorCode.PARAMS_ERROR));
    //     openAPI.getComponents().addResponses("401", createApiResponse(ErrorCode.NOT_LOGIN_ERROR));
    //     openAPI.getComponents().addResponses("403", createApiResponse(ErrorCode.NO_AUTH_ERROR));
    //     openAPI.getComponents().addResponses("404", createApiResponse(ErrorCode.NOT_FOUND_ERROR));
    //     openAPI.getComponents().addResponses("500", createApiResponse(ErrorCode.SYSTEM_ERROR));
    //     openAPI.getComponents().addResponses("50001", createApiResponse(ErrorCode.OPERATION_ERROR));
    //     openAPI.getComponents().addResponses("50010", createApiResponse(ErrorCode.API_REQUEST_ERROR));
    // }
    //
    // private ApiResponse createApiResponse(ErrorCode errorCode) {
    //     // 创建 Schema
    //     Schema<?> schema = new Schema<BaseResponse<String>>().$ref("#/components/schemas/BaseResponse");
    //
    //     // 创建 MediaType 并设置 Schema
    //     MediaType mediaType = new MediaType().schema(schema);
    //
    //     // 创建 Content 并添加 MediaType
    //     Content content = new Content().addMediaType("application/json", mediaType);
    //
    //     // 创建 ApiResponse 并设置描述和内容
    //     return new ApiResponse()
    //             .description(errorCode.getMessage())
    //             .content(content);
    // }
}
