package com.deal_stage_flow.demo.controller;

import com.deal_stage_flow.demo.entity.Quote;
import com.deal_stage_flow.demo.enums.QuoteState;
import com.deal_stage_flow.demo.service.QuoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

@Controller
public class QuoteController {

    @Autowired
    private QuoteService quoteService;

    // Get all quotes
    @QueryMapping
    public List<Quote> getAllQuotes() {
        return quoteService.getAllQuotes();
    }

    // Get quote by ID
    @QueryMapping
    public Quote getQuoteById(@Argument Long id) {
        return quoteService.getQuoteById(id).get();
    }

    // Get quotes by deal ID
    @QueryMapping
    public List<Quote> getQuotesByDeal(@Argument Long dealId) {
        return quoteService.getQuotesByDeal(dealId);
    }

    // Create a new quote
    @MutationMapping
    public Quote createQuote(@Argument Optional<Long> dealId, @Argument List<String> lineItems) {
        Long deal = null;
        if(dealId.isPresent())
            deal = dealId.get() ;
        return quoteService.createQuote(deal, lineItems);
    }

    // Update an existing quote
    @MutationMapping
    public Quote updateQuote(@Argument Long id, @Argument List<String> lineItems, @Argument QuoteState currentState) {
        return quoteService.updateQuote(id, lineItems, currentState);
    }

    // Delete a quote (soft delete)
    @MutationMapping
    public Boolean deleteQuote(@Argument Long id) {

        return quoteService.deleteQuote(id);
    }

    // State transition mutations
    @MutationMapping
    public Quote publishQuote(@Argument Long id) {
        return quoteService.updateQuoteState(id, QuoteState.PUBLISHED);  //for publish a quote
    }

    @MutationMapping
    public Quote completeQuote(@Argument Long id) {

        return quoteService.updateQuoteState(id, QuoteState.COMPLETED);  //for complete quote
    }

    @MutationMapping
    public Quote archiveQuote(@Argument Long id) {

        return quoteService.updateQuoteState(id, QuoteState.ARCHIVED);
    }

    @MutationMapping
    public Boolean deleteQuotePermanently(@Argument Long id) {

        return quoteService.permanentlyDeleteQuote(id);
    }

    @MutationMapping
    public Quote expireQuote(@Argument Long id) {
        return quoteService.updateQuoteState(id, QuoteState.EXPIRED);
    }

    @MutationMapping
    public Quote associateQuoteWithDeal(
            @Argument Long quoteId,
            @Argument Long dealId) {
        return quoteService.associateQuoteWithDeal(quoteId, dealId);
    }
}

