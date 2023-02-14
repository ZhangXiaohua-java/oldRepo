package cloud.huel.service;

import cloud.huel.domain.Emp;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author 张晓华
 * @version 1.0
 */
public interface EmpService {

	/**
	 * 获取分页的详细信息
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	PageInfo<Emp> queryEmpByPage(int pageNum, int pageSize);

	/**
	 * 添加员工信息
	 * @param emp
	 * @return
	 */
	Integer addEmp(Emp emp);

	/**
	 * 检查用户名是否已经存在,以决定是否可以执行向数据库的添加操作
	 * @param empName
	 * @return
	 */
	boolean queryEmpNameIsExists(String empName);


	/**
	 * 根据员工的ID查询员工的全部信息
	 * @param id
	 * @return
	 */
	Emp queryEmpById(Integer id);

	/**
	 * 根据传入的Emp对象有选择性的更新员工信息
	 * @param emp
	 * @return
	 */
	Integer updateEmpInfo(Emp emp);

	/**
	 * 批量删除员工
	 * @param ids
	 * @return
	 */
	Integer batchDeleteEmp(Integer [] ids);


}
