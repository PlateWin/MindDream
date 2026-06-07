package com.minddream.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 梦境实体类
 */
@Data
@Entity
@Table(name = "dreams")
public class Dream {
    
    @Id
    @Column(length = 50)
    private String id;
    
    @Column(length = 200)
    private String name;
    
    @Column(name = "dream_number")
    private Integer dreamNumber;
    
    @Column(name = "time_period", length = 50)
    private String timePeriod;
    
    @Column(name = "birth_year", length = 10)
    private String birthYear;
    
    @Column(length = 1)
    private String gender;
    
    @Column(name = "age_group", length = 1)
    private String ageGroup;
    
    @Column(columnDefinition = "TEXT")
    private String report;
    
    @Column(name = "characters_raw", length = 500)
    private String charactersRaw;
    
    @Column(name = "characters_cn", columnDefinition = "TEXT")
    private String charactersCn;
    
    @Column(name = "emotions_raw", length = 500)
    private String emotionsRaw;
    
    @Column(name = "emotions_cn", columnDefinition = "TEXT")
    private String emotionsCn;
    
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
