package element.io.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import element.io.mall.common.util.PageUtils;
import element.io.mall.product.entity.SpuImagesEntity;

import java.util.List;
import java.util.Map;

/**
 * spu图片
 *
 * @author 张晓华
 * @email 3323393308@qq.com
 * @date 2022-10-27 20:11:30
 */
public interface SpuImagesService extends IService<SpuImagesEntity> {

	PageUtils queryPage(Map<String, Object> params);

	void saveImages(Long id, List<String> images);
	

}

