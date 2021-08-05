package edu.ustb.minddata.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 启动后自动打开网页
 * @author UmiSkky
 */
@Component
@Slf4j
public class CommandRunnerConfig implements CommandLineRunner {

    @Value("${start.url}")
    private String startUrl;

    @Value("${start.enabled}")
    private boolean isEnabled;

    @Override
    public void run(String... args) {
        if (isEnabled) {
            log.info("自动加载指定的页面");
            try {
                Runtime.getRuntime().exec("cmd /c start " + startUrl);
            } catch (Exception ex) {
                ex.printStackTrace();
                log.error("浏览器打开页面异常");
            }
        }
    }

}
