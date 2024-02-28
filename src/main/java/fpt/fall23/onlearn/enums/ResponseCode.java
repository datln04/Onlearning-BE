package fpt.fall23.onlearn.enums;

public enum ResponseCode {

    FAILED(1, "Failed request"),

    AUTH_ERROR_INVALID_USERNAME_OR_PASSWORD(102, "Invalid username or password"),

    AUTH_ERROR_NOT_PERMITTED(103, "Account is not allowed to access this system"),

    UNAUTHORIZED(401, "Unauthorized"),

    INTERNAL_SERVER_ERROR(500, "Internal server error"),


    // File
    FILE_ERROR_UPLOAD_FAILED (103, "Upload file failed"),


    // Account
    ACCOUNT_ERROR_EXIST_USER_NAME(201, "Existed username"),

    ACCOUNT_ERROR_EXIST_EMAIL(202, "Existed email"),

    ACCOUNT_ERROR_EXIST_CODE(203, "Existed code"),

    ACCOUNT_ERROR_NOT_FOUND(204, "Account not found"),

    // Major
    MAJOR_ERROR_NOT_FOUND(303, "Major error not found"),


    // Semester
    SEMESTER_ERROR_NOT_FOUND(401, "Semester not found"),

    SEMESTER_ERROR_INVALID_TIME(402, "Start time must be before end time"),

    SEMESTER_ERROR_EXIST_SEMESTER(403, "There is a semester exist in this time range"),

    SEMESTER_ERROR_EXIST_NAME(404, "Exist semester name"),

    // Subject
    SUBJECT_ERROR_NOT_FOUND (501, "Subject not found"),

    // Event
    EVENT_ERROR_END_TIME_LESS_THAN_START_TIME(601, "Event's end time must be later than event's start time"),

    EVENT_ERROR_EXPIRED_SEMESTER (602, "Semester is over"),

    EVENT_ERROR_INVALID_START_TIME (603, "Event's start time must be later than now"),

    EVENT_ERROR_INVALID_EVENT_TIME (604, "Event must be started and ended in the selected semester"),

    EVENT_ERROR_NOT_FOUND (605, "Event not found"),

    // Event registration
    EVENT_REGISTRATION_ERROR_NOT_FOUND(701, "Event registration not found"),

    EVENT_REGISTRATION_NOT_STUDENT(702, "Student did not registered this event"),

    EVENT_REGISTRATION_EVENT_STARTED(703, "Event started"),

    EVENT_REGISTRATION_EXIST_REGISTRATION(704, "Exist registration"),

    EVENT_REGISTRATION_STUDENT_NOT_ALLOWED(705, "Student is not allowed to register this event"),

    EVENT_REGISTRATION_ACTIVITY_NOT_FOUND(706, "Activity not found"),

    EVENT_REGISTRATION_ACTIVITY_COMPLETED(707, "Activity completed"),


;

    private final int code;
    private final String message;

    ResponseCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
