package element.io.mall.product.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import element.io.mall.product.entity.CategoryEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品三级分类
 *
 * @author 张晓华
 * @email 3323393308@qq.com
 * @date 2022-10-27 20:11:30
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {

	List<CategoryEntity> batchFindCategoryNames(@Param("list") List<Long> catelogIds);

	List<CategoryEntity> selectAllCategories();

}
