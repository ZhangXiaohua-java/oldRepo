package cloud.huel.spike.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author 张晓华
 * @date 2022-9-6
 */
@Data
public class SpikeGoodsVO implements Serializable {

	private static final long serialVersionUID = 3949497447592048273L;


	@ApiModelProperty("商品ID")
	private Integer id;

	@ApiModelProperty("商品名称")
	private String goodsName;

	@ApiModelProperty("商品原价")
	private BigDecimal originPrice;

	@ApiModelProperty("图片的存储路径")
	private String imgAddress;

	@ApiModelProperty("参与秒杀活动的商品的数量")
	private Integer amount;

	@ApiModelProperty("参与秒杀活动的商品的库存数量")
	private Integer stock;

	@ApiModelProperty("此商品的秒杀活动开始时间")
	private Date startTime;

	@ApiModelProperty("商品秒杀活动结束时间")
	private Date endTime;

	@ApiModelProperty("秒杀活动的商品价格")
	private BigDecimal price;
	

}
