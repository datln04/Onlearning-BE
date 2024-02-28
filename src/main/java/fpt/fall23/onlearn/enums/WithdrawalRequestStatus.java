package fpt.fall23.onlearn.enums;

public enum WithdrawalRequestStatus {
    PENDING("PENDING"),

    SUCCESS("SUCCESS"),

    REJECT("REJECT");
    private final String description;

    WithdrawalRequestStatus(String description) {
        this.description = description;
    }
}
