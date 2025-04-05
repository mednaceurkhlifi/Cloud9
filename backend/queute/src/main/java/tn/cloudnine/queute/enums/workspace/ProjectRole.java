package tn.cloudnine.queute.enums.workspace;

public enum ProjectRole {
    MANAGER("Project manager"),
    TEAM_MEMBER("Team member");

    private final String name;

    ProjectRole(String name) { this.name = name; }
    public String getProjectRole() { return this.name; }
}
