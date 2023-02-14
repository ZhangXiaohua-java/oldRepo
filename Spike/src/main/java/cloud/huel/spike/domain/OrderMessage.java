package cloud.huel.spike.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author 张晓华
 * @date 2022-9-8
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderMessage implements Serializable {


	private static final long serialVersionUID = 1718797515189278571L;

	private Long uid;

	private Integer gid;


}
