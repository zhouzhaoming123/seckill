package com.zzmhome.seckill.vo;

import com.zzmhome.seckill.validator.IsMobile;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

/**
 * Data:
 * Author: zhouzm
 * ---------------------------
 */
@Data
public class LoginVo {
    @NotNull
    @IsMobile
    private String mobile;

    @NotNull
    @Length(max = 11)
    private String password;
}
