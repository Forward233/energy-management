package com.yunpower.system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import com.yunpower.common.security.annotation.EnableCustomConfig;
import com.yunpower.common.security.annotation.EnableRyFeignClients;
import com.yunpower.common.swagger.annotation.EnableCustomSwagger2;
import org.mybatis.spring.annotation.MapperScan;

/**
 * 系统模块
 * 
 * @author yunpower
 */
@EnableCustomConfig
@EnableCustomSwagger2
@EnableRyFeignClients
@MapperScan("com.yunpower.system.mapper")
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, MybatisPlusAutoConfiguration.class})
public class YunPowerSystemApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(YunPowerSystemApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  主系统模块启动成功   ლ(´ڡ`ლ)ﾞ  \n");
    }
}
