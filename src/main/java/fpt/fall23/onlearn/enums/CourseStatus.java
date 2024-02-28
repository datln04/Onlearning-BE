package fpt.fall23.onlearn.enums;

public enum CourseStatus {
    ACTIVE("ACTIVE"),

    DEACTIVE("DEACTIVE"),

    PENDING("PENDING"),

    DRAFT("DRAFT"),

    REJECT("REJECT");

    private final String description;

    CourseStatus(String description) {
        this.description = description;
    }
}
