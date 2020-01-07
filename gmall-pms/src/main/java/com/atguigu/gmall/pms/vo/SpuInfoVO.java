package com.atguigu.gmall.pms.vo;

import com.atguigu.gmall.pms.entity.SpuInfoEntity;
import lombok.Data;

import java.util.List;

/**
 * @author wxy
 * @create 2020-01-05 23:32
 */
@Data
public class SpuInfoVO extends SpuInfoEntity {
    private List<String> spuImages;
    //基本属性    prduct
    private List<BaseAttrValueVO> baseAttrs;//通用的规格

    private List<SkuInfoVO> skus;


}
