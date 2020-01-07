package com.atguigu.gmall.pms.service.impl;

import com.atguigu.core.bean.PageVo;
import com.atguigu.core.bean.Query;
import com.atguigu.core.bean.QueryCondition;
import com.atguigu.gmall.pms.dao.ProductAttrValueDao;
import com.atguigu.gmall.pms.dao.SkuInfoDao;
import com.atguigu.gmall.pms.dao.SpuInfoDao;
import com.atguigu.gmall.pms.dao.SpuInfoDescDao;
import com.atguigu.gmall.pms.entity.*;
import com.atguigu.gmall.pms.feign.GmallSmsClient;
import com.atguigu.gmall.pms.service.ProductAttrValueService;
import com.atguigu.gmall.pms.service.SkuImagesService;
import com.atguigu.gmall.pms.service.SkuSaleAttrValueService;
import com.atguigu.gmall.pms.service.SpuInfoService;
import com.atguigu.gmall.pms.vo.BaseAttrValueVO;
import com.atguigu.gmall.pms.vo.SkuInfoVO;
import com.atguigu.gmall.pms.vo.SpuInfoVO;
import com.atguigu.gmall.sms.SaleVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {

    @Resource
    private SpuInfoDescDao descDao;
    @Resource
    private ProductAttrValueService attrValueService;
    @Resource
    private ProductAttrValueDao attrValueDao;
    @Resource
    private SkuInfoDao skuInfoDao;
//    @Resource
//    private SkuImagesDao skuImagesDao;
    @Resource
    private SkuImagesService imagesService;
    @Resource
    private SkuSaleAttrValueService saleAttrValueService;
    @Resource
    private GmallSmsClient smsClient;
    @Override
    public PageVo queryPage(QueryCondition params) {
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                new QueryWrapper<SpuInfoEntity>()
        );

        return new PageVo(page);
    }

    @Override
    public PageVo querySpuByCidOrKey(QueryCondition condition, Long catId) {
        QueryWrapper<SpuInfoEntity> wrapper = new QueryWrapper<>();
        //判断查询全站还是本类
        if(catId!=0l) {
               wrapper.eq("catalog_id",catId);
        }
        //关键字查询
        String key = condition.getKey();
        if(StringUtils.isNotBlank(key)){
             wrapper.and(t->t.eq("id",key).or().like("spu_name",key));

        }

        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(condition),
                wrapper
        );

        return new PageVo(page);

    }

    @Override
    public void bigSave(SpuInfoVO spuInfoVO) {
        //保存spu相关信息
        //1.1 spuinfo
        spuInfoVO.setCreateTime(new Date());
        spuInfoVO.setUodateTime(spuInfoVO.getCreateTime());
        this.save(spuInfoVO);
        Long spuId = spuInfoVO.getId();
        //1.2 spuInfoDesc

        List<String> spuImages = spuInfoVO.getSpuImages();
        if(!CollectionUtils.isEmpty(spuImages)){
            SpuInfoDescEntity descEntity = new SpuInfoDescEntity();
            descEntity.setSpuId(spuId);
            descEntity.setDecript(StringUtils.join(spuImages,","));
            this.descDao.insert(descEntity);

        }

        //1.3 基础属性相关信息
        List<BaseAttrValueVO>  baseAttrs = spuInfoVO.getBaseAttrs();
        if(!CollectionUtils.isEmpty(baseAttrs)){
            List<ProductAttrValueEntity> attrValues = baseAttrs.stream().map(baseAttrValueVO -> {
                ProductAttrValueEntity attrValueEntity = new ProductAttrValueEntity();
                BeanUtils.copyProperties(baseAttrValueVO, attrValueEntity);
                attrValueEntity.setSpuId(spuId);
                attrValueEntity.setAttrSort(0);
                attrValueEntity.setQuickShow(0);
                return attrValueEntity;
            }).collect(Collectors.toList());
            this.attrValueService.saveBatch(attrValues);
        }

        //保存sku相关信息
        List<SkuInfoVO> skus = spuInfoVO.getSkus();
        if(CollectionUtils.isEmpty(skus)){
            return ;
        }
         //遍历skus
        skus.forEach(sku -> {

            SkuInfoEntity skuInfoEntity = new SkuInfoEntity();
            //从sku拷贝到skuInfoEntity
            BeanUtils.copyProperties(sku,skuInfoEntity);
            skuInfoEntity.setSpuId(spuId);
            List<String> images = sku.getImages();
            if(!CollectionUtils.isEmpty(images)){
                skuInfoEntity.setSkuDefaultImg(skuInfoEntity.getSkuDefaultImg()==null?images.get(0):skuInfoEntity.getSkuDefaultImg());
            }
            skuInfoEntity.setSkuCode(UUID.randomUUID().toString());
            skuInfoEntity.setCatalogId(spuInfoVO.getCatalogId());
            skuInfoEntity.setBrandId(spuInfoVO.getBrandId());
            this.skuInfoDao.insert(skuInfoEntity);
            Long skuId = skuInfoEntity.getSkuId();
            if(!CollectionUtils.isEmpty(images)){
                List<SkuImagesEntity> skuImagesEntities = images.stream().map(image -> {
                    SkuImagesEntity imagesEntity = new SkuImagesEntity();
                    imagesEntity.setSkuId(skuId);
                    imagesEntity.setImgUrl(image);
                    imagesEntity.setImgSort(0);
                    imagesEntity.setDefaultImg(StringUtils.equals(image, skuInfoEntity.getSkuDefaultImg()) ? 1 : 0);
                    return imagesEntity;
                }).collect(Collectors.toList());
                imagesService.saveBatch(skuImagesEntities);
            }

            List<SkuSaleAttrValueEntity> saleAttrs = sku.getSaleAttrs();
                    if(!CollectionUtils.isEmpty(saleAttrs)){
                        saleAttrs.forEach(skuSaleAttrValueEntity -> {
                            skuSaleAttrValueEntity.setSkuId(skuId);
                            skuSaleAttrValueEntity.setAttrSort(0);
                        });
                        saleAttrValueService.saveBatch(saleAttrs);
                    }

            //营销相关信息
            SaleVO saleVO = new SaleVO();
                    BeanUtils.copyProperties(sku,saleVO);
                    saleVO.setSkuId(skuId);
                    this.smsClient.saveSales(saleVO);
            //skuBounds
            //skuLadder
            //FullReduction
        });
        // skuInfo
        //skuIngoImages

        //skuSaleAttrValue
        



    }

}