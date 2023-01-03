package com.demo.code.util;

import com.crointech.integral.common.bean.Resp;
import com.crointech.integral.common.integralEnum.GlobalSystemEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.Collection;

/**
 * resp脱壳工具类
 */
@Slf4j
public class RespUtil {

    /**
     * 脱壳校验
     * @param resp 响应体
     * @return
     */
    public static boolean checkResp(Resp resp) {
        if (null == resp) {
            log.info("RespUtil-checkResp-resultIsNull");
            return Boolean.FALSE;
        }
        if (!GlobalSystemEnum.OK.getBusinessRespCode().equals(resp.getCode())) {
            log.info("RespUtil-checkResp-resultIsFail");
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    /**
     * 脱壳校验（数据data）
     * @param resp 响应体
     * @return
     */
    public static boolean checkDataResp(Resp resp) {
        if (null == resp) {
            log.info("RespUtil-checkResp-resultIsNull");
            return Boolean.FALSE;
        }
        if (!GlobalSystemEnum.OK.getBusinessRespCode().equals(resp.getCode())) {
            log.info("RespUtil-checkResp-resultIsFail");
            return Boolean.FALSE;
        }
        if (null == resp.getData()) {
            log.info("RespUtil-checkResp-DataIsNull");
            return Boolean.FALSE;
        }
        if (resp.getData() instanceof Collection && CollectionUtils.isEmpty((Collection) resp.getData())) {
            log.info("RespUtil-checkResp-resultIsEmpty");
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    /**
     * 脱壳校验并获取返回值
     * @param resp 响应体
     * @return
     */
    public static <T> T getData(Resp resp) {
        return (T)resp.getData();
    }
}
