package com.deal_stage_flow.demo.entity;

import com.deal_stage_flow.demo.enums.PipelineStage;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "deals")
public class Deal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String dealName;
    private Double amount;

    @CreationTimestamp
    private Date createdOn;

    @UpdateTimestamp
    private Date updatedOn;

    @ManyToOne
    @JoinColumn(name = "pipeline_id", nullable = true)
    private Pipeline pipeline;

    @Enumerated(EnumType.STRING)
    private PipelineStage stage;

    @OneToMany(mappedBy = "deal", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Quote> quotes;

    public Deal() {}

    public Deal(String dealName, Double amount, Pipeline pipeline, PipelineStage stage) {
        this.dealName = dealName;
        this.amount = amount;
        this.pipeline = pipeline;
        this.stage = stage;
    }


    public Long getId() {
        return id;
    }

    public String getDealName() {
        return dealName;
    }

    public Double getAmount() {
        return amount;
    }

    public Pipeline getPipeline() {
        return pipeline;
    }

    public PipelineStage getStage() {
        return stage;
    }



    public Deal(Long id, String dealName, Double amount,Pipeline pipeline, PipelineStage stage) {
        this.id = id;
        this.dealName = dealName;
        this.amount = amount;
        this.pipeline = pipeline;
        this.stage = stage;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDealName(String dealName) {
        this.dealName = dealName;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Date getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Date updatedOn) {
        this.updatedOn = updatedOn;
    }

    public void setPipeline(Pipeline pipeline) {
        this.pipeline = pipeline;
    }



    public void setStage(PipelineStage newStage) {
        if (this.pipeline != null && this.pipeline.getStages().contains(newStage)) {
            this.stage = newStage;
        } else {
            throw new IllegalArgumentException("Invalid stage transition. This stage is not part of the associated pipeline.");
        }
    }

    public List<Quote> getQuotes() {
        return quotes;
    }

    public void setQuotes(List<Quote> quotes) {
        this.quotes = quotes;
    }

    @Override
    public String toString() {
        return "Deal{" +
                "id=" + id +
                ", dealName='" + dealName + '\'' +
                ", amount=" + amount +
                ", createdOn=" + createdOn +
                ", updatedOn=" + updatedOn +
                ", pipeline=" + pipeline +
                ", stage=" + stage +
                ", quotes=" + quotes +
                '}';
    }


    //deal can have other things like line items , quotes ,customer
}


