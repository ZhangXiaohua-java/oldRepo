package cloud.huel.spike.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author 张晓华
 * @since 2022-09-04
 */
@Getter
@Setter
@TableName("t_spike_goods")
@ApiModel(value = "SpikeGoods对象", description = "")
public class SpikeGoods implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("秒杀商品的标识")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("关联的商品ID")
    private Integer goodsId;

    @ApiModelProperty("参与秒杀活动的商品的数量")
    private Integer amount;

    @ApiModelProperty("参与秒杀活动的商品的库存数量")
    private Integer stock;

    @ApiModelProperty("此商品的秒杀活动开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty("商品秒杀活动结束时间")
    private LocalDateTime endTime;

    @ApiModelProperty("秒杀活动的商品价格")
    private BigDecimal price;


}
