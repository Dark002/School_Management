package com.dark002.school_management.service;

import com.dark002.school_management.model.Result;
import com.dark002.school_management.repository.ResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ResultService {
    @Autowired
    private ResultRepository results;

    public void createResult(Result result, String registrationId) {
        result.setSubjectRegistrationId(Integer.parseInt(registrationId));
        results.create(result);
    }
}
