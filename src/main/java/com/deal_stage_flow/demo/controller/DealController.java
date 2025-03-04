package com.deal_stage_flow.demo.controller;
import com.deal_stage_flow.demo.entity.Deal;
import com.deal_stage_flow.demo.entity.Quote;
import com.deal_stage_flow.demo.enums.PipelineStage;
import com.deal_stage_flow.demo.service.DealService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import java.util.List;
import java.util.Optional;

@Controller
public class DealController {

    private final DealService dealService;

    public DealController(DealService dealService) {
        this.dealService = dealService;
    }

    @QueryMapping
    public List<Deal> getAllDeals() {
        return dealService.getAllDeals();
    }

    @MutationMapping
    public Deal createDeal(@Argument String dealName,
                           @Argument Double amount,
                           @Argument Long pipelineId,
                           @Argument String stage) {
        return dealService.createDeal(dealName, amount, pipelineId, stage);
    }

    @MutationMapping
    public Deal updateStage(@Argument Long id, @Argument String newStage) {
        return dealService.updateDealStage(id, newStage);
    }

    //Create or update Deal
    @MutationMapping
    public Deal createOrUpdateDeal(@Argument Long id, @Argument Optional<PipelineStage> stage,@Argument Long pipelineId , @Argument Optional<Double> amount,@Argument Optional<String> dealName) {

        return dealService.createOrUpdateDeal(id, stage,amount,pipelineId,dealName);
    }



}


