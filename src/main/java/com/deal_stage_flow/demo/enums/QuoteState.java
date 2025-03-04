package com.deal_stage_flow.demo.enums;


import com.deal_stage_flow.demo.entity.Quote;

public enum QuoteState {

    DRAFT {
        @Override
        public void publish(Quote quote) {
            quote.reviewQuote();
            System.out.println("Publishing the quote...");
            quote.setCurrentState(PUBLISHED);
        }

        @Override
        public void archive(Quote quote) {
            System.out.println("Archiving  the draft quote...");
            quote.setCurrentState(ARCHIVED);

        }


    },
    PUBLISHED {
        @Override
        public void complete(Quote quote) {
            System.out.println("Completing the quote...");
            quote.setCurrentState(COMPLETED);
            quote.generateInvoice();
        }

        @Override
        public void expire(Quote quote) {
            System.out.println("Expiring the published quote...");
            quote.setCurrentState(EXPIRED);
        }

        @Override
        public void archive(Quote quote) {
            System.out.println("Archiving  the published quote...");
            quote.setCurrentState(ARCHIVED);
        }
    },
    COMPLETED {
        @Override
        public void archive(Quote quote) {
            System.out.println("Archiving the completed quote...");
            quote.setCurrentState(ARCHIVED);
        }
    },
    EXPIRED {
        @Override
        public void archive(Quote quote) {
            System.out.println("Archiving the expired quote...");
            quote.setCurrentState(ARCHIVED);
        }
    },
    ARCHIVED {
        @Override
        public void delete(Quote quote) {
            System.out.println("Deleting the archived quote...");
            quote.setCurrentState(DELETED);
        }
    },
    DELETED {
        @Override
        public void delete(Quote quote) {
            throw new IllegalStateException("Quote is already deleted.");
        }
    };

    public void publish(Quote quote) {
        throw new IllegalStateException("Cannot publish the quote in the current state.");
    }

    public void complete(Quote quote) {
        throw new IllegalStateException("Cannot complete the quote in the current state.");
    }

    public void expire(Quote quote) {
        throw new IllegalStateException("Cannot expire the quote in the current state.");
    }

    public void archive(Quote quote) {
        throw new IllegalStateException("Cannot archive the quote in the current state.");
    }

    public void delete(Quote quote) {
        throw new IllegalStateException("Cannot delete the quote in the current state.");
    }
}

