package fpt.fall23.onlearn.enums;

public enum PaymentHistoryType {

        DEPOSIT("DEPOSIT"),
        WITHDRAW("WITHDRAW");

    private final String description;

    PaymentHistoryType(String description) {
        this.description = description;
    }
}
