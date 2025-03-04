package com.deal_stage_flow.demo.entity;


import com.deal_stage_flow.demo.enums.QuoteState;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "quotes")
public class Quote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private QuoteState currentState = QuoteState.DRAFT;

    @ElementCollection
    @CollectionTable(name = "quote_line_items", joinColumns = @JoinColumn(name = "quote_id"))
    @Column(name = "line_item")
    private List<String> lineItems = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deal_id")
    private Deal deal;

    public Quote() {
        this.currentState = QuoteState.DRAFT;
    }

    public void publish() {
        reviewQuote();
        currentState.publish(this);
        System.out.println(this.currentState);
    }

    public void complete() {
        System.out.println(this.currentState);
        currentState.complete(this);
    }

    public void archive() {
        currentState.archive(this);
    }

    public void delete() {
        currentState.delete(this);
    }

    public void expire() {
        currentState.expire(this);
    }

    public void generateInvoice() {
        System.out.println("Invoice generated successfully!");
    }

    public QuoteState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(QuoteState currentState) {
        this.currentState = currentState;
    }

    public List<String> getLineItems() {
        return lineItems;
    }

    public void setLineItems(List<String> lineItems) {
        this.lineItems = lineItems;
    }

    public Deal getDeal() {
        return deal;
    }

    public void setDeal(Deal deal) {
        this.deal = deal;
    }

    public void reviewQuote() {
        if (lineItems.isEmpty()) {
            throw new IllegalStateException("Line items cannot be empty");
        } else {
            System.out.println("Review successful");
        }
    }

    public String generateHtml() {
        return "<html><body><h1>Quote</h1><p>State: " + currentState + "</p><ul>" +
                lineItems.stream().map(item -> "<li>" + item + "</li>").reduce("", String::concat) +
                "</ul></body></html>";
    }

    public String generatePdf() {
        return "PDF representation of the quote in state: " + currentState + " with items: " + lineItems;
    }

    @Override
    public String toString() {
        return "Quote{" +
                "id=" + id +
                ", currentState=" + currentState +
                ", lineItems=" + lineItems +
                ", deal=" + (deal != null ? deal.getId() : null) +
                '}';
    }
}

