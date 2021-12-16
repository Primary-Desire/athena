package cn.ruiheyun.athena.util;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;

public class GeneratorUtils {

    public static void main(String[] args) {
        String url = "jdbc:p6spy:mysql://localhost:3306/athena?serverTimezone=Asia/Shanghai";
        String username = "root";
        String password = "Share@2498";

        try {
            FastAutoGenerator.create(url, username, password).globalConfig(builder -> {
                builder.author(System.getenv("USERNAME"))
                        .disableOpenDir()
                        .fileOverride()
                        .outputDir(String.format("%s\\src\\main\\java", System.getProperty("user.dir")));
            }).packageConfig(builder -> {
                builder.parent("cn.ruiheyun.athena")
                        .moduleName("admin");
            }).execute();
        } finally {
            System.exit(0);
        }

    }

}
