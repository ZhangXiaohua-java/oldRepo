package cloud.huel.spike.service.impl;

import cloud.huel.spike.constant.RedisConstants;
import cloud.huel.spike.domain.SpikeGoods;
import cloud.huel.spike.mapper.SpikeGoodsMapper;
import cloud.huel.spike.service.ISpikeGoodsService;
import cloud.huel.spike.vo.SpikeGoodsVO;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 张晓华
 * @since 2022-09-04
 */
@Service
public class SpikeGoodsServiceImpl extends ServiceImpl<SpikeGoodsMapper, SpikeGoods> implements ISpikeGoodsService{

	@Autowired
	private SpikeGoodsMapper spikeGoodsMapper;

	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	private ThreadLocal<Map<Integer, Boolean>> threadLocal = new ThreadLocal<>();

	/**
	 * 查询所有的秒杀商品
	 *
	 * @return
	 */
	@Override
	public List<SpikeGoodsVO> querySpikeGoodsForPage() {
		return spikeGoodsMapper.selectAllSpikeGoodsForPage();
	}


	/**
	 * 查询秒杀商品详情
	 *
	 * @param id 商品ID
	 * @return 商品详情
	 */
	@Override
	public SpikeGoodsVO querySpikeGoodsByGooodsId(Integer id) {
		assert Objects.nonNull(id) : "id不允许为空";
		return spikeGoodsMapper.selectGoodsByGoodsId(id);
	}





}
