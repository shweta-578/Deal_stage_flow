package com.deal_stage_flow.demo.entity;

import com.deal_stage_flow.demo.enums.PipelineStage;
import com.deal_stage_flow.demo.enums.PipelineType;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "pipelines")
public class Pipeline {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private PipelineType type;

    @ElementCollection(targetClass = PipelineStage.class, fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "pipeline_stages", joinColumns = @JoinColumn(name = "pipeline_id"))
    @Column(name = "stage")
    private List<PipelineStage> stages;

    public Pipeline() {}

    public Pipeline(String name, PipelineType type, List<PipelineStage> stages) {
        this.name = name;
        this.type = type;
        this.stages = stages;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public PipelineType getType() {
        return type;
    }

    public List<PipelineStage> getStages() {
        return stages;
    }

    public void setStages(List<PipelineStage> stages) {
        this.stages = stages;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(PipelineType type) {
        this.type = type;
    }

    public Pipeline(Long id, String name, PipelineType type, List<PipelineStage> stages) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.stages = stages;
    }

    @Override
    public String toString() {
        return "Pipeline{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", stages=" + stages +
                '}';
    }
}

