package com.deal_stage_flow.demo.controller;

import com.deal_stage_flow.demo.entity.Pipeline;
import com.deal_stage_flow.demo.enums.PipelineStage;
import com.deal_stage_flow.demo.enums.PipelineType;
import com.deal_stage_flow.demo.enums.RequestType;
import com.deal_stage_flow.demo.service.PipelineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.Arguments;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

@Controller
public class PipelineController {

    private final PipelineService pipelineService;

    Logger logger = LoggerFactory.getLogger(PipelineController.class);

    public PipelineController(PipelineService pipelineService) {
        this.pipelineService = pipelineService;
    }

    // Create Pipeline
    @MutationMapping
    public Pipeline createPipeline(@Argument String name,
                                   @Argument PipelineType type,
                                   @Argument List<PipelineStage> stages) {
        return pipelineService.createPipeline(name, type, stages);
    }

    // Get All Pipelines
    @QueryMapping
    public List<Pipeline> getAllPipelines() {
        return pipelineService.getAllPipelines();
    }

    // Get Pipeline by ID
    @QueryMapping
    public Optional<Pipeline> getPipelineById(@Argument Long id) {
        return pipelineService.getPipelineById(id);
    }

    //Update Pipeline
    @MutationMapping
    public Pipeline updatePipeline(@Argument Long id,
                                   @Argument Optional<String> name,
                                   @Argument Optional<PipelineType> type,
                                   @Argument Optional<List<PipelineStage>> stages) {
        return pipelineService.updatePipeline(id, name, type, stages);
    }


    // 🔹 Delete Pipeline
    @MutationMapping
    public Boolean deletePipeline(@Argument Long id) {
        pipelineService.deletePipeline(id);
        return true;
    }
    @MutationMapping
    public ResponseEntity<String> addOrRemoveStagesFromPipeline(@Argument Long pipelineId , @Argument RequestType requestType,
                                                        @Argument PipelineStage pipelineStage){
        logger.info(":::::::::inside addOrRemoveStagesFromPipeline:::::::::::");

        Boolean result = pipelineService.addOrRemovePipelineStage(pipelineId,requestType,pipelineStage);
        return new ResponseEntity<>("Pipeline updated successfully!",HttpStatus.OK);
    }
}

