package com.sam.framework.config;

import com.github.xiaoymin.swaggerbootstrapui.annotations.EnableSwaggerBootstrapUI;
import com.sam.common.utils.JWTUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

/**
 * <p>
 * RESTful 服务 API 管理框架 Swagger 配置初始化
 * </p>
 *
 * @author Caratacus
 */
@Configuration
@EnableSwagger2
@EnableSwaggerBootstrapUI
@Profile("dev")
public class SwaggerAutoConfiguration {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .globalOperationParameters(getParameters())
                .host("127.0.0.1")
                .directModelSubstitute(LocalDateTime.class, String.class)
                .directModelSubstitute(LocalDate.class, String.class)
                .directModelSubstitute(LocalTime.class, String.class)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .paths(PathSelectors.any()).build().useDefaultResponseMessages(false);
    }

    /**
     * 获取swagger ApiInfo
     *
     * @return
     */
    ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("RestBoot API")
                .description("Apartment Swagger API 文档")
                .termsOfServiceUrl("http://127.0.0.1/rest-apartment")
                .version("1.0")
                .contact(new Contact("RestBoot", "线上地址暂无", "932396823@qq.com"))
                .build();
    }

    /**
     * 获取Swagger参数
     *
     * @return
     */
    List<Parameter> getParameters() {
        return Collections.singletonList(new ParameterBuilder()
                .name("Authorization")
                .defaultValue("Bearer " + JWTUtils.generate(1))
                .description("Authorization")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .required(false)
                .build());
    }
}
