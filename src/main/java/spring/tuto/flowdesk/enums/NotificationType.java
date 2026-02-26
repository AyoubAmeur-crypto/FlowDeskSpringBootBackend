package spring.tuto.flowdesk.enums;

public enum NotificationType {

    PROJECT_CREATED,
    PROJECT_ASSIGNED,
    PROJECT_STATUS_CHANGED,
    PROJECT_DEADLINE_REMINDER,
    PROJECT_OVERDUE,

    // Service
    SERVICE_REQUEST,
    SERVICE_APPROVED,
    SERVICE_COMPLETED,

    // Client
    NEW_CLIENT,
    CLIENT_MESSAGE,

    // Task
    TASK_ASSIGNED,
    TASK_DUE_REMINDER,
    TASK_COMPLETED,

    // Financial
    PAYMENT_RECEIVED,
    INVOICE_GENERATED,
    PAYMENT_OVERDUE,

    // System
    SYSTEM_ALERT,
    SYSTEM_UPDATE
}
