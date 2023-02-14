package cloud.huel.domain;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * @author 张晓华
 * @version 1.0
 */
public class Emp  implements Serializable {

	private Integer id;
	@Pattern(regexp = "[\\u4e00-\\u9fa5]{2,5}",message = "用户名不合法")
	private String empName;
	@Max(value = 89,message = "年龄超出了允许的范围")
	private Integer empAge;
	private String empGender;
	private Integer did;
	private Dept dept;

	public Emp() {
	}

	public Emp(Integer id, String empName, Integer empAge, String empGender, Integer did, Dept dept) {
		this.id = id;
		this.empName = empName;
		this.empAge = empAge;
		this.empGender = empGender;
		this.did = did;
		this.dept = dept;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Emp emp = (Emp) o;

		if (id != null ? !id.equals(emp.id) : emp.id != null) return false;
		if (empName != null ? !empName.equals(emp.empName) : emp.empName != null) return false;
		if (empAge != null ? !empAge.equals(emp.empAge) : emp.empAge != null) return false;
		if (empGender != null ? !empGender.equals(emp.empGender) : emp.empGender != null) return false;
		if (did != null ? !did.equals(emp.did) : emp.did != null) return false;
		return dept != null ? dept.equals(emp.dept) : emp.dept == null;
	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (empName != null ? empName.hashCode() : 0);
		result = 31 * result + (empAge != null ? empAge.hashCode() : 0);
		result = 31 * result + (empGender != null ? empGender.hashCode() : 0);
		result = 31 * result + (did != null ? did.hashCode() : 0);
		result = 31 * result + (dept != null ? dept.hashCode() : 0);
		return result;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public Integer getEmpAge() {
		return empAge;
	}

	public void setEmpAge(Integer empAge) {
		this.empAge = empAge;
	}

	public String getEmpGender() {
		return empGender;
	}

	public void setEmpGender(String empGender) {
		this.empGender = empGender;
	}

	public Integer getDid() {
		return did;
	}

	public void setDid(Integer did) {
		this.did = did;
	}

	public Dept getDept() {
		return dept;
	}

	public void setDept(Dept dept) {
		this.dept = dept;
	}

	@Override
	public String toString() {
		return "Emp{" +
				"id=" + id +
				", empName='" + empName + '\'' +
				", empAge=" + empAge +
				", empGender='" + empGender + '\'' +
				", did=" + did +
				", dept=" + dept +
				'}';
	}
}
