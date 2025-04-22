package tn.cloudnine.queute.enums;

public enum EmailTemplate {

    INVITE_MEETING("mails/invite_meet.html"),
    TASK_NOTIF("mails/notif_task_deadline.html")
    ;

    private final String name;
    EmailTemplate(String name) {
        this.name = name;
    }

    public String getTemplatePath() {
        return name;
    }
}
