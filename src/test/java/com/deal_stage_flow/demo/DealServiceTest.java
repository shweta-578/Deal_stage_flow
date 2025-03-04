package com.deal_stage_flow.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.deal_stage_flow.demo.entity.Deal;
import com.deal_stage_flow.demo.entity.Pipeline;
import com.deal_stage_flow.demo.enums.PipelineStage;
import com.deal_stage_flow.demo.enums.PipelineType;
import com.deal_stage_flow.demo.repo.DealRepo;
import com.deal_stage_flow.demo.repo.PipelineRepo;
import com.deal_stage_flow.demo.service.DealService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@ExtendWith(MockitoExtension.class)
public class DealServiceTest {

    @InjectMocks
    private DealService dealService;

    @Mock
    private DealRepo dealRepository;

    @Mock
    private PipelineRepo pipelineRepository;

    private Deal dealWithPipeline;
    private Deal dealWithoutPipeline;
    private Pipeline pipeline;

    /**
     * The @BeforeEach annotation in JUnit 5 is used to mark a method that should be executed before each test case runs.
    This ensures that any setup required for the tests is performed before executing them.*/
    @BeforeEach
    public void initialSetUp() {
        pipeline = new Pipeline();
        pipeline.setId(1L);
        pipeline.setName("E-Commerce Pipeline");
        pipeline.setStages(Arrays.asList(
                PipelineStage.CHECKOUT_PENDING,
                PipelineStage.CHECKOUT_COMPLETED,
                PipelineStage.SHIPPED,
                PipelineStage.PROCESSED
        ));

        dealWithPipeline = new Deal(1l, "Deal12", 1000.0, pipeline, PipelineStage.CHECKOUT_PENDING);

        dealWithoutPipeline = new Deal(2l, "Deal13", 5000.0, null, PipelineStage.CHECKOUT_PENDING);

    }

    @Test
    public void testDealWithoutPipelineShouldFailed() {
        //mock repo to return deal without pipeline
        when(dealRepository.findById(2l)).thenReturn(Optional.of(dealWithoutPipeline));

        //
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
        {
            dealService.updateDealStage(2l, "CHECKOUT_PENDING");
        });
        assertEquals("Invalid stage transition. This stage is not part of the associated pipeline.", exception.getMessage());

        //verify that save was never called
       verify(dealRepository, never()).save(any());

    }

    @Test
    public void testValidDealStageTransition() {
        // Given: A deal in the CHECKOUT_PENDING stage
        Pipeline pipeline = new Pipeline("E-COMMERCE", PipelineType.ECOMMERCE,
                List.of(PipelineStage.CHECKOUT_PENDING, PipelineStage.CHECKOUT_COMPLETED));
        Deal deal = new Deal(1L, "D1", 100.0, pipeline, PipelineStage.CHECKOUT_PENDING);

        when(dealRepository.findById(1L)).thenReturn(Optional.of(deal));

        when(dealRepository.save(any(Deal.class))).thenReturn(deal);

        Deal updatedDeal = dealService.transitionDealStage(1L, PipelineStage.CHECKOUT_COMPLETED);
        // the deal stage should be updated
        assertEquals(PipelineStage.CHECKOUT_COMPLETED, updatedDeal.getStage());

    }

    @Test
    public void testDealAlreadyInTheTargetStage(){
        when(dealRepository.findById(1l)).thenReturn(Optional.of(dealWithPipeline));
        dealWithPipeline.setStage(PipelineStage.CHECKOUT_COMPLETED);

        dealService.transitionDealStage(1l,PipelineStage.CHECKOUT_COMPLETED);

        assertEquals(PipelineStage.CHECKOUT_COMPLETED ,dealWithPipeline.getStage());

        verify(dealRepository,never()).save(any()); /**Why Use It?
        In test cases where a deal should not be saved (because an operation is invalid or restricted),
         we use this to confirm that save() was not executed.**/

    }
}

