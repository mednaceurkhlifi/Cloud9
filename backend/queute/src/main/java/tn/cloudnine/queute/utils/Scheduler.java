package tn.cloudnine.queute.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tn.cloudnine.queute.model.user.User;
import tn.cloudnine.queute.model.workspace.Task;
import tn.cloudnine.queute.repository.workspace.TaskRepository;
import tn.cloudnine.queute.service.workspace.ITaskService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import static tn.cloudnine.queute.enums.workspace.TaskStatus.DONE;

@Service
@RequiredArgsConstructor
@Component
public class Scheduler {

    private final TaskRepository repository;
    private final IEmailService emailService;

    @Transactional(readOnly = true)
    @Scheduled(cron = "0 0 8 * * *")
    public void notifyUsersForTaskDeadline() {
        LocalDateTime startOfTomorrow = LocalDate.now().plusDays(1).atStartOfDay();
        LocalDateTime endOfTomorrow = startOfTomorrow.plusDays(1);
        Set<Task> tasks = repository.findTasksWithDeadline(startOfTomorrow, endOfTomorrow, DONE);
//        System.out.println("tasks : " + tasks);
        String task_link;
        String task_title;
        for (Task task : tasks) {
            task_title = task.getTitle();
            task_link = "http://localhost:4200/workspace/task/" + task.getTaskId();
            System.out.println(task.getFormattedDateTime());
            for (User member : task.getMembers()) {
                emailService.sendTaskDeadlineNotif(
                        member.getEmail(), member.getFull_name(), task_title, task.getFormattedDateTime(), task_link
                );
            }
        }
    }
}
