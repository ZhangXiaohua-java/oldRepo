package element.io.mall.product.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import element.io.mall.common.util.PageUtils;
import element.io.mall.product.dao.SkuSaleAttrValueDao;
import element.io.mall.product.entity.SkuSaleAttrValueEntity;
import element.io.mall.product.service.SkuSaleAttrValueService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service("skuSaleAttrValueService")
public class SkuSaleAttrValueServiceImpl extends ServiceImpl<SkuSaleAttrValueDao, SkuSaleAttrValueEntity> implements SkuSaleAttrValueService {

	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		return null;
	}


	@Override
	public void batchSaveSaleAttrs(List<SkuSaleAttrValueEntity> attrValueEntities) {
		this.saveBatch(attrValueEntities);
	}

	@Override
	public List<String> queryAllSaleAttrs(Long skuId) {
		return this.baseMapper.selectSaleAttrsWithSkuId(skuId);
	}
}