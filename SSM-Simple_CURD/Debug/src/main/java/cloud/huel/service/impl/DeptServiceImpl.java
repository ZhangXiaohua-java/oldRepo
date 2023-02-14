package cloud.huel.service.impl;

import cloud.huel.dao.DeptDao;
import cloud.huel.domain.Dept;
import cloud.huel.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author 张晓华
 * @version 1.0
 */
@Service
public class DeptServiceImpl implements DeptService {
	@Autowired
	DeptDao deptDao;
	/**
	 * 查询所有的部门信息
	 *
	 * @return
	 */
	@Override
	public List<Dept> queryAllDept() {
		List<Dept> list = deptDao.queryAllDept();
		Objects.requireNonNull(list,"cloud.huel.service.impl.DeptServiceImpl.queryAllDept查询出错");
		return list;
	}
}
