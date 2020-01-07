package com.atguigu.gmall.pms.feign;

import com.atguigu.gmall.sms.api.GmallSmsApi;
import org.springframework.cloud.openfeign.FeignClient;


/**
 * @author wxy
 * @create 2020-01-06 13:55
 */

@FeignClient("sms-service")
public interface GmallSmsClient extends GmallSmsApi {

  }
