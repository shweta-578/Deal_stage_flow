package com.deal_stage_flow.demo.service;


import com.deal_stage_flow.demo.entity.Pipeline;
import com.deal_stage_flow.demo.enums.PipelineStage;
import com.deal_stage_flow.demo.enums.PipelineType;
import com.deal_stage_flow.demo.enums.RequestType;
import com.deal_stage_flow.demo.repo.PipelineRepo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.beans.Transient;
import java.util.List;
import java.util.Optional;

@Service
public class PipelineService {

    private final PipelineRepo pipelineRepository;

    public PipelineService(PipelineRepo pipelineRepository) {
        this.pipelineRepository = pipelineRepository;
    }

    // 🔹 Create Pipeline
    public Pipeline createPipeline(String name, PipelineType type, List<PipelineStage> stages) {
        Pipeline pipeline = new Pipeline(name, type, stages);
        return pipelineRepository.save(pipeline);
    }

    // 🔹 Get All Pipelines
    public List<Pipeline> getAllPipelines() {
        System.out.println("inside get all pipelines");
        return pipelineRepository.findAll();
    }

    // 🔹 Get Pipeline by ID
    public Optional<Pipeline> getPipelineById(Long id) {
        return pipelineRepository.findById(id);
    }

    // 🔹 Update Pipeline
    public Pipeline updatePipeline(Long id, Optional<String> name, Optional<PipelineType> type, Optional<List<PipelineStage>> stages) {
        return pipelineRepository.findById(id).map(pipeline -> {
            name.ifPresent(pipeline::setName);
            type.ifPresent(pipeline::setType);

            // If new stages are provided, merge them with existing ones
            if (stages.isPresent()) {
                List<PipelineStage> currentStages = pipeline.getStages();
                List<PipelineStage> newStages = stages.get();

                // Add only new stages that are not already in the pipeline
                for (PipelineStage stage : newStages) {
                    if (!currentStages.contains(stage)) {
                        currentStages.add(stage);
                    }
                }

                pipeline.setStages(currentStages);
            }

            return pipelineRepository.save(pipeline);
        }).orElseThrow(() -> new RuntimeException("Pipeline not found!"));
    }



    // 🔹 Delete Pipeline
    public void deletePipeline(Long id) {
        pipelineRepository.deleteById(id);
    }

    @Transactional
    public Boolean addOrRemovePipelineStage(Long pipelineId, RequestType requestType, PipelineStage pipelineStage) {
        Optional<Pipeline> optionalPipeline = pipelineRepository.findById(pipelineId);
        if (optionalPipeline.isPresent()) {
            Pipeline pipeline = optionalPipeline.get();
            List<PipelineStage> stages = pipeline.getStages();

            if (requestType == RequestType.ADD) {
                // Add stage if it doesn't already exist
                if (!stages.contains(pipelineStage)) {
                    stages.add(pipelineStage);
                }
            } else if (requestType == RequestType.REMOVE) {
                // Remove stage
                stages.remove(pipelineStage);
            }

            pipeline.setStages(stages);
            pipelineRepository.save(pipeline);
            return true;
        } else {
            throw new IllegalArgumentException("Pipeline with id " + pipelineId + " not found");
        }

    }
}

