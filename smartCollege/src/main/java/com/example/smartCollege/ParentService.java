package com.example.smartCollege;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service 
public class ParentService {

    @Autowired
    private ParentRepository parentRepository;
    @Autowired
    private MarksRepository marksRepository;
    @Autowired
    private AttendanceRepository attendanceRepository;
    @Autowired
    private FeesRepository feesRepository;

    public Map<String, Object> getChildProgress(Long parentId) {
        Parent parent = parentRepository.findById(parentId).orElseThrow();
        Long studId = parent.getStudent().getStudId();

        Map<String, Object> progress = new HashMap<>();
        progress.put("marks", marksRepository.findByStudent_StudId(studId));
        progress.put("attendance", attendanceRepository.findByStudent_StudId(studId));
        
        return progress;
    }

    public Fees getChildFees(Long parentId) {
        Parent parent = parentRepository.findById(parentId).orElseThrow();
        return feesRepository.findByStudent_StudId(parent.getStudent().getStudId());
    }
}