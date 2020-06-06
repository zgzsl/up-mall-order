package com.zsl.upmall.vo.out;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @ClassName GrouponListVo
 * @Description 参团列表
 * @Author binggleW
 * @Date 2020-05-13 10:22
 * @Version 1.0
 **/
@Data
public class GrouponListVo {
    private String groupNickName;
    private String groupAvatar;
    private String grouponName;
    private String nickName;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date payTime;
    private Integer goodsCount;
}
