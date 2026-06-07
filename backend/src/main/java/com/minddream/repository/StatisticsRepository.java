package com.minddream.repository;

import com.minddream.entity.Statistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 统计数据访问层
 */
@Repository
public interface StatisticsRepository extends JpaRepository<Statistics, Long> {

    /**
     * 根据统计类型查询
     */
    List<Statistics> findByStatType(String statType);

    /**
     * 根据类型和键查询
     */
    Statistics findByStatTypeAndStatKey(String statType, String statKey);
}
