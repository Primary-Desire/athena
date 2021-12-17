package cn.ruiheyun.athena.admin.service;

import cn.ruiheyun.athena.admin.entity.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;

/**
 * <p>
 * 系统用户 服务类
 * </p>
 *
 * @author LauHonban
 * @since 2021-12-16
 */
public interface ISysUserService extends IService<SysUser>, ReactiveUserDetailsService {

    boolean updateLastLoginInfo(String username, String ip);

}
