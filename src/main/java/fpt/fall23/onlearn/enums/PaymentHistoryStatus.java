package fpt.fall23.onlearn.enums;

public enum PaymentHistoryStatus {

    CREATED("CREATED"),
    COMPLETED("COMPLETED"),
    REFUNDED("REFUNDED"),

    PENDING("PENDING"),

    PENDING_PAYOUT("PENDING_PAYOUT"),

    COMPLETED_PAYOUT("COMPLETED_PAYOUT"),
    PAYOUT("PAYOUT"),

    CANCEL("CANCEL");

    private final String description;

    PaymentHistoryStatus(String description) {
        this.description = description;
    }

}
