package edu.ustb.minddata.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author UmiSkky
 */
@Data
@Component
public class PathConfig {

    @Value("${datasource.path.prefix}")
    private String tmpPrefixPath;

    @Value("${datasource.path.suffix}")
    private String tmpSuffixPath;

    private static String prefixPath;

    private static String suffixPath;

    @PostConstruct
    public void setPathConfig(){
        prefixPath = this.tmpPrefixPath;
        suffixPath = this.tmpSuffixPath;
    }

    public static String getPrefixPath(){
        return prefixPath;
    }

    public static String getSuffixPath(){
        return suffixPath;
    }
}
