package tn.cloudnine.queute.enums.workspace;

public enum TaskStatus {
    TO_DO("To do"),
    IN_PROGRESS("In progress"),
    BLOCKED("Blocked"),
    REWORK_NEEDED("Rework needed"),
    DONE("Done"),
    ARCHIVED("Archived");

    private final String name;

    TaskStatus(String name) { this.name = name; }
    public String getTaskStatus() { return this.name; }
}
