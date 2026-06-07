package com.minddream.dto;

import lombok.Data;

/**
 * 搜索请求 DTO
 */
@Data
public class SearchRequest {

    private String gender; // 性别: M/F
    private String ageGroup; // 年龄组: A/C/O
    private String timePeriod; // 时间段
    private String keyword; // 关键词
    private Integer page = 0; // 页码（从0开始）
    private Integer size = 10; // 每页数量
    private String sortBy = "createdAt"; // 排序字段
    private String sortOrder = "desc"; // 排序方向: asc/desc
}
