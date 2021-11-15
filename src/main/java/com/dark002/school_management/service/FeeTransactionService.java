package com.dark002.school_management.service;

import com.dark002.school_management.model.FeeTransaction;
import com.dark002.school_management.model.Classroom;
import com.dark002.school_management.model.Student;
import com.dark002.school_management.repository.FeeTransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class FeeTransactionService {
    @Autowired
    private FeeTransactionRepository feeTransactions;

    public List<FeeTransaction> getAllFeeTransactionsByStudentForClassroom(Student student, Classroom classroom) {
        return feeTransactions.getAllByStudentForClassroom(student.getRollNo(), classroom.getId());
    }

    public void addFeeTransaction(Student student, Classroom classroom, FeeTransaction feeTransaction) {
        feeTransaction.setClassroomId(classroom.getId());
        feeTransaction.setStudentRollNo(student.getRollNo());
        feeTransaction.setDate(new Date());
        feeTransactions.create(feeTransaction);
    }

    public void deleteFeeTransaction(String feeId) {
        feeTransactions.delete(Integer.parseInt(feeId));
    }
}
