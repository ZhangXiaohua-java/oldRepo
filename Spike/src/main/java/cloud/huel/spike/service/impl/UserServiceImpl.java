package cloud.huel.spike.service.impl;

import cloud.huel.spike.domain.User;
import cloud.huel.spike.mapper.UserMapper;
import cloud.huel.spike.pub.ResponseStatus;
import cloud.huel.spike.service.IUserService;
import cloud.huel.spike.util.MD5Utils;
import cloud.huel.spike.vo.ResultVO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 张晓华
 * @since 2022-09-03
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {


	@Autowired
	private UserMapper userMapper;


	/**
	 * 用户登录
	 *
	 * @param user 用户信息
	 * @return ResultVo 包含状态码
	 */
	@Override
	public ResultVO login(User user) {
		User u = userMapper.selectById(user.getId());
		if (Objects.isNull(u) || !MD5Utils.inputPassToDbPass(user.getPassword(),u.getSalt()).equals(u.getPassword())) {
			return ResultVO.error(ResponseStatus.PARAMETER_EXCEPTION);
		}
		return ResultVO.success(ResponseStatus.SUCCESS).addData("user", u);
	}


}
