package com.deal_stage_flow.demo.service;

import com.deal_stage_flow.demo.entity.Deal;
import com.deal_stage_flow.demo.entity.Pipeline;
import com.deal_stage_flow.demo.entity.Quote;
import com.deal_stage_flow.demo.enums.PipelineStage;
import com.deal_stage_flow.demo.repo.DealRepo;
import com.deal_stage_flow.demo.repo.PipelineRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DealService {

    private final DealRepo dealRepository;
    private final PipelineRepo pipelineRepository;

    public DealService(DealRepo dealRepository, PipelineRepo pipelineRepository) {
        this.dealRepository = dealRepository;
        this.pipelineRepository = pipelineRepository;
    }

    public List<Deal> getAllDeals() {
        return dealRepository.findAll();
    }

    public Deal createDeal(String name, Double amount, Long pipelineId, String stage) {
        if (pipelineId == null) {
            throw new IllegalArgumentException("A pipeline must be provided to create a deal.");
        }

        Pipeline pipeline = pipelineRepository.findById(pipelineId)
                .orElseThrow(() -> new RuntimeException("Pipeline not found"));

        // Set default stage if not provided
        String initialStage = (stage != null) ? stage : String.valueOf(pipeline.getStages().get(0));

        Deal deal = new Deal();
        deal.setDealName(name);
        deal.setAmount(amount);
        deal.setPipeline(pipeline);
        deal.setStage(PipelineStage.valueOf(initialStage));

        return dealRepository.save(deal);
    }

//need to modify this
    public Deal updateDealStage(Long id, String newStage) {
        Optional<Deal> dealOptional = dealRepository.findById(id);

        if (dealOptional.isPresent()) {
            Deal deal = dealOptional.get();
            PipelineStage newPipelineStage = PipelineStage.valueOf(newStage);

            // Validate if the new stage belongs to the pipeline
            if (deal.getPipeline() != null && !deal.getPipeline().getStages().contains(newPipelineStage)) {
                throw new IllegalArgumentException("Invalid stage transition for this pipeline.");
            }

            deal.setStage(newPipelineStage);
            return dealRepository.save(deal);
        }
        throw new RuntimeException("Deal not found");
    }

    public Deal transitionDealStage(Long dealId, PipelineStage newStage) {
        Deal deal = dealRepository.findById(dealId)
                .orElseThrow(() -> new IllegalArgumentException("Deal not found"));

        //Do nothing if already in the target stage
        if (deal.getStage() == newStage) {
            return deal; // No update needed
        }

        deal.setStage(newStage);
       return dealRepository.save(deal); // Save only when the stage is updated
    }


    public Deal createOrUpdateDeal(Long id, Optional<PipelineStage> stage, Optional<Double> amount ,Long pipelineId,Optional<String> dealName) {
        Deal deal;
        Optional<Pipeline> pipeline = pipelineRepository.findById(pipelineId);
        if(!pipeline.isPresent()){
            throw new IllegalArgumentException("Pipeline not present with pipelineId : "+pipelineId);
        }

        if (id != null) {
            // Edit existing deal if present
            Optional<Deal> optionalDeal = dealRepository.findById(id);
            if (optionalDeal.isPresent()) {
                deal = optionalDeal.get();
                stage.ifPresent(deal::setStage);
                amount.ifPresent(deal::setAmount);
                deal.setPipeline(pipeline.get());
                dealName.ifPresent(deal::setDealName);
            } else {
                throw new IllegalArgumentException("Deal with id " + id + " not found");
            }
        } else {
            // Create new deal
            if (stage.isEmpty() || amount.isEmpty()) {
                throw new IllegalArgumentException("Stage and amount are required for creating a new deal");
            }

            deal = new Deal();
            deal.setPipeline(pipeline.get());
            deal.setStage(stage.get());
            deal.setAmount(amount.get());
            deal.setDealName(dealName.get());
        }
        return dealRepository.save(deal);
    }
}
