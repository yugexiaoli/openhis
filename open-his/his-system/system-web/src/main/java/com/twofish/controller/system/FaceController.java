package com.twofish.controller.system;

import com.alibaba.fastjson.JSON;
import com.twofish.service.FaceService;
import com.twofish.vo.AjaxResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 人脸识别
 */
@RestController
@Log4j2
@Api(value = "人脸识别接口",tags = "人脸识别接口")
@RequestMapping("/system/face")
public class FaceController {
    @Resource
    private FaceService faceService;

    /**
     * 人脸搜索
     * @param imgSrc
     * @return
     */
    @PostMapping("search")
    @ApiOperation(value = "人脸检测",tags = "人脸检测")
    public AjaxResult search(@RequestBody String imgSrc, HttpServletRequest request){
        //解决编码转义
        //反向解码得出username对应的二机制序列

        return AjaxResult.success(null, JSON.toJSONString(faceService.search(imgSrc)));
    }


}
