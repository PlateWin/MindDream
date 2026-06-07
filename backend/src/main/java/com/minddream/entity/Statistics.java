package com.minddream.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 统计数据实体类
 */
@Data
@Entity
@Table(name = "statistics")
public class Statistics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "stat_type", length = 50, nullable = false)
    private String statType;

    @Column(name = "stat_key", length = 100, nullable = false)
    private String statKey;

    @Column(name = "stat_value", length = 500)
    private String statValue;

    @Column(name = "count")
    private Integer count;

    @Column(name = "percentage", precision = 5, scale = 2)
    private BigDecimal percentage;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
