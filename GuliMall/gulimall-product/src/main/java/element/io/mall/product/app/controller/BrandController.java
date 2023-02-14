package element.io.mall.product.app.controller;

import element.io.mall.common.util.PageUtils;
import element.io.mall.common.util.R;
import element.io.mall.common.validation.AppendGroup;
import element.io.mall.common.validation.ModifyGroup;
import element.io.mall.product.entity.BrandEntity;
import element.io.mall.product.service.BrandService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;


/**
 * 品牌
 *
 * @author 张晓华
 * @email 3323393308@qq.com
 * @date 2022-10-27 20:11:30
 */
@Slf4j
@RestController
@RequestMapping("product/brand")
public class BrandController {
	@Autowired
	private BrandService brandService;

	/**
	 * 列表
	 */
	@RequestMapping("/list")
	//@RequiresPermissions("product:brand:list")
	public R list(@RequestParam Map<String, Object> params) {
		PageUtils page = brandService.queryPage(params);
		return R.ok().put("page", page);
	}


	/**
	 * 信息
	 */
	@RequestMapping("/info/{brandId}")
	//@RequiresPermissions("product:brand:info")
	public R info(@PathVariable("brandId") Long brandId) {
		BrandEntity brand = brandService.getById(brandId);
		return R.ok().put("brand", brand);
	}

	/**
	 * 保存
	 */
	@RequestMapping("/save")
	//@RequiresPermissions("product:brand:save")
	public R save(@Validated({AppendGroup.class}) @RequestBody BrandEntity brand) {
		if (brandService.save(brand)) {
			return R.ok();
		} else {
			return R.error();
		}

	}

	/**
	 * 修改
	 */
	@RequestMapping("/update")
	//@RequiresPermissions("product:brand:update")
	public R update(@RequestBody @Validated({ModifyGroup.class}) BrandEntity brand) {
		brandService.updateBrandInfoCaseCade(brand);
		return R.ok();
	}

	@RequestMapping("/update/status")
	public R updateStatus(@RequestBody BrandEntity brand) {
		brandService.updateById(brand);
		return R.ok();
	}

	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	//@RequiresPermissions("product:brand:delete")
	public R delete(@RequestBody Long[] brandIds) {
		brandService.removeByIds(Arrays.asList(brandIds));
		return R.ok();
	}


}
