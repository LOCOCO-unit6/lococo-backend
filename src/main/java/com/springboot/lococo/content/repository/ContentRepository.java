package com.springboot.lococo.content.repository;

import com.springboot.lococo.content.model.ContentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContentRepository extends JpaRepository<ContentEntity, Long> {

    List<ContentEntity> findByUserId(Long userId);
}
