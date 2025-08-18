package com.springboot.lococo.mainpage.repository;

import com.springboot.lococo.mainpage.model.RecommendedCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RecommendedCourseRepository extends JpaRepository<RecommendedCourse, Long> {
    List<RecommendedCourse> findByCategoryOrderByLikeCountDesc(String category);
    List<RecommendedCourse> findByDestinationContaining(String destination);
    List<RecommendedCourse> findByDifficultyLessThanEqualOrderByViewCountDesc(Integer difficulty);
    List<RecommendedCourse> findTop10ByOrderByViewCountDesc();
}
