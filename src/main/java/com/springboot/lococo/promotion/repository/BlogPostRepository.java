package com.springboot.lococo.promotion.repository;

import com.springboot.lococo.promotion.model.BlogPostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogPostRepository extends JpaRepository<BlogPostEntity, Long> {
    List<BlogPostEntity> findByProposalId(Long contentId);
}