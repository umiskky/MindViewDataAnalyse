package edu.ustb.minddata.generator;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

/**
 * @author UmiSkky
 */
public class CodeGenerator {

    public static void main(String[] args) {
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();

        // set freemarker engine
        mpg.setTemplateEngine(new FreemarkerTemplateEngine());

        // 配置模板
        TemplateConfig templateConfig = new TemplateConfig();
        //控制 不生成 controller  空字符串就行
        templateConfig.setController("");
        mpg.setTemplate(templateConfig);

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        gc.setOutputDir(projectPath + "/src/main/java");
        gc.setAuthor("umiskky");
        gc.setOpen(false);
        gc.setFileOverride(false); // 是否覆盖
        gc.setServiceName("%sService"); // 去Service的I前缀
        gc.setIdType(IdType.INPUT);
        gc.setDateType(DateType.ONLY_DATE);
        gc.setSwagger2(true);
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:sqlite:src/main/resources/MindviewKey.db");
        dsc.setDriverName("org.sqlite.JDBC");
//        dsc.setUsername("");
//        dsc.setPassword("");
        dsc.setDbType(DbType.SQLITE);
        mpg.setDataSource(dsc);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setParent("edu.ustb");
        pc.setModuleName("minddata");
        pc.setEntity("entity");
        pc.setMapper("mapper");
        pc.setService("service");
//        pc.setController("controller");
        mpg.setPackageInfo(pc);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setInclude("personnel","personnelrecord","testingeyedatas","testingtimedatas");
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setEntityLombokModel(true);

//        strategy.setRestControllerStyle(true);
//        strategy.setControllerMappingHyphenStyle(true);

//        TableFill tableFill = new TableFill("id", FieldFill.INSERT);
//        ArrayList<TableFill> arrayList = new ArrayList<>();
//        arrayList.add(tableFill);
//        strategy.setTableFillList(arrayList);

        // 写于父类中的公共字段
        strategy.setTablePrefix(pc.getModuleName() + "_");
        mpg.setStrategy(strategy);
        mpg.execute();
    }
}
