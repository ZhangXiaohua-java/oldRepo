package element.io.mall.ware.controller;

import element.io.mall.common.util.PageUtils;
import element.io.mall.common.util.R;
import element.io.mall.ware.entity.WareInfoEntity;
import element.io.mall.ware.service.WareInfoService;
import element.io.mall.ware.vo.CourierVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;


/**
 * 仓库信息
 *
 * @author 张晓华
 * @email 3323393308@qq.com
 * @date 2022-10-27 20:53:27
 */
@RestController
@RequestMapping("ware/wareinfo")
public class WareInfoController {
	@Autowired
	private WareInfoService wareInfoService;

	/**
	 * 列表
	 */
	@RequestMapping("/list")
	//@RequiresPermissions("ware:wareinfo:list")
	public R list(@RequestParam Map<String, Object> params) {
		PageUtils page = wareInfoService.queryPage(params);
		return R.ok().put("page", page);
	}


	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	//@RequiresPermissions("ware:wareinfo:info")
	public R info(@PathVariable("id") Long id) {
		WareInfoEntity wareInfo = wareInfoService.getById(id);

		return R.ok().put("wareInfo", wareInfo);
	}

	/**
	 * 保存
	 */
	@RequestMapping("/save")
	//@RequiresPermissions("ware:wareinfo:save")
	public R save(@RequestBody WareInfoEntity wareInfo) {
		wareInfoService.save(wareInfo);

		return R.ok();
	}

	/**
	 * 修改
	 */
	@RequestMapping("/update")
	//@RequiresPermissions("ware:wareinfo:update")
	public R update(@RequestBody WareInfoEntity wareInfo) {
		wareInfoService.updateById(wareInfo);

		return R.ok();
	}

	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	//@RequiresPermissions("ware:wareinfo:delete")
	public R delete(@RequestBody Long[] ids) {
		wareInfoService.removeByIds(Arrays.asList(ids));
		return R.ok();
	}

	@GetMapping("/count/fee/{addId}")
	public CourierVo countFee(@PathVariable Long addId) {
		return wareInfoService.countFee(addId);
	}


}
