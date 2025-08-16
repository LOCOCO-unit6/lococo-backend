package com.springboot.lococo.organizermypage.repository;


import com.springboot.lococo.organizermypage.model.Proposal;
import com.springboot.lococo.organizermypage.model.ProposalSource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProposalRepository extends JpaRepository<Proposal, Long> {
    List<Proposal> findByAffiliationAndDeletedFalseOrderByCreatedAtDesc(String affiliation);
    List<Proposal> findByAffiliationAndSourceAndDeletedFalseOrderByCreatedAtDesc(String affiliation, ProposalSource source);
}