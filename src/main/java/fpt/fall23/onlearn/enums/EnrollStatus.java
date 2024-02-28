package fpt.fall23.onlearn.enums;

import lombok.Getter;

@Getter
public enum EnrollStatus {
    PROCESSING("Đang học"),
    DONE("Đã xong"),
    PENDING("Đang chờ xét duyệt"),
    REMOVED("Đã xoá"),
    REFUNDED("Hoàn tiền");

    private final String description;

    EnrollStatus(String description) {
        this.description = description;
    }
}
