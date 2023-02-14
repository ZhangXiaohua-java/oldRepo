package cloud.huel.spike.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.math.BigDecimal;
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
@TableName("t_goods")
@ApiModel(value = "Goods对象", description = "")
public class Goods implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("商品ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("商品名称")
    private String goodsName;

    private BigDecimal price;

    @ApiModelProperty("-1表示库存无限制")
    private Long stock;

    private String imgAddress;

    @ApiModelProperty("商品关键字")
    private String title;

    @ApiModelProperty("商品描述信息")
    private String describeInfo;


}
