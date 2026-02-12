package com.example.smartCollege;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoticeBoardRepository extends JpaRepository<NoticeBoard, Long> {

    // Finds notices meant for a specific group (e.g., "PARENTS") 
    // OR notices meant for "ALL"
    List<NoticeBoard> findByTargetAudienceOrTargetAudience(String audience, String general);
}