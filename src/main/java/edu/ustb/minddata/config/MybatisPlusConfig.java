package edu.ustb.minddata.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import edu.ustb.minddata.config.datasource.DynamicDataSource;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author UmiSkky
 */
@Configuration
@MapperScan("edu.ustb.minddata.mapper")
public class MybatisPlusConfig {

    @Bean("defaultDataSource")
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.hikari")
    public DataSource defaultDataSource(){
        return DataSourceBuilder.create().build();
    }

    @Bean("dynamicDataSource")
    public DynamicDataSource dynamicDataSource(@Qualifier("defaultDataSource") DataSource defaultDataSource) {
        DynamicDataSource dynamicDataSource =  DynamicDataSource.getInstance();
        Map<Object, Object> dataSourceMap = new HashMap<>(1);
        dataSourceMap.put("defaultDataSource", defaultDataSource);
        dynamicDataSource.setDefaultDataSource(defaultDataSource);
        dynamicDataSource.setDataSources(dataSourceMap);
        return dynamicDataSource;
    }

    @Bean("sqlSessionFactory")
    @Primary
    public MybatisSqlSessionFactoryBean mybatisSqlSessionFactoryBean(@Qualifier("dynamicDataSource") DynamicDataSource dynamicDataSource,
                                                                     @Qualifier("mybatisPlusInterceptor") MybatisPlusInterceptor interceptor) throws Exception {
        MybatisSqlSessionFactoryBean sessionFactory = new MybatisSqlSessionFactoryBean();
        // ??????????????????????????????????????????????????????????????? dynamicDataSource????????????????????????????????????
        sessionFactory.setDataSource(dynamicDataSource);
        sessionFactory.setPlugins(interceptor);
        return sessionFactory;
    }

    @Bean("mybatisPlusInterceptor")
    public MybatisPlusInterceptor paginationInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();

        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor();
        // ???????????????????????????????????????????????? true??????????????????false ????????????  ??????false
        paginationInnerInterceptor.setOverflow(false);
        // ??????????????????????????????????????? 500 ??????-1 ????????????
        paginationInnerInterceptor.setMaxLimit(100L);
        paginationInnerInterceptor.setDbType(DbType.SQLITE);

        interceptor.addInnerInterceptor(paginationInnerInterceptor);
        return interceptor;
    }

}
