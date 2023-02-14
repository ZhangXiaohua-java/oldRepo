package cloud.huel.spike.service;

import cloud.huel.spike.domain.Goods;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 张晓华
 * @since 2022-09-04
 */
public interface IGoodsService extends IService<Goods> {


	Goods queryGoodsById(Integer id);

	String generatePath(Integer uid, Integer gid);

	boolean checkPath(Integer uid, Integer gid, String path);


}
