package cloud.huel.service;

import cloud.huel.domain.Dept;

import java.util.List;

/**
 * @author 张晓华
 * @version 1.0
 */
public interface DeptService {
	/**
	 * 查询所有的部门信息
	 * @return
	 */
	public List<Dept> queryAllDept();
}
