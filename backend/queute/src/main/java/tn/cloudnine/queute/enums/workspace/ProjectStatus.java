package tn.cloudnine.queute.enums.workspace;

public enum ProjectStatus {
    NOT_STARTED("Not started"),
    IN_PROGRESS("In progress"),
    ON_HOLD("On hold"),
    CANCELED("Canceled"),
    UNDER_REVIEW("Under review"),
    FINISHED("Finished");

    private final String name;

    ProjectStatus(String name) { this.name = name; }
    public String getProjectStatus() { return this.name; }
}
