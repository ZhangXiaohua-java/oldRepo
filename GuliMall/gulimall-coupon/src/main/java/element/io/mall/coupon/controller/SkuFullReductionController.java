package element.io.mall.coupon.controller;

import element.io.mall.common.to.SkuReductionTo;
import element.io.mall.common.util.PageUtils;
import element.io.mall.common.util.R;
import element.io.mall.coupon.entity.SkuFullReductionEntity;
import element.io.mall.coupon.service.SkuFullReductionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;


/**
 * 商品满减信息
 *
 * @author 张晓华
 * @email 3323393308@qq.com
 * @date 2022-10-27 20:31:53
 */
@RestController
@RequestMapping("coupon/skufullreduction")
public class SkuFullReductionController {
	@Autowired
	private SkuFullReductionService skuFullReductionService;

	/**
	 * 列表
	 */
	@RequestMapping("/list")
	//@RequiresPermissions("coupon:skufullreduction:list")
	public R list(@RequestParam Map<String, Object> params) {
		PageUtils page = skuFullReductionService.queryPage(params);

		return R.ok().put("page", page);
	}


	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	//@RequiresPermissions("coupon:skufullreduction:info")
	public R info(@PathVariable("id") Long id) {
		SkuFullReductionEntity skuFullReduction = skuFullReductionService.getById(id);

		return R.ok().put("skuFullReduction", skuFullReduction);
	}

	/**
	 * 保存
	 */
	@RequestMapping("/save")
	//@RequiresPermissions("coupon:skufullreduction:save")
	public R save(@RequestBody SkuFullReductionEntity skuFullReduction) {
		skuFullReductionService.save(skuFullReduction);

		return R.ok();
	}

	/**
	 * 修改
	 */
	@RequestMapping("/update")
	//@RequiresPermissions("coupon:skufullreduction:update")
	public R update(@RequestBody SkuFullReductionEntity skuFullReduction) {
		skuFullReductionService.updateById(skuFullReduction);

		return R.ok();
	}

	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	//@RequiresPermissions("coupon:skufullreduction:delete")
	public R delete(@RequestBody Long[] ids) {
		skuFullReductionService.removeByIds(Arrays.asList(ids));

		return R.ok();
	}


	@PostMapping("/reduction/save")
	public R saveDeatailReduction(SkuReductionTo skuReductionTo) {
		if (skuFullReductionService.saveReduction(skuReductionTo)) {
			return R.ok();
		} else {
			return R.error();
		}
	}

}
