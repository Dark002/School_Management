package com.dark002.school_management.service;

import com.dark002.school_management.model.Teacher;
import com.dark002.school_management.model.Payout;
import com.dark002.school_management.repository.PayoutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PayoutService {
    @Autowired
    private PayoutRepository payouts;

    public List<Payout> getAllPayoutsByTeacher(Teacher teacher) {
        return payouts.getAllByTeacher(teacher.getId());
    }

    public void addPayout(Payout payout, Teacher teacher) {
        payout.setTeacherId(teacher.getId());
        payouts.addPayout(payout);
    }

    public Payout getPayoutById(String id) {
        return payouts.getById(Integer.parseInt(id));
    }

    public void deletePayout(Payout payout) {
        payouts.deletePayout(payout.getId());
    }
}
