package cloud.huel.spike.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

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
@TableName("t_order")
@ApiModel(value = "Order对象", description = "")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("订单标识")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("创建此订单用户的标识")
    private Long userId;

    @ApiModelProperty("订单关联的商品ID")
    private Integer goodsId;

    @ApiModelProperty("订单的商品数量")
    private Integer amount;

    @ApiModelProperty("订单支付金额")
    private BigDecimal payMoney;

    @ApiModelProperty("订单创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty("订单状态,0表示未支付,1表示已支付,2表示已退款,3表示已发货,4已收货,5表示订单已完成")
    private Integer status;

    @ApiModelProperty("供乐观锁使用")
    private Integer version;


}
