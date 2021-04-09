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

        Task task = new Task(request.getTaskName(), request.getTaskDescription(), user, request.getStartDate(), request.getEndDate(), request.isIfDone(), priority);
        List<Tag> tags = request.getTags();

        taskRepository.save(task);

        for(Tag tag : tags){
            tag.setTaskId(task.getId_task());
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

        taskRepository.save(task);

    }

    public void changeTaskStatus(long id){

        Task task = taskRepository.getOne(id);

        task.setIfDone(!task.getIfDone());

        taskRepository.save(task);

    }

    private TaskPriority getPriority(String priorityName){
        System.out.println(priorityName);
        return switch (priorityName) {
            case "SMALL" -> TaskPriority.SMALL;
            case "HIGH" -> TaskPriority.HIGH;
            case "HIGHER" -> TaskPriority.HIGHER;
            case "CRITICAL" -> TaskPriority.CRITICAL;
            default -> TaskPriority.NORMAL;
        };

    }

    public List<String> getPriorities(){
        return Stream.of(TaskPriority.values()).map(TaskPriority::name).collect(Collectors.toList());
    }

    public List<Task> getUserTasks(String email){
        return taskRepository.findAllByUserEmail(email).get();
    }

}