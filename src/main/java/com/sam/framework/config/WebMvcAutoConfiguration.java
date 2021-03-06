package com.sam.framework.config;

import com.baomidou.dynamic.datasource.processor.DsHeaderProcessor;
import com.baomidou.dynamic.datasource.processor.DsProcessor;
import com.baomidou.dynamic.datasource.processor.DsSessionProcessor;
import com.baomidou.dynamic.datasource.processor.DsSpelExpressionProcessor;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.sam.common.spring.ApiHandlerExceptionResolver;
import com.sam.common.spring.IEnumConverterFactory;
import com.sam.common.spring.validator.ValidatorCollectionImpl;
import com.sam.common.utils.JacksonUtils;
import com.sam.framework.aspect.LogRecordAspect;
import com.sam.framework.processor.DsAttributeProcessor;
import com.sam.framework.processor.DsAuthorizationProcessor;
import com.sam.framework.processor.DsParameterProcessor;
import com.sam.framework.provider.JdbcDataSourceProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * <p>
 * Spring Boot 配置
 * </p>
 *
 * @author Caratacus
 */
@Configuration
public class WebMvcAutoConfiguration implements WebMvcConfigurer {

    /**
     * 允许跨域的origin
     */
    private static final String[] origins = {
            "http://localhost",
            "http://localhost:8080",
            "http://localhost:8081",
            "http://172.18.88.1",
            "https://172.18.88.1",
            "http://172.18.88.1:8080",
            "https://172.18.88.1:8080",
    };

    @Bean
    public LogRecordAspect logRecordAspect() {
        return new LogRecordAspect();
    }

    @Override
    public Validator getValidator() {
        return new SpringValidatorAdapter(new ValidatorCollectionImpl());
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverterFactory(new IEnumConverterFactory());
    }

    @Bean
    @ConditionalOnMissingBean(RequestContextListener.class)
    public RequestContextListener requestContextListener() {
        return new RequestContextListener();
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.forEach(JacksonUtils.wrapperObjectMapper());
    }

    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
        exceptionResolvers.add(new ApiHandlerExceptionResolver());
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        //将需要跨域访问的域名或IP添加到origins中
        registry.addMapping("/**")
                .allowedOrigins(origins)
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    @ConditionalOnMissingBean
    public DsProcessor dsProcessor() {
        //可做到读写分离 分库分表
        DsAuthorizationProcessor authorizationProcessor = new DsAuthorizationProcessor();
        DsParameterProcessor parameterProcessor = new DsParameterProcessor();
        DsAttributeProcessor attributeProcessor = new DsAttributeProcessor();
        DsHeaderProcessor headerProcessor = new DsHeaderProcessor();
        DsSessionProcessor sessionProcessor = new DsSessionProcessor();
        DsSpelExpressionProcessor spelExpressionProcessor = new DsSpelExpressionProcessor();
        authorizationProcessor.setNextProcessor(parameterProcessor);
        parameterProcessor.setNextProcessor(attributeProcessor);
        attributeProcessor.setNextProcessor(headerProcessor);
        headerProcessor.setNextProcessor(sessionProcessor);
        sessionProcessor.setNextProcessor(spelExpressionProcessor);
        return authorizationProcessor;
    }

    /**
     * 当LocalDate,LocalTime,LocalDateTime作为RequestParam或者PathVariable时
     * 直接进行格式化操作
     */

    @Bean
    public Converter<String, LocalDate> localDateConverter() {
        return new Converter<String, LocalDate>() {
            @Override
            public LocalDate convert(String source) {
                if (StringUtils.isNotEmpty(source)) {
                    return LocalDate.parse(source, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                }
                return null;
            }
        };
    }

    @Bean
    public Converter<String, LocalTime> localTimeConverter() {
        return new Converter<String, LocalTime>() {
            @Override
            public LocalTime convert(String source) {
                if (StringUtils.isNotEmpty(source)) {
                    return LocalTime.parse(source, DateTimeFormatter.ofPattern("HH:mm:ss"));
                }
                return null;
            }
        };
    }

    @Bean
    public Converter<String, LocalDateTime> localDateTimeConverter() {
        return new Converter<String, LocalDateTime>() {
            @Override
            public LocalDateTime convert(String source) {
                if (StringUtils.isNotEmpty(source)) {
                    return LocalDateTime.parse(source, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                }
                return null;
            }
        };
    }

    @Value("${datasource.driver}")
    private String DRIVER;
    @Value("${datasource.url}")
    private String URL;
    @Value("${datasource.username}")
    private String USERNAME;
    @Value("${datasource.password}")
    private String PASSWORD;

    @Bean
    public JdbcDataSourceProvider dynamicDataSourceProvider() {
        return new JdbcDataSourceProvider(DRIVER, URL, USERNAME, PASSWORD);
    }

    @Bean
    public Executor myAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);//核心线程数
        executor.setMaxPoolSize(20);//最大线程数
        executor.setKeepAliveSeconds(5);//最大线程数 空闲时间5秒后销毁
        executor.setQueueCapacity(100);//配置队列大小
        executor.setThreadNamePrefix("MyAsync-");//线程名前缀
        /**
         * AbortPolicy：处理程序遭到拒绝将抛出运行时 RejectedExecutionException
         * CallerRunsPolicy:主线程直接执行该任务，执行完之后尝试添加下一个任务到线程池中，可以有效降低向线程池内添加任务的速度
         * DiscardOldestPolicy:抛弃旧的任务、暂不支持；会导致被丢弃的任务无法再次被执行
         * DiscardPolicy:抛弃当前任务、暂不支持；会导致被丢弃的任务无法再次被执行
         */
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());//拒绝策略
        executor.initialize();
        return executor;
    }

}
