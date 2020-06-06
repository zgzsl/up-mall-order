package com.zsl.upmall.vo.in;

import lombok.Data;

@Data
public class AddressInfo {
    /** id主键 */
    private Integer id;

    /** 收货人名称 */
    private String name;

    /** 国家代码ID */
    private String countryCode;

    /** 省份代码ID */
    private String provinceCode;

    /** 城市代码ID */
    private String cityCode;

    /** 区域代码ID */
    private String areaCode;

    /** 地址全程 */
    private String fullSite;

    /** 详细收货地址 */
    private String addressDetail;

    /** 手机号码 */
    private String tel;

    /** 是否默认地址 */
    private Integer isDefault;
}
