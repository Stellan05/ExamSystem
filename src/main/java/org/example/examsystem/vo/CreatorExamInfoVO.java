package org.example.examsystem.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 出题者查询考试信息VO
 */
@Data
public class CreatorExamInfoVO {
    private Long examId;
    private String examName;
    private String description;
    private Date startTime;
    private Date endTime;
    private String status;
    private Integer limitMinutes;
    private Integer showAnswer;
    private Date createTime;
    private Date updateTime;

    private List<QuestionDetailVO> questionDetailVOList;

    private List<UserSimpleInfoVO> userSimpleInfoVOList;
}
