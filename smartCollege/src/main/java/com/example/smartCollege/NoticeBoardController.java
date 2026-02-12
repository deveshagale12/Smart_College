package com.example.smartCollege;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notices")
@CrossOrigin(origins = "*") // <--- Add this line to ALL Controllers
public class NoticeBoardController {

    @Autowired
    private NoticeBoardService noticeService;

    // 1. Post a new notice (Admin only logic)
    @PostMapping("/post")
    public ResponseEntity<NoticeBoard> postNotice(@RequestBody NoticeBoard notice) {
        return ResponseEntity.ok(noticeService.createNotice(notice));
    }

    // 2. View all notices
    @GetMapping("/all")
    public ResponseEntity<List<NoticeBoard>> getAllNotices() {
        return ResponseEntity.ok(noticeService.getAllNotices());
    }

    // 3. View notices by target (e.g., Faculty specific)
    @GetMapping("/target/{audience}")
    public ResponseEntity<List<NoticeBoard>> getNoticesByTarget(@PathVariable String audience) {
        return ResponseEntity.ok(noticeService.getNoticesByTarget(audience));
    }
}