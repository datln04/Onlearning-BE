package fpt.fall23.onlearn.enums;

import lombok.Getter;

@Getter
public enum LessonType {
    VIDEO("Bài video"),
    READING("Bài đọc");
    private final String description;

    LessonType(String description) {
        this.description = description;
    }
}
