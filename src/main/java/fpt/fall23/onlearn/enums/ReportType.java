package fpt.fall23.onlearn.enums;

public enum ReportType {
    STUDENT("STUDENT"),
    TEACHER("TEACHER");

    private final String description;

    ReportType(String description) {
        this.description = description;
    }
}
