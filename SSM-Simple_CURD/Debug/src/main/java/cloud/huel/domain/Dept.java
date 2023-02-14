package cloud.huel.domain;

import java.io.Serializable;
import java.util.List;

/**
 * @author 张晓华
 * @version 1.0
 */
public class Dept implements Serializable {
	private Integer deptId;
	private String deptName;
	private List<Emp> empList;
	public Dept() {
	}

	public Dept(Integer deptId, String deptName) {
		this.deptId = deptId;
		this.deptName = deptName;
	}

	public Integer getDeptId() {
		return deptId;
	}

	public void setDeptId(Integer deptId) {
		this.deptId = deptId;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public List<Emp> getEmpList() {
		return empList;
	}

	public void setEmpList(List<Emp> empList) {
		this.empList = empList;
	}

	@Override
	public boolean equals(Object o) {

		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Dept dept = (Dept) o;

		if (deptId != null ? !deptId.equals(dept.deptId) : dept.deptId != null) return false;
		if (deptName != null ? !deptName.equals(dept.deptName) : dept.deptName != null) return false;
		return empList != null ? empList.equals(dept.empList) : dept.empList == null;
	}

	@Override
	public int hashCode() {
		int result = deptId != null ? deptId.hashCode() : 0;
		result = 31 * result + (deptName != null ? deptName.hashCode() : 0);
		result = 31 * result + (empList != null ? empList.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "Dept{" +
				"deptId=" + deptId +
				", deptName='" + deptName + '\'' +
				", empList=" + empList +
				'}';
	}
}
