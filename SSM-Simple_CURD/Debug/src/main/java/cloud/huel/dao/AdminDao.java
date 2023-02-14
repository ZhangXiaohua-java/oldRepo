package cloud.huel.dao;

import cloud.huel.domain.Admin;

/**
 * @author 张晓华
 * @version 1.0
 */
public interface AdminDao {

	/**
	 * 检查数据库中是否存在管理员信息
	 * @param admin
	 * @return
	 */
	Admin loginCheck(Admin admin);


}
