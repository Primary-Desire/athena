package cn.ruiheyun.athena.configuration;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("cn.ruiheyun.athena.*.mapper")
public class MybatisPlusConfiguration {
}
