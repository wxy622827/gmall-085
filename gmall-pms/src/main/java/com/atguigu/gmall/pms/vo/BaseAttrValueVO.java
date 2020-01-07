package com.atguigu.gmall.pms.vo;

import com.atguigu.gmall.pms.entity.ProductAttrValueEntity;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

import java.util.List;

/**
 * @author wxy
 * @create 2020 -01-05 23:38
 */
@Data
public class BaseAttrValueVO extends ProductAttrValueEntity {


    public  void setValueSelected(List<String> valueSelected){
        if(!CollectionUtils.isEmpty(valueSelected)){
            this.setAttrValue(StringUtils.join(valueSelected,","));
        }else {
            this.setAttrValue(null);

        }
    }
}
