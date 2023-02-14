package cloud.huel.domain;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author 张晓华
 * @version 1.0
 */
public class Admin implements Serializable {
	private Integer id;
	@NotNull
	private String username;
	@NotNull
	private String password;
//	只有0 和 1 两个值,0 代表被禁用的账户,1 代表正常的账户
	private Integer status;

	public Admin() {
	}

	public Admin(Integer id, String username, String password, Integer status) {
		this.id = id;
		this.username = username;
		this.password = password;
		this.status = status;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Admin admin = (Admin) o;

		if (id != null ? !id.equals(admin.id) : admin.id != null) return false;
		if (username != null ? !username.equals(admin.username) : admin.username != null) return false;
		if (password != null ? !password.equals(admin.password) : admin.password != null) return false;
		return status != null ? status.equals(admin.status) : admin.status == null;
	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (username != null ? username.hashCode() : 0);
		result = 31 * result + (password != null ? password.hashCode() : 0);
		result = 31 * result + (status != null ? status.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "Admin{" +
				"id=" + id +
				", username='" + username + '\'' +
				", password='" + password + '\'' +
				", status=" + status +
				'}';
	}

}
