package com.atguigu.gmall.wms.dao;

import com.atguigu.gmall.wms.entity.WareInfoEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 仓库信息
 * 
 * @author wangxueyan
 * @email wxy@atguigu.com
 * @date 2020-01-03 13:49:12
 */
@Mapper
public interface WareInfoDao extends BaseMapper<WareInfoEntity> {
	
}
