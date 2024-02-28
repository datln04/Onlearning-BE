package fpt.fall23.onlearn.enums;

public enum TransactionType {

    ENROLLED("ENROLLED"),

    SERVICE_CHARGE("SERVICE_CHARGE"),
    REFUNDED("REFUNDED"),

    WITHDRAW("WITHDRAW");

    private final String description;

    TransactionType(String description) {
        this.description = description;
    }
}
