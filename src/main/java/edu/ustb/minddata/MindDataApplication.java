package edu.ustb.minddata;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @author UmiSkky
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class MindDataApplication {

    public static void main(String[] args) {
        SpringApplication.run(MindDataApplication.class, args);
    }

}
