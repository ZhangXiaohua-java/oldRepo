package cloud.huel.service.impl;

import cloud.huel.dao.EmpDao;
import cloud.huel.domain.Emp;
import cloud.huel.service.EmpService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 张晓华
 * @version 1.0
 */
@Service
public class EmpServiceImpl implements EmpService {
	@Resource(name = "empDao")
	private EmpDao dao;
	/**
	 * 获取分页的详细信息
	 *
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	@Override
	public PageInfo<Emp> queryEmpByPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum,pageSize);
		List<Emp> empList = dao.queryPartOfEmpList();
		return  new PageInfo<>(empList,9);
	}

	/**
	 * 添加员工信息
	 *
	 * @param emp
	 * @return
	 */
	@Override
	public Integer addEmp(Emp emp) {
		return dao.addEmp(emp);
	}

	/**
	 * 检查用户名是否已经存在,以决定是否可以执行向数据库的添加操作
	 *
	 * @param empName
	 * @return
	 */
	@Override
	public boolean queryEmpNameIsExists(String empName) {
		Integer result = dao.checkEmpNameExists(empName);
		if (result == 0 || result == null) {
			return true;
		}
		return false;
	}

	/**
	 * 根据员工的ID查询员工的全部信息
	 *
	 * @param id
	 * @return
	 */
	@Override
	public Emp queryEmpById(Integer id) {
		return dao.queryEmpById(id);
	}

	/**
	 * 根据传入的Emp对象有选择性的更新员工信息
	 *
	 * @param emp
	 * @return
	 */
	@Override
	public Integer updateEmpInfo(Emp emp) {
		return  dao.updateEmp(emp);
	}

	/**
	 * 批量删除员工
	 *
	 * @param ids
	 * @return
	 */
	@Override
	public Integer batchDeleteEmp(Integer[] ids) {
		return dao.deleteEmp(ids);
	}

}
