package fpt.fall23.onlearn.enums;

public enum TransactionStatus {

    REQUEST("REQUEST"),

    COMPLETED("COMPLETED"),

    DONE("DONE"),

    CANCEL("CANCEL");

    private final String description;

    TransactionStatus(String description) {
        this.description = description;
    }
}
