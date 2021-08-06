package edu.ustb.minddata.config.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.util.HashMap;
import java.util.Map;

/**
 * @author UmiSkky
 */
public class DynamicDataSource extends AbstractRoutingDataSource{

    public static Map<Object, Object> dynamicTargetDataSources = new HashMap<>();

    private static DynamicDataSource dynamicDataSource = null;

    private DynamicDataSource() {}

    public static synchronized DynamicDataSource getInstance() {
        if (dynamicDataSource == null) {
            dynamicDataSource = new DynamicDataSource();
        }
        return dynamicDataSource;
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return DynamicDataSourceContextHolder.getDataSourceKey();
    }

    @Override
    public void setTargetDataSources(Map<Object, Object> targetDataSources) {
        dynamicTargetDataSources = targetDataSources;
        super.setTargetDataSources(targetDataSources);
    }

    public void setDefaultDataSource(Object defaultDataSource) {
        super.setDefaultTargetDataSource(defaultDataSource);
    }

    public void setDataSources(Map<Object, Object> dataSources) {
        setTargetDataSources(dataSources);
    }

    public void addTargetDataSources(Object key, Object dataSource) {
        dynamicTargetDataSources.put(key, dataSource);
        super.setTargetDataSources(dynamicTargetDataSources);
        super.afterPropertiesSet();
    }

}
