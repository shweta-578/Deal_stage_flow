package com.deal_stage_flow.demo.service;

import com.deal_stage_flow.demo.entity.Deal;
import com.deal_stage_flow.demo.entity.Quote;
import com.deal_stage_flow.demo.enums.QuoteState;
import com.deal_stage_flow.demo.repo.DealRepo;
import com.deal_stage_flow.demo.repo.QuoteRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuoteService {

    @Autowired
    private QuoteRepo quoteRepository;

    @Autowired
    private DealRepo dealRepository;

    public List<Quote> getAllQuotes() {
        return quoteRepository.findAll();
    }

    public Optional<Quote> getQuoteById(Long id) {
        return quoteRepository.findById(id);
    }

    public Quote createQuote(Long dealId, List<String> lineItems) {
        if (lineItems == null || lineItems.isEmpty()) {
            throw new IllegalArgumentException("Line items cannot be empty");
        }

        Quote quote = new Quote();
        quote.setLineItems(lineItems);

        if (dealId != null) {
            Optional<Deal> deal = dealRepository.findById(dealId);
            if (deal.isPresent()) {
                quote.setDeal(deal.get());
            } else {
                throw new IllegalArgumentException("Deal not found with id: " + dealId);
            }
        }

        return quoteRepository.save(quote);
    }

    public Quote updateQuote(Long id, List<String> lineItems, QuoteState state) {
        Optional<Quote> existingQuote = quoteRepository.findById(id);
        if (existingQuote.isPresent()) {
            Quote quote = existingQuote.get();
            if (lineItems != null) {
                quote.setLineItems(lineItems);
            }
            if (state != null) {
              updateQuoteState(id,state);
            }
            return quoteRepository.save(quote);
        } else {
            throw new IllegalArgumentException("Quote not found with id: " + id);
        }
    }

    public boolean deleteQuote(Long id) {
        Optional<Quote> existingQuote = quoteRepository.findById(id);
        if (existingQuote.isPresent()) {
            quoteRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Quote> getQuotesByDeal(Long dealId) {
        Optional<Deal> existingDeal = dealRepository.findById(dealId);
        if (existingDeal.isPresent())
            return existingDeal.get().getQuotes();
        else
            return new ArrayList<Quote>();
    }

    public Quote updateQuoteState(Long quoteId, QuoteState newState) {
        Optional<Quote> optionalQuote = quoteRepository.findById(quoteId);
        if (optionalQuote.isPresent()) {
            Quote quote = optionalQuote.get();
            QuoteState currentState = quote.getCurrentState();

            // Check if the transition is valid
            if (isValidTransition(currentState, newState)) {
                quote.setCurrentState(newState);
                quoteRepository.save(quote); // Save the updated quote
                return quote;
            } else {
                throw new IllegalStateException("Invalid state transition from " + currentState + " to " + newState);
            }
        } else {
            throw new IllegalArgumentException("Quote with id " + quoteId + " not found");
        }
    }

    private boolean isValidTransition(QuoteState currentState, QuoteState newState) {
        switch (currentState) {
            case DRAFT:
                return newState == QuoteState.PUBLISHED || newState == QuoteState.ARCHIVED;
            case PUBLISHED:
                return newState == QuoteState.COMPLETED || newState == QuoteState.EXPIRED || newState == QuoteState.ARCHIVED;
            case COMPLETED:
                return newState == QuoteState.ARCHIVED;
            case EXPIRED:
                return newState == QuoteState.ARCHIVED;
            case ARCHIVED:
                return newState == QuoteState.DELETED;
            case DELETED:
                return false; // No transitions allowed from DELETED state
            default:
                throw new IllegalArgumentException("Unknown state: " + currentState);
        }
    }

    public Boolean permanentlyDeleteQuote(Long id) {
        Optional<Quote> quote = quoteRepository.findById(id);
        if (quote.isPresent()) {
            quoteRepository.delete(quote.get());
            return true;
        } else{
            return false ;
        }

    }

    public Quote associateQuoteWithDeal(Long quoteId, Long dealId) {
        Optional<Deal> dealOptional  = dealRepository.findById(dealId);
        Optional<Quote> quoteOptional = quoteRepository.findById(quoteId);
        if(!dealOptional.isPresent() || !quoteOptional.isPresent()){
            throw new RuntimeException("Please provide valid quote or deal!");
        }
        Deal deal = dealOptional.get();
        Quote quote = quoteOptional.get();

        // Associate the Quote with the Deal
        quote.setDeal(deal);

        // Save the updated Quote entity
        return quoteRepository.save(quote);

    }
}