package cloud.huel.spike.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author 张晓华
 * @date 2022-9-7
 */
@Data
public class SpikeOrderVO implements Serializable {

	private static final long serialVersionUID = -3635897227811956411L;

	private String goodsName;

	private String imgAddress;

	private BigDecimal payMoney;

	private Date createTime;

	private Integer status;


}
