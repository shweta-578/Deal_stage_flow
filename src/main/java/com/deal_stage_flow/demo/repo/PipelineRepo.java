package com.deal_stage_flow.demo.repo;

import com.deal_stage_flow.demo.entity.Pipeline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PipelineRepo extends JpaRepository<Pipeline, Long> {

}
