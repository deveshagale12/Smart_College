package com.example.smartCollege;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NoticeBoardService {

    @Autowired
    private NoticeBoardRepository noticeRepository;

    public NoticeBoard createNotice(NoticeBoard notice) {
        notice.setDate(LocalDate.now());
        return noticeRepository.save(notice);
    }

    public List<NoticeBoard> getAllNotices() {
        return noticeRepository.findAll();
    }

    public List<NoticeBoard> getNoticesByTarget(String audience) {
        return noticeRepository.findByTargetAudienceOrTargetAudience(audience, "ALL");
    }
}