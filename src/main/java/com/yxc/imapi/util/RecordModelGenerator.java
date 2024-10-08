package com.yxc.imapi.util;

import com.alibaba.druid.pool.DruidDataSource;
import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.generator.Generator;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.io.ClassPathResource;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * 依据表结构为activerecord生成model和baseModel、MappingKit。
 */
public class RecordModelGenerator {

    public static DataSource getDataSource() {
        DruidDataSource dataSource = new DruidDataSource();
        try{
            YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
            yaml.setResources(new ClassPathResource("application-local.yml"));
            Properties properties = yaml.getObject();
            dataSource.setUrl(properties.getProperty("spring.datasource.url"));
            dataSource.setUsername(properties.getProperty("spring.datasource.username"));
            dataSource.setPassword(properties.getProperty("spring.datasource.password"));
        }catch(Exception e){
            e.printStackTrace();
        }
        return dataSource;
    }

    public static void main(String[] args) {
        // base model 所使用的包名
        String baseModelPackageName = "com.yxc.imapi.model.base";
        // base model 文件保存路径
        String baseModelOutputDir = PathKit.getWebRootPath() + "/src/main/java/com/yxc/imapi/model/base";

        // model 所使用的包名 (MappingKit 默认使用的包名)
        String modelPackageName = "com.yxc.imapi.model";
        // model 文件保存路径 (MappingKit 与 DataDictionary 文件默认保存路径)
        String modelOutputDir = baseModelOutputDir + "/..";

        // 创建生成器
        Generator generator = new Generator(getDataSource(), baseModelPackageName, baseModelOutputDir, modelPackageName,
                modelOutputDir);
        // 设置是否生成链式 setter 方法
        generator.setGenerateChainSetter(false);

        // 设置是否在 Model 中生成 dao 对象
        generator.setGenerateDaoInModel(true);
        // 设置是否生成链式 setter 方法
        generator.setGenerateChainSetter(true);
        // 设置是否生成字典文件
        generator.setGenerateDataDictionary(false);
        generator.setMetaBuilder(new _MetaBuilder(getDataSource()));
        // 设置需要被移除的表名前缀用于生成modelName。例如表名 "osc_user"，移除前缀 "osc_"后生成的model名为
        // "User"而非 OscUser
        generator.setRemovedTableNamePrefixes("t_","h_","zd");
        // 生成
        generator.generate();
    }
}
