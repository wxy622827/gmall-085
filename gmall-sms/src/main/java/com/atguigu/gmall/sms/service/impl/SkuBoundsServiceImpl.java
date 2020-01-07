package com.atguigu.gmall.sms.service.impl;

import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.Query;
import com.atguigu.core.bean.QueryCondition;
import com.atguigu.gmall.sms.SaleVO;
import com.atguigu.gmall.sms.dao.SkuBoundsDao;
import com.atguigu.gmall.sms.dao.SkuFullReductionDao;
import com.atguigu.gmall.sms.dao.SkuLadderDao;
import com.atguigu.gmall.sms.entity.SkuBoundsEntity;
import com.atguigu.gmall.sms.entity.SkuFullReductionEntity;
import com.atguigu.gmall.sms.entity.SkuLadderEntity;
import com.atguigu.gmall.sms.service.SkuBoundsService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


@Service("skuBoundsService")
public class SkuBoundsServiceImpl extends ServiceImpl<SkuBoundsDao, SkuBoundsEntity> implements SkuBoundsService {
@Resource
    private SkuLadderDao ladderDao;
@Resource
private SkuFullReductionDao reductionDao;
    @Override
    public PageVo queryPage(QueryCondition params) {
        IPage<SkuBoundsEntity> page = this.page(
                new Query<SkuBoundsEntity>().getPage(params),
                new QueryWrapper<SkuBoundsEntity>()
        );

        return new PageVo(page);
    }

    @Override
    public void saveSales(SaleVO saleVO) {
        SkuBoundsEntity skuBoundsEntity = new SkuBoundsEntity();
        //skuBounds积分
        BeanUtils.copyProperties(saleVO,skuBoundsEntity);
        List<String> works = saleVO.getWork();
        skuBoundsEntity.setWork(new Integer(works.get(0))+new Integer(works.get(1))*2+new Integer(works.get(2))*4+new Integer(works.get(3))*8);
        this.save(skuBoundsEntity);
        //skuLadder 打折
        SkuLadderEntity skuLadderEntity = new SkuLadderEntity();
        BeanUtils.copyProperties(saleVO,skuLadderEntity);
        skuLadderEntity.setAddOther(saleVO.getLadderOther());

        this.ladderDao.insert(skuLadderEntity);

        //FullReduction满减
        SkuFullReductionEntity reductionEntity = new SkuFullReductionEntity();
        BeanUtils.copyProperties(saleVO,reductionEntity);
        reductionEntity.setAddOther(saleVO.getFullAddOther());
        this.reductionDao.insert(reductionEntity);
    }

}