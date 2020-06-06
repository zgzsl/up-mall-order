package com.zsl.upmall.aid;

import lombok.Data;

import java.io.Serializable;

/**
 * app分页组件
 * @author binggleWang
 * @version $Id: AppPage.java, v 0.1 2020年04月08日 下午2:31:23
 */
@Data
public class PageParam<T>  implements Serializable{
	
	private static final long serialVersionUID = -7248374800878487522L;
	/**
     * <p>当前页</p>
     */
    private int pageNum=1;
    /**
     * <p>每页记录数</p>
     */
    private int pageSize=10;
}
