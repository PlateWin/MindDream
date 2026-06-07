package com.minddream.repository;

import com.minddream.entity.Dream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 梦境数据访问层
 */
@Repository
public interface DreamRepository extends JpaRepository<Dream, String> {

    /**
     * 根据性别分页查询
     */
    Page<Dream> findByGender(String gender, Pageable pageable);

    /**
     * 根据年龄组分页查询
     */
    Page<Dream> findByAgeGroup(String ageGroup, Pageable pageable);

    /**
     * 多条件搜索
     */
    @Query("SELECT d FROM Dream d WHERE " +
            "(:gender IS NULL OR d.gender = :gender) AND " +
            "(:ageGroup IS NULL OR d.ageGroup = :ageGroup) AND " +
            "(:timePeriod IS NULL OR d.timePeriod LIKE %:timePeriod%) AND " +
            "(:keyword IS NULL OR d.report LIKE %:keyword%)")
    Page<Dream> searchDreams(@Param("gender") String gender,
            @Param("ageGroup") String ageGroup,
            @Param("timePeriod") String timePeriod,
            @Param("keyword") String keyword,
            Pageable pageable);

    /**
     * 统计性别分布
     */
    @Query("SELECT d.gender, COUNT(d) FROM Dream d GROUP BY d.gender")
    List<Object[]> countByGender();

    /**
     * 统计年龄分布
     */
    @Query("SELECT d.ageGroup, COUNT(d) FROM Dream d GROUP BY d.ageGroup")
    List<Object[]> countByAgeGroup();

    /**
     * 获取随机梦境
     */
    @Query(value = "SELECT * FROM dreams ORDER BY RAND() LIMIT :limit", nativeQuery = true)
    List<Dream> findRandomDreams(@Param("limit") int limit);
}
