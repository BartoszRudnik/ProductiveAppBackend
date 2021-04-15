package Xeva.productiveApp.task;

import Xeva.productiveApp.appUser.AppUserService;
import Xeva.productiveApp.appUser.ApplicationUser;
import Xeva.productiveApp.tags.Tag;
import Xeva.productiveApp.tags.TagService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final AppUserService userService;
    private final TagService tagService;

    public long addTask(AddTaskRequest request){

        boolean isUser = userService.findByEmail(request.getUserEmail()).isPresent();

        if(!isUser){
            throw new IllegalStateException("Wrong user");
        }

        ApplicationUser user = userService.findByEmail(request.getUserEmail()).get();
        TaskPriority priority = getPriority(request.getPriority());
        TaskLocalization localization = getLocalization(request.getLocalization());

        Task task = new Task(request.getTaskName(), request.getTaskDescription(), user, request.getStartDate(), request.getEndDate(), request.isIfDone(), priority, localization);
        List<Tag> tags = request.getTags();

        taskRepository.save(task);

        for(Tag tag : tags){
            if(tag.getTaskId() == null) {
                tag.setId(null);
            }
            tag.setTaskId(task.getId_task());
            tag.setOwnerEmail(user.getEmail());
        }

        tagService.saveAll(tags);

        return task.getId_task();

    }

    public void deleteTask(long id){
        taskRepository.deleteById(id);
    }

    public List<GetTasksResponse> getTasks(String email){

        boolean isUser = userService.findByEmail(email).isPresent();

        if(!isUser){
            throw new IllegalStateException("Wrong user");
        }

        List<GetTasksResponse> tasksResponse = new ArrayList<>();
        ApplicationUser user = userService.findByEmail(email).get();

        List<Task> tasks = taskRepository.findAllByUserEmail(user.getEmail()).get();

        for(Task task : tasks){
            tasksResponse.add(new GetTasksResponse(task, tagService.findAllByTaskId(task.getId_task())));
        }

        return tasksResponse;

    }

    public void updateTask(AddTaskRequest request, long id){

        Task task = taskRepository.findById(id).get();

        task.setDescription(request.getTaskDescription());
        task.setTask_name(request.getTaskName());
        task.setStartDate(request.getStartDate());
        task.setEndDate(request.getEndDate());
        task.setPriority(getPriority(request.getPriority()));
        task.setIfDone(request.isIfDone());
        task.setLocalization(getLocalization(request.getLocalization()));

        taskRepository.save(task);

    }

    public void changeTaskStatus(long id){

        Task task = taskRepository.getOne(id);

        task.setIfDone(!task.getIfDone());

        taskRepository.save(task);

    }

    private TaskPriority getPriority(String priorityName){
        System.out.println(priorityName);
        switch (priorityName) {
            case "SMALL":
				return TaskPriority.SMALL;
            case "HIGH":
				return TaskPriority.HIGH;
            case "HIGHER":
				return TaskPriority.HIGHER;
            case "CRITICAL":
				return TaskPriority.CRITICAL;
        }
		return TaskPriority.NORMAL;
    }

    private TaskLocalization getLocalization(String localizationName){
        switch(localizationName){
            case "INBOX":
                return TaskLocalization.INBOX;
            case "SCHEDULED":
                return TaskLocalization.SCHEDULED;
            case "ANYTIME":
                return TaskLocalization.ANYTIME;
            case "TRASH":
                return TaskLocalization.TRASH;
            case "COMPLETED":
                return TaskLocalization.COMPLETED;
        }
        return TaskLocalization.DELEGATED;
    }

    public List<String> getPriorities(){
        return Stream.of(TaskPriority.values()).map(TaskPriority::name).collect(Collectors.toList());
    }

    public List<Task> getUserTasks(String email){
        return taskRepository.findAllByUserEmail(email).get();
    }

}
