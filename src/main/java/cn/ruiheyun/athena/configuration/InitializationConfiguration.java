package cn.ruiheyun.athena.configuration;

import cn.ruiheyun.athena.admin.entity.SysUser;
import cn.ruiheyun.athena.admin.service.ISysUserService;
import cn.ruiheyun.athena.auth.service.IRSAKeyService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.Resource;

@Configuration
public class InitializationConfiguration {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Resource
    private IRSAKeyService keyService;
    @Resource
    private ISysUserService sysUserService;

    @Bean
    public void initializationKey() {
        keyService.findKey()
                .filter(key -> StringUtils.isNotBlank(key.getPrivateKey()) && StringUtils.isNotBlank(key.getPublicKey()))
                .switchIfEmpty(keyService.saveKey())
                .subscribe();
    }

    @Bean
    public void initializationRootAdmin() {
        SysUser sysUser = new SysUser();
        sysUser.setUsername("root");
        sysUser.setPassword(passwordEncoder.encode("root"));
    }

}
