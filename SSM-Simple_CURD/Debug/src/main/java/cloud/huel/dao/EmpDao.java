package cloud.huel.dao;

import cloud.huel.domain.Emp;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 张晓华
 * @version 1.0
 */
public interface EmpDao {

	/**
	 * 查询员工信息(分页)
	 * @return List<Emp>
	 */

	List<Emp> queryPartOfEmpList();

	/**
	 * 添加员工
	 * @param emp
	 * @return integer
	 */
	Integer addEmp(Emp emp);

	/**
	 * 检查员工姓名是否已经存在
	 * @param empName
	 * @return
	 */
	Integer checkEmpNameExists(@Param("empName") String empName);

	/**
	 * 根据id查询员工信息
	 * @param id
	 * @return
	 */

	Emp queryEmpById(@Param("id") Integer id);

	/**
	 *
	 * 更新员工信息,只更新部分信息,保证name 与 id 字段不可变
	 * @param emp
	 * @return
	 */

	Integer updateEmp(Emp emp);

	/**
	 * 批量删除员工
	 * @param ids
	 * @return
	 */
	Integer deleteEmp(Integer [] ids);



}
