package com.deal_stage_flow.demo.repo;

import com.deal_stage_flow.demo.entity.Quote;
import jdk.jfr.Registered;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuoteRepo extends JpaRepository<Quote,Long> {
}
