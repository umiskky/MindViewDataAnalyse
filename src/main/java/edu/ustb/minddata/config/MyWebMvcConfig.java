package edu.ustb.minddata.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

/**
 * @author UmiSkky
 */
@Slf4j
@Configuration
public class MyWebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("file:///" + Paths.get(PathConfig.getPrefixPath()).toAbsolutePath() + "/");
        registry.addResourceHandler("/**").addResourceLocations("classpath:/META-INF/static/");
        registry.addResourceHandler("/favicon.ico").addResourceLocations("classpath:/META-INF/static/");
    }

}
