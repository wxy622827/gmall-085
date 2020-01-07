package com.atguigu.gmall.pms.vo;

import com.atguigu.gmall.pms.entity.SkuInfoEntity;
import com.atguigu.gmall.pms.entity.SkuSaleAttrValueEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author wxy
 * @create 2020-01-06 0:12
 */
@Data
public class SkuInfoVO extends SkuInfoEntity {
    private BigDecimal growBounds;
    private BigDecimal buyBounds;
    private List<String> work;

    private BigDecimal fullPrice;
    private BigDecimal reducePrice;
    private Integer fullAddOther;

    private  Integer fullCount;
    private BigDecimal discount;
    private Integer ladderOther;

    private  List<SkuSaleAttrValueEntity> saleAttrs;

    private  List<String> images;

}
