package cloud.huel.service;

import cloud.huel.domain.Admin;

/**
 * @author 张晓华
 * @version 1.0
 */
public interface LoginService {

	/**
	 * 查询数据库中是否存在管理员信息
	 * @param admin
	 * @return
	 */
	Admin adminLogin(Admin admin);


}
