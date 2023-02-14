package cloud.huel.spike.service;

import cloud.huel.spike.domain.User;
import cloud.huel.spike.vo.ResultVO;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 张晓华
 * @since 2022-09-03
 */
public interface IUserService extends IService<User> {

	/**
	 *  用户登录
	 * @param user 用户信息
	 * @return ResultVo 包含状态码
	 */
	ResultVO login(User user);


}
