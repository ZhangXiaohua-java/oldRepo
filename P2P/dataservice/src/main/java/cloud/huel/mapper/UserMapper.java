package cloud.huel.mapper;

import cloud.huel.domain.user.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface UserMapper {

	int deleteByPrimaryKey(Integer id);

	int insert(User record);

	int insertSelective(User record);

	User selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(User record);

	int updateByPrimaryKey(User record);

	/**
	 * 查询平台总人数
	 *
	 * @return 平台注册总人数
	 */
	@Select("select count(*) from u_user")
	Long selectTotalUserCount();

	/**
	 * 查询手机号是否已经被注册
	 *
	 * @param phone 手机号
	 * @return 是否已经被注册
	 */
	@Select("select count(*) from u_user where phone = #{phone}")
	Integer phoneExists(@Param("phone") String phone);

	/**
	 * 根据手机号和用户登录密码确认是否存在指定的用户信息
	 *
	 * @param user 手机号与密码
	 * @return 用户信息
	 */
	User selectUserByPhoneAndPassword(User user);


}