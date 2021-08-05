package edu.ustb.minddata.config.datasource;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.zaxxer.hikari.HikariDataSource;
import edu.ustb.minddata.enums.ResultEnum;
import edu.ustb.minddata.exception.DefinedException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Set;

/**
 * @author UmiSkky
 */
@Slf4j(topic = "database")
public class DynamicDataSourceContextHolder {

    private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>() {
        /**
         * 将 master 数据源的 key作为默认数据源的 key
         */
        @Override
        protected String initialValue() {
            return "defaultDataSource";
        }
    };

    private static final String JDBC_DRIVER = "org.sqlite.JDBC";
    private static final String DB_URL_HEAD = "jdbc:sqlite:";

    /**
     * 切换数据源
     */
    public synchronized static void setDataSourceKey(String key) throws Exception{
        if (!StrUtil.isEmpty(key)) {
            // 当数据源不存在时，添加数据源
            DynamicDataSource dynamicDataSource = DynamicDataSource.getInstance();
            Map<Object, Object> dynamicTargetDataSources = DynamicDataSource.dynamicTargetDataSources;
            Set<Object> keySet = dynamicTargetDataSources.keySet();
            if (!keySet.contains(key)) {
                String path = String.valueOf(Paths.get("" + PathConfig.getPrefixPath(), key + PathConfig.getSuffixPath()));
                if(FileUtil.isFile(path)){
                    String absolutePath = FileUtil.file(path).getAbsolutePath();
                    HikariDataSource dataSource = new HikariDataSource();
                    dataSource.setJdbcUrl(DB_URL_HEAD + absolutePath);
                    dataSource.setDriverClassName(JDBC_DRIVER);
                    dynamicDataSource.addTargetDataSources(key, dataSource);
                    log.info("添加数据源：" + absolutePath);
                }else{
                    log.error(ResultEnum.DATASOURCE_CREATE_ERROR.getResultMsg() + ": " + key);
                    throw new DefinedException(ResultEnum.DATASOURCE_CREATE_ERROR);
                }
            }
            if(keySet.contains(key)){
                contextHolder.set(key);
                log.info("切换数据源：-" + key);
            }else{
                log.error(ResultEnum.DATASOURCE_SWITCH_ERROR.getResultMsg() + ": " + key);
                throw new DefinedException(ResultEnum.DATASOURCE_SWITCH_ERROR);
            }
        }
    }

    /**
     * 获取数据源
     */
    public static String getDataSourceKey() {
        return contextHolder.get();
    }

    /**
     * 重置数据源
     */
    public static void clearDataSourceKey() {
        contextHolder.remove();
        contextHolder.set("defaultDataSource");
        log.info("切换数据源：-defaultDataSource");
    }
}

@Data
@Component
class PathConfig{

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