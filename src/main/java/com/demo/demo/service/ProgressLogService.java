package com.demo.demo.service;

import com.demo.demo.dto.ProgressLogRequest;
import com.demo.demo.entity.ProgressLogs.ProgressLog;
import com.demo.demo.repository.Progress_logs.ProgressLogRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ProgressLogService {
    @Autowired
    private ProgressLogRepository repository;

    public List<ProgressLog> getProgressLogsByUserId(Integer userId) {
        return repository.findByUserId(userId);
    }

    public ProgressLog saveLog(ProgressLog log) {
        return repository.save(log);
    }

//    public ProgressLogs deleteLog(ProgressLogs id){
//     return repository.deleteById(id);
//    }

    @Transactional

    public void deleteLog(Integer id) {
        repository.deleteById(id);
     }
}
