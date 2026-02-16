
package com.smartCollege;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class AcademicService {
    @Autowired
    private AcademicRepository academicRepository;
    
    @Autowired
    private StudentRepository studentRepository;

    public AcademicRecord addRecord(Long studId, AcademicRecord record) {
        Student student = studentRepository.findById(studId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        
        record.setStudent(student);
        return academicRepository.save(record);
    }

    public List<AcademicRecord> getRecordsByStudent(Long studId) {
        return academicRepository.findByStudentStudId(studId);
    }
    
 // UPDATE: Edit existing year data
    public AcademicRecord updateRecord(Long recordId, AcademicRecord updatedData) {
        AcademicRecord existingRecord = academicRepository.findById(recordId)
                .orElseThrow(() -> new RuntimeException("Academic record not found with ID: " + recordId));

        existingRecord.setCourseName(updatedData.getCourseName());
        existingRecord.setAcademicYear(updatedData.getAcademicYear());
        existingRecord.setCurrentYear(updatedData.getCurrentYear());
        existingRecord.setStatus(updatedData.getStatus());

        return academicRepository.save(existingRecord);
    }

    // DELETE: Remove a year's record
    public void deleteRecord(Long recordId) {
        if (!academicRepository.existsById(recordId)) {
            throw new RuntimeException("Cannot delete. Record not found.");
        }
        academicRepository.deleteById(recordId);
    }
}