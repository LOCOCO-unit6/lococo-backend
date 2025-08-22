package com.springboot.lococo.promotion.repository;

import com.springboot.lococo.promotion.model.InstagramPostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface InstagramPostRepository extends JpaRepository<InstagramPostEntity, Long> {
    List<InstagramPostEntity> findByContentId(Long contentId);
}