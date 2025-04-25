package tn.cloudnine.queute.utils;

import tn.cloudnine.queute.model.workspace.Meeting;

public interface IMeetingReminderScheduler {
    void scheduleMeetingReminder(Meeting meeting);
}
