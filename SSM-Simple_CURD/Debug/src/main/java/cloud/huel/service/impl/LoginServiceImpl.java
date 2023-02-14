package cloud.huel.service.impl;

import cloud.huel.dao.AdminDao;
import cloud.huel.domain.Admin;
import cloud.huel.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author 张晓华
 * @version 1.0
 */
@Service
public class LoginServiceImpl implements LoginService {
	@Autowired
	private AdminDao dao;
	/**
	 * 查询数据库中是否存在管理员信息
	 *
	 * @param admin
	 * @return
	 */
	@Override
	public Admin adminLogin(Admin admin) {
		return dao.loginCheck(admin);
	}


}
