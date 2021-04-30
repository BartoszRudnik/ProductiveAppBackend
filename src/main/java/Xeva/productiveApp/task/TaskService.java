package Xeva.productiveApp.task;

import Xeva.productiveApp.appUser.AppUserService;
import Xeva.productiveApp.appUser.ApplicationUser;
import Xeva.productiveApp.tags.Tag;
import Xeva.productiveApp.tags.TagService;
import Xeva.productiveApp.task.requests.AddTaskRequest;
import Xeva.productiveApp.task.requests.GetTasksResponse;
import Xeva.productiveApp.task.requests.UpdateTaskPositionRequest;
import Xeva.productiveApp.task.requests.UpdateTaskRequest;
import Xeva.productiveApp.userRelation.RelationState;
import Xeva.productiveApp.userRelation.UserRelation;
import Xeva.productiveApp.userRelation.UserRelationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class TaskService {

    private final UserRelationService userRelationService;
    private final TaskRepository taskRepository;
    private final AppUserService userService;
    private final TagService tagService;

    private void saveTask(Task task){
        taskRepository.save(task);
        task.setPosition(task.getId_task() + 1000.0);
        taskRepository.save(task);
    }

    private void setTags(Task task, List<Tag> tags){
        for(Tag tag : tags){
            if(tag.getTaskId() == null) {
                tag.setId(null);
            }
            tag.setTaskId(task.getId_task());
            tag.setOwnerEmail(task.getUser().getEmail());
        }
        tagService.saveAll(tags);
    }

    public long addTask(AddTaskRequest request){

        boolean isUser = userService.findByEmail(request.getUserEmail()).isPresent();

        if(!isUser){
            throw new IllegalStateException("Wrong user");
        }

        Task task;
        ApplicationUser user = userService.findByEmail(request.getUserEmail()).get();
        TaskPriority priority = getPriority(request.getPriority());
        TaskLocalization localization = getLocalization(request.getLocalization());
        List<Tag> tags = request.getTags();

        if(localization == TaskLocalization.DELEGATED && request.getDelegatedEmail().length() > 1){

            ApplicationUser delegatedUser = userService.findByEmail(request.getDelegatedEmail()).get();

            task = new Task(request.getTaskName(), request.getTaskDescription(), user, localization, priority, request.isIfDone(), request.getStartDate(), request.getEndDate(), delegatedUser, request.getDelegatedEmail());

            this.saveTask(task);
            this.setTags(task, tags);

        }else{

            task = new Task(request.getTaskName(), request.getTaskDescription(), user, request.getStartDate(), request.getEndDate(), request.isIfDone(), priority, localization, request.getDelegatedEmail());
            this.saveTask(task);
            this.setTags(task, tags);

        }

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
            if(task.getParentTask() != null){

                ApplicationUser parentUser = userService.findByEmail(task.getParentTask().getUser().getEmail()).get();
                UserRelation relation = userRelationService.getUsersRelation(user, parentUser);

                if(relation != null && relation.getState() == RelationState.ACCEPTED){
                    tasksResponse.add(new GetTasksResponse(task, tagService.findAllByTaskId(task.getId_task())));
                }

            }else {
                tasksResponse.add(new GetTasksResponse(task, tagService.findAllByTaskId(task.getId_task())));
            }
        }

        return tasksResponse;

    }

    public void updateTaskPosition(UpdateTaskPositionRequest request, long id){
        Task task = taskRepository.findById(id).get();

        task.setPosition(request.getPosition());

        taskRepository.save(task);
    }

    public void updateTask(UpdateTaskRequest request, long id){

        Task task = taskRepository.findById(id).get();

        task.setDescription(request.getTaskDescription());
        task.setTask_name(request.getTaskName());
        task.setStartDate(request.getStartDate());
        task.setEndDate(request.getEndDate());
        task.setPriority(this.getPriority(request.getPriority()));
        task.setIfDone(request.isIfDone());
        task.setLocalization(this.getLocalization(request.getLocalization()));
        task.setPosition(request.getPosition());
        task.setDelegatedEmail(request.getDelegatedEmail());

        if(userService.findByEmail(request.getDelegatedEmail()).isPresent()) {

            ApplicationUser delegatedUser = userService.findByEmail(request.getDelegatedEmail()).get();
            Task childTask = new Task(request.getTaskName(), request.getTaskDescription(), delegatedUser, this.getPriority(request.getPriority()), request.isIfDone(), request.getStartDate(), request.getEndDate(), task);

            task.setChildTask(childTask);

            
        }

        List<Tag> tags = request.getTags();
        List<Tag> existingTags = tagService.findAllByTaskId(id);
        List<Tag> tagsToAdd = new ArrayList<>();

        for(Tag tag : tags){
            tag.setOwnerEmail(task.getUser().getEmail());
        }

        for(Tag tag : existingTags){
            if(!tags.contains(tag)){
                tagService.deleteById(tag.getId());
            }
        }

        for(Tag tag : tags){
            if(!existingTags.contains(tag)) {
                if (tag.getTaskId() == null) {
                    tag.setId(null);
                }

                tag.setTaskId(task.getId_task());

                tagsToAdd.add(tag);
            }
        }

        tagService.saveAll(tagsToAdd);

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
            case "LOW":
				return TaskPriority.LOW;
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
