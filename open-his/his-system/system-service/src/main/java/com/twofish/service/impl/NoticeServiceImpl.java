package com.twofish.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.twofish.domain.Notice;
import com.twofish.dto.NoticeDto;
import com.twofish.mapper.NoticeMapper;
import com.twofish.service.NoticeService;
import com.twofish.vo.DataGridView;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
@Service
public class NoticeServiceImpl implements NoticeService{
    @Resource
    private NoticeMapper noticeMapper;

    @Override
    public DataGridView listNoticeForPage(NoticeDto noticeDto) {
        Page<Notice> page = new Page<>(noticeDto.getPageNum(),noticeDto.getPageSize());
        QueryWrapper<Notice> qw = new QueryWrapper<>();
        qw.like(StringUtils.isNotBlank(noticeDto.getNoticeTitle()),Notice.COL_NOTICE_TITLE,noticeDto.getNoticeTitle());
        qw.like(StringUtils.isNotBlank(noticeDto.getCreateBy()),Notice.COL_CREATE_BY,noticeDto.getCreateBy());
        qw.eq(null!=noticeDto.getNoticeType(),Notice.COL_NOTICE_TYPE,noticeDto.getNoticeType());
        qw.orderByDesc(Notice.COL_CREATE_TIME);
        noticeMapper.selectPage(page,qw);
        return new DataGridView(page.getTotal(),page.getRecords());
    }

    @Override
    public int addNotice(NoticeDto noticeDto) {
        Notice notice = new Notice();
        BeanUtil.copyProperties(noticeDto,notice);
        //设置创建人
        notice.setCreateBy(noticeDto.getSimpleUser().getUserName());
        return noticeMapper.insert(notice);
    }

    @Override
    public Notice getNoticeById(Long noticeId) {
        return this.noticeMapper.selectById(noticeId);
    }

    @Override
    public int updateNotice(NoticeDto noticeDto) {
        Notice notice = new Notice();
        BeanUtil.copyProperties(noticeDto,notice);
        //设置修改人
        notice.setUpdateBy(noticeDto.getSimpleUser().getUserName());
        return noticeMapper.updateById(notice);
    }

    @Override
    public int deleteNoticeByIds(Long[] noticeIds) {
        List<Long> ids = Arrays.asList(noticeIds);
        if(ids!=null && ids.size()>0){
            return this.noticeMapper.deleteBatchIds(ids);
        }
        return -1;
    }




}
