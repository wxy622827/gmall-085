package com.atguigu.gmall.sms;

/**
 * @author wxy
 * @create 2020-01-06 13:11
 */

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
@Data
public class SaleVO {
    private Long skuId;
    private BigDecimal growBounds;
    private BigDecimal buyBounds;
    private List<String> work;

    private BigDecimal fullPrice;
    private BigDecimal reducePrice;
    private Integer fullAddOther;

    private  Integer fullCount;
    private BigDecimal discount;
    private Integer ladderOther;

}
