package element.io.mall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import element.io.mall.common.util.PageUtils;
import element.io.mall.ware.entity.WareInfoEntity;
import element.io.mall.ware.vo.CourierVo;

import java.util.Map;

/**
 * 仓库信息
 *
 * @author 张晓华
 * @email 3323393308@qq.com
 * @date 2022-10-27 20:53:27
 */
public interface WareInfoService extends IService<WareInfoEntity> {

	PageUtils queryPage(Map<String, Object> params);


	CourierVo countFee(Long addId);
	
}

