package element.io.mall.ware.controller;

import element.io.mall.common.util.PageUtils;
import element.io.mall.common.util.R;
import element.io.mall.ware.entity.PurchaseDetailEntity;
import element.io.mall.ware.service.PurchaseDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;


/**
 * @author 张晓华
 * @email 3323393308@qq.com
 * @date 2022-10-27 20:53:27
 */
@RestController
@RequestMapping("ware/purchasedetail")
public class PurchaseDetailController {
	@Autowired
	private PurchaseDetailService purchaseDetailService;

	/**
	 * 列表
	 * ware/purchasedetail/list
	 */
	@RequestMapping("/list")
	//@RequiresPermissions("ware:purchasedetail:list")
	public R list(@RequestParam Map<String, Object> params) {
		PageUtils page = purchaseDetailService.queryPage(params);

		return R.ok().put("page", page);
	}


	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	//@RequiresPermissions("ware:purchasedetail:info")
	public R info(@PathVariable("id") Long id) {
		PurchaseDetailEntity purchaseDetail = purchaseDetailService.getById(id);

		return R.ok().put("purchaseDetail", purchaseDetail);
	}

	/**
	 * 保存
	 */
	@RequestMapping("/save")
	//@RequiresPermissions("ware:purchasedetail:save")
	public R save(@RequestBody PurchaseDetailEntity purchaseDetail) {
		purchaseDetailService.save(purchaseDetail);

		return R.ok();
	}

	/**
	 * 修改
	 */
	@RequestMapping("/update")
	//@RequiresPermissions("ware:purchasedetail:update")
	public R update(@RequestBody PurchaseDetailEntity purchaseDetail) {
		purchaseDetailService.updateById(purchaseDetail);

		return R.ok();
	}

	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	//@RequiresPermissions("ware:purchasedetail:delete")
	public R delete(@RequestBody Long[] ids) {
		purchaseDetailService.removeByIds(Arrays.asList(ids));

		return R.ok();
	}

}
