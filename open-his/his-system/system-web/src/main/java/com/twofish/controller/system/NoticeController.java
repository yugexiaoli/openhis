package com.twofish.controller.system;

import com.twofish.aspectj.annotation.Log;
import com.twofish.aspectj.enums.BusinessType;
import com.twofish.dto.NoticeDto;
import com.twofish.service.NoticeService;
import com.twofish.utils.ShiroSecurityUtils;
import com.twofish.vo.AjaxResult;
import com.twofish.vo.DataGridView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


/**
 *
 * 通知公告接口
 * @author ccy
 *
 */
@RestController
@Log4j2
@Api(value = "系统公告数据接口",tags = "公告数据接口")
@RequestMapping("/system/notice/")
public class NoticeController {

    @Resource
    private NoticeService noticeService;

    /**
     * 分页查询所有通知公告
     * @param noticeDto
     * @return
     */
    @GetMapping("listNoticeForPage")
    @ApiOperation(value = "分页查询所有通知公告",notes = "分页查询所有通知公告")
    public AjaxResult listNoticeForPage(NoticeDto noticeDto){
        DataGridView dataGridView = this.noticeService.listNoticeForPage(noticeDto);
        return AjaxResult.success("查询成功",dataGridView.getData(),dataGridView.getTotal());
    }

    /**
     * 添加公告数据
     * @param noticeDto
     * @return
     */
    @Log(title = "添加公告数据",businessType = BusinessType.INSERT)
    @PostMapping("addNotice")
    @ApiOperation(value = "添加公告数据",notes = "添加公告数据")
    public AjaxResult addNotice(@Validated NoticeDto noticeDto){
        //设置添加人
        noticeDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(this.noticeService.addNotice(noticeDto));
    }


    /**
     * 根据id查询公告数据
     * @param noticeId
     * @return
     */
    @GetMapping("getNoticeById/{noticeId}")
    @ApiOperation(value = "根据id查询公告数据",notes = "根据id查询公告数据")
    public AjaxResult getNoticeById(@PathVariable @Validated @NotNull(message = "公告id不能为空") Long noticeId){
        return AjaxResult.success("查询成功",this.noticeService.getNoticeById(noticeId));
    }

    /**
     * 更新公告数据
     * @param noticeDto
     * @return
     */
    @Log(title = "更新公告数据",businessType = BusinessType.UPDATE)
    @PutMapping("updateNotice")
    @ApiOperation(value = "更新公告数据数据",notes = "更新公告数据")
    public AjaxResult updateNotice(@Validated NoticeDto noticeDto){
        //设置修改人
        noticeDto.setSimpleUser(ShiroSecurityUtils.getCurrentSimpleUser());
        return AjaxResult.toAjax(this.noticeService.updateNotice(noticeDto));
    }


    /**
     * 删除公告数据(可批量删除)
     * @param noticeIds 公告数据id数组
     * @return
     */
    @Log(title = "删除公告数据",businessType = BusinessType.DELETE)
    @DeleteMapping("deleteNoticeByIds/{noticeIds}")
    @ApiOperation(value = "根据id删除公告数据",notes = "根据id删除公告数据")
    public AjaxResult deleteNoticeByIds(@PathVariable @Validated @NotEmpty(message = "公告id不能为空") Long[] noticeIds){
        return AjaxResult.toAjax(this.noticeService.deleteNoticeByIds(noticeIds));
    }



}
