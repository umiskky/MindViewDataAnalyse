package edu.ustb.minddata.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

/**
 * @author UmiSkky
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket docketPersonnel(Environment environment) {

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .groupName("Personnel")
                .select()
                .apis(RequestHandlerSelectors.basePackage("edu.ustb.minddata.controller"))
                .paths(PathSelectors.ant("/api/personnel/**"))
                .build();
    }

    @Bean
    public Docket docketRecord() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .groupName("Record")
                .select()
                .apis(RequestHandlerSelectors.basePackage("edu.ustb.minddata.controller"))
                .paths(PathSelectors.ant("/api/record/**"))
                .build();
    }

    @Bean
    public Docket docketReport() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .groupName("Report")
                .select()
                .apis(RequestHandlerSelectors.basePackage("edu.ustb.minddata.controller"))
                .paths(PathSelectors.ant("/api/report/**"))
                .build();
    }

    private ApiInfo apiInfo() {
        Contact contact = new Contact("umiskky", "https://www.umiskky.xyz", "umiskky@outlook.com");
        return new ApiInfo(
                "umiskky-API",
                "API",
                "v1.0",
                "",
                contact,
                "",
                "",
                new ArrayList<>()
        );
    }
}
