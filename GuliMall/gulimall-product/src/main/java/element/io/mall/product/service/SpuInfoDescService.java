package element.io.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import element.io.mall.common.util.PageUtils;
import element.io.mall.product.entity.SpuInfoDescEntity;

import java.util.Map;

/**
 * spu信息介绍
 *
 * @author 张晓华
 * @email 3323393308@qq.com
 * @date 2022-10-27 20:11:30
 */
public interface SpuInfoDescService extends IService<SpuInfoDescEntity> {

	PageUtils queryPage(Map<String, Object> params);

	void saveDesc(SpuInfoDescEntity spuInfoDescEntity);


	SpuInfoDescEntity getSpuDescBySpuId(Long spuId);
	
}

