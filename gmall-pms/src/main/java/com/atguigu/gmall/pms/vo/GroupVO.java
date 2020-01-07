package com.atguigu.gmall.pms.vo;

import com.atguigu.gmall.pms.entity.AttrAttrgroupRelationEntity;
import com.atguigu.gmall.pms.entity.AttrEntity;
import com.atguigu.gmall.pms.entity.AttrGroupEntity;
import lombok.Data;

import java.util.List;

/**
 * @author wxy
 * @create 2020-01-05 16:48
 */
@Data
public class GroupVO extends AttrGroupEntity {
   private List<AttrEntity> attrEntities;
   private  List<AttrAttrgroupRelationEntity> relations;
}
