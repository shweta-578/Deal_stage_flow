package com.deal_stage_flow.demo;
import com.deal_stage_flow.demo.entity.Pipeline;
import com.deal_stage_flow.demo.enums.PipelineStage;
import com.deal_stage_flow.demo.enums.PipelineType;
import com.deal_stage_flow.demo.repo.PipelineRepo;
import com.deal_stage_flow.demo.service.PipelineService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.verify;
import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class PipelineInitialTest {

    @Mock
    private PipelineRepo pipelineRepo ;

    @InjectMocks
    private PipelineService pipelineService;

    @Test
    void getAllPipeLines(){
        Pipeline pipeline1 = new Pipeline(1l,"Dummy1",PipelineType.SALES, Arrays.asList(PipelineStage.SUCCESS,PipelineStage.CANCELLED));
        Pipeline pipeline2 = new Pipeline(2l,"Dummy2",PipelineType.ECOMMERCE, Arrays.asList(PipelineStage.SUCCESS,PipelineStage.CANCELLED));

        given(pipelineRepo.findAll()).willReturn(List.of(pipeline1,pipeline2));

        var pipelines = pipelineService.getAllPipelines() ;
        System.out.println(pipelines);
        assertThat(pipelines).isNotNull();
        assertThat(pipelines.size()).isEqualTo(2);


    }



    /*@Test
    public void testSalesDeal(){
        Deal deal = new Deal(1l,"New Deal",50000.00, PipelineType.SALES,"SUCCESS");
        assertEquals(PipelineType.SALES ,deal.getPipelineType(),"Pipeline should be SALES");
        assertEquals("APPOINTMENT_SCHEDULED", deal.getStage(), "Stage should match the initial value.");

    }
    @Test
    void testEcommerceDeal() {
        Deal deal = new Deal(2L, "Laptop Order Deal", 52200.0, PipelineType.ECOMMERCE, "CHECKOUT_PENDING");
        assertEquals(PipelineType.ECOMMERCE, deal.getPipelineType(), "Pipeline should be ECOMMERCE.");
        assertEquals("CHECKOUT_PENDING", deal.getStage(), "Stage should match the initial value.");
    }*/

}
