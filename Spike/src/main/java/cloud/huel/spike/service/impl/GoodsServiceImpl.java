package cloud.huel.spike.service.impl;

import cloud.huel.spike.constant.UserConstants;
import cloud.huel.spike.domain.Goods;
import cloud.huel.spike.mapper.GoodsMapper;
import cloud.huel.spike.service.IGoodsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 张晓华
 * @since 2022-09-04
 */
@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements IGoodsService {

	@Autowired
	private GoodsMapper goodsMapper;

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	@Override
	public Goods queryGoodsById(Integer id) {
		return goodsMapper.selectById(id);
	}

	@Override
	public String generatePath(Integer uid, Integer gid) {
		ValueOperations<String, Object> ops = redisTemplate.opsForValue();
		String path = UserConstants.USER_PATH + uid + ":" + gid;
		String value = UUID.randomUUID().toString().replaceAll("-", "");
		ops.set(path, value);
		return value;
	}

	@Override
	public boolean checkPath(Integer uid, Integer gid, String path) {
		ValueOperations<String, Object> ops = redisTemplate.opsForValue();
		if (Objects.isNull(uid) || Objects.isNull(gid)) {
			return false;
		}
		String storagePath = (String) ops.get(UserConstants.USER_PATH + uid + ":" + gid);
		return path.equals(storagePath);
	}


}
