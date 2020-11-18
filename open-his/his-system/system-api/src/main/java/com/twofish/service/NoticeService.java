package com.twofish.service;

import com.twofish.domain.Notice;
import com.twofish.domain.Notice;
import com.baomidou.mybatisplus.extension.service.IService;
import com.twofish.dto.NoticeDto;
import com.twofish.vo.DataGridView;

public interface NoticeService {
    /**
     * 分页查询所有公告
     * @param noticeDto
     * @return
     */
    DataGridView listNoticeForPage(NoticeDto noticeDto);

    /**
     * 添加公告
     * @param noticeDto
     * @return
     */
    int addNotice(NoticeDto noticeDto);

    /**
     * 根据id查询公告
     * @param noticeId
     * @return
     */
    Notice getNoticeById(Long noticeId);

    /**
     * 修改公告
     * @param noticeDto
     * @return
     */
    int updateNotice(NoticeDto noticeDto);

    /**
     * 删除公告(可批量)
     * @param noticeIds
     * @return
     */
    int deleteNoticeByIds(Long[] noticeIds);

}
