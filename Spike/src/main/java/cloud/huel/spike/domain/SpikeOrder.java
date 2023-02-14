package cloud.huel.spike.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
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
@TableName("t_spike_order")
@ApiModel(value = "SpikeOrder对象", description = "")
public class SpikeOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("创建订单的用户标识")
    private Long userId;

    @ApiModelProperty("关联的订单ID")
    private Integer orderId;

    @ApiModelProperty("秒杀抢到的商品的ID")
    private Integer goodsId;


}
