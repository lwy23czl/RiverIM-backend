package cn.river.im;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
//开启缓存功能
@EnableCaching
@MapperScan("cn.river.im.mapper")
public class ApiRun {
    public static void main(String[] args) {
        SpringApplication.run(ApiRun.class, args);
    }
}

