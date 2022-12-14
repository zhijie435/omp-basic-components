package com.gb.soa.basic.components;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.beans.Introspector;

@ImportResource(locations = {"classpath:application_dubbo.xml", "classpath:spring-quartz.xml"})
@Configuration
@EnableDiscoveryClient
@EnableAutoConfiguration
@ComponentScan(basePackages = {"com.gb.soa.omp.ccache", "com.gb.soa.sequence", "com.gb.soa.omp.export", "com.gb.soa.basic.config","com.ykcloud.soa.mapping"})
public class BasicComponentsMain {
    public static void main(String[] args) {
        new SpringApplicationBuilder(BasicComponentsMain.class).beanNameGenerator(new CustomGenerator()).run(args);
        System.out.println("spring 启动好了");
    }

    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        jsonConverter.setObjectMapper(objectMapper);
        return jsonConverter;
    }

    public static class CustomGenerator extends AnnotationBeanNameGenerator {

        @Override
        protected String buildDefaultBeanName(BeanDefinition definition) {
            String beanClassName = definition.getBeanClassName();
            Assert.state(beanClassName != null, "No bean class name set");
            String beanName = ClassUtils.getPackageName(beanClassName) + "." + Introspector.decapitalize(ClassUtils.getShortName(beanClassName));
            return beanName;
        }
    }
}
