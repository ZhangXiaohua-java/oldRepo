package cloud.huel.spike.domain;

import cloud.huel.spike.validator.Regex;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author 张晓华
 * @since 2022-09-03
 */

@Data
@TableName("t_user")
@ApiModel(value = "User对象", description = "")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("用户手机号做ID")
    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(message = "手机号不允许为空", groups = {Login.class})
    //@Regex(regex = "^(1[3-9]{2})([0-9]{8})$", groups = {Login.class})
    private Long id;

    @ApiModelProperty("用户昵称")
    private String nickname;

    @ApiModelProperty("用户密码MD5")
    @Size(min = 32, max = 32, message = "非法密码", groups = {Login.class})
    @NotBlank(message = "密码不允许为空", groups = {Login.class})
    private String password;

    @ApiModelProperty("用户密码盐")
    private String salt;

    @ApiModelProperty("用户注册时间")
    private LocalDateTime registryDate;

    @ApiModelProperty("记录用户上一次的登录时间")
    private LocalDateTime lastLoginTime;

    @ApiModelProperty("记录用户的登录次数")
    @Min(0)
    private Long loginCount;


}
