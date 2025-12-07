package org.example.examsystem.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.RequiredArgsConstructor;
import org.example.examsystem.vo.PageResult;
import org.example.examsystem.service.IService.IWrongQuestionService;
import org.example.examsystem.vo.WrongQuestionVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wrong-question")
@RequiredArgsConstructor
public class WrongQuestionController {

    private final IWrongQuestionService wrongQuestionService;

    @GetMapping("/byType")
    public PageResult<WrongQuestionVO> getWrongQuestionsByType(@RequestParam Integer type,
                                                               @RequestParam(defaultValue = "1") int page,
                                                               @RequestParam(defaultValue = "10") int size) {
        IPage<WrongQuestionVO> resultPage = wrongQuestionService.getWrongQuestionsByType(type,page,size);
        return new PageResult<>(
                resultPage.getRecords(),
                resultPage.getTotal(),
                resultPage.getSize(),
                resultPage.getCurrent()
        );
    }
}
