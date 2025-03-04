package com.deal_stage_flow.demo.enums;

public enum PipelineStage {
//Dummy Sales pipeline stages
    SUCCESS,
    FAIL,
    ACKNOWLEDGE,

    // E-commerce Pipeline Stages
    CHECKOUT_ABANDONED,
    CHECKOUT_PENDING,
    CHECKOUT_COMPLETED,
    PROCESSED,
    SHIPPED,
    CANCELLED,

    // Sales Pipeline Stages
    APPOINTMENT_SCHEDULED,
    QUALIFIED_TO_BUY,
    PRESENTATION_SCHEDULED,
    DECISION_MAKER_BOUGHT_IN,
    CONTRACT_SENT,
    CLOSED_WON,
    CLOSED_LOST;


    //FOR new pipeline

}
