package com.beibei;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.beibei.entity.vo.request.RegisterVO;
import com.beibei.service.UsersService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class HuiYiHealthApplicationTests {
    @Resource
    private UsersService service;

    @Test
    void codeCreate() {
        // 使用 FastAutoGenerator 快速配置代码生成器
        FastAutoGenerator.create("jdbc:mysql://localhost:3306/health?serverTimezone=GMT%2B8", "root", "123456")
                .globalConfig(builder -> {
                    builder.author("Peachlambuct") // 设置作者
                            .outputDir("src/main/java"); // 输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("com.beibei") // 设置父包名
                            .entity("entity.dto") // 设置实体类包名
                            .mapper("mapper") // 设置 Mapper 接口包名
                            .service("service") // 设置 Service 接口包名
                            .serviceImpl("service.impl") // 设置 Service 实现类包名
                            .xml("mappers"); // 设置 Mapper XML 文件包名
                })
                .strategyConfig(builder -> {
                    builder.addInclude("check_projects") // 设置需要生成的表名
                            .entityBuilder()
                            .enableLombok() // 启用 Lombok
                            .enableTableFieldAnnotation() // 启用字段注解
                            .controllerBuilder()
                            .enableRestStyle(); // 启用 REST 风格
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用 Freemarker 模板引擎
                .execute(); // 执行生成
    }

    @Test
    void register() throws Exception {
        service.register(new RegisterVO("liqiuyu", "123456"));
    }
}
