package org.example.examsystem.controller;

import lombok.RequiredArgsConstructor;
import org.example.examsystem.mapper.AbnormalBehaviorMapper;
import org.example.examsystem.vo.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/abnormal")
@RequiredArgsConstructor

public class AbnormalController {
    private final AbnormalBehaviorMapper abnormalBehaviorMapper;

    @GetMapping("/get/behavior")
    public Result getAllAbnormalBehavior(@RequestParam Long examId){
        return Result.ok(abnormalBehaviorMapper.listAbnormalInfo(examId));
    }
}
