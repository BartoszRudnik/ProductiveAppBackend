package Xeva.productiveApp.task;

import Xeva.productiveApp.appUser.AppUserService;
import Xeva.productiveApp.appUser.ApplicationUser;
import Xeva.productiveApp.localization.Localization;
import Xeva.productiveApp.localization.LocalizationService;
import Xeva.productiveApp.localization.dto.AddLocalization;
import Xeva.productiveApp.tags.Tag;
import Xeva.productiveApp.tags.TagService;
import Xeva.productiveApp.task.dto.*;
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
    private final LocalizationService localizationService;

    public GetTasksResponse getSingleTaskFull(String mail, Long taskId){

        boolean isUser = userService.findByEmail(mail).isPresent();

        if(!isUser){
            throw new IllegalStateException("Wrong user");
        }

        ApplicationUser user = userService.findByEmail(mail).get();
        Task task = new Task();
        if(taskRepository.findById(taskId).isPresent()) {
            task = taskRepository.findById(taskId).get();
        }

        GetTasksResponse taskResponse = new GetTasksResponse();

        if(task.getParentTask() != null){

            ApplicationUser parentUser = userService.findByEmail(task.getParentTask().getUser().getEmail()).get();
            UserRelation relation = userRelationService.getUsersRelation(user, parentUser);

            if(relation != null && relation.getState() == RelationState.ACCEPTED){
                this.setParentTaskInformation(task, task.getParentTask());
                this.taskRepository.save(task);

                taskResponse = new GetTasksResponse(task, tagService.findAllByTaskId(task.getId()), task.getParentTask().getUser().getEmail(), null, task.getParentTask().getId());
            }

        }else if(task.getChildTask() != null){
            taskResponse = new GetTasksResponse(task, tagService.findAllByTaskId(task.getId()), null, task.getChildTask().getId(), null);
        }
        else {
            taskResponse = new GetTasksResponse(task, tagService.findAllByTaskId(task.getId()));
        }

        return taskResponse;
    }

    public GetSingleTaskResponse getSingleTask(String mail, Long taskId){

        boolean isUser = userService.findByEmail(mail).isPresent();

        if(!isUser){
            throw new IllegalStateException("Wrong user");
        }

        Task task = taskRepository.getOne(taskId);

        GetSingleTaskResponse result = new GetSingleTaskResponse();

        result.setDescription(task.getDescription());
        result.setTaskId(task.getId());
        result.setEndDate(task.getEndDate());
        result.setStartDate(task.getStartDate());
        result.setTaskName(task.getTaskName());
        result.setPriority(task.getPriority().toString());
        result.setOwnerEmail(task.getUser().getEmail());

        return result;

    }

    private void saveTask(Task task){
        taskRepository.save(task);
        task.setPosition(task.getId() + 1000.0);
        taskRepository.save(task);
    }

    private void setTags(Task task, List<Tag> tags){
        for(Tag tag : tags){
            if(tag.getTaskId() == null) {
                tag.setId(null);
            }
            tag.setTaskId(task.getId());
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
        TaskList localization = getLocalization(request.getLocalization());
        List<Tag> tags = request.getTags();

        if(localization == TaskList.DELEGATED && request.getDelegatedEmail() != null && request.getDelegatedEmail().length() > 1){

            ApplicationUser delegatedUser = userService.findByEmail(request.getDelegatedEmail()).get();

            if(request.getLocalizationId() == null) {
                task = new Task(request.getTaskName(), request.getTaskDescription(), user, localization, priority, request.isIfDone(), request.getStartDate(), request.getEndDate(), delegatedUser, request.getDelegatedEmail());
            }else{
                Localization notificationLocalization = this.localizationService.findById(request.getLocalizationId()).get();
                task = new Task(request.getTaskName(), request.getTaskDescription(), user, localization, priority, request.isIfDone(), request.getStartDate(), request.getEndDate(), delegatedUser, request.getDelegatedEmail(), notificationLocalization, request.getLocalizationRadius(), request.isNotificationOnEnter(), request.isNotificationOnExit());
            }

        }else{

            if(request.getLocalizationId() == null) {
                task = new Task(request.getTaskName(), request.getTaskDescription(), user, request.getStartDate(), request.getEndDate(), request.isIfDone(), priority, localization, request.getDelegatedEmail());
            }else{
                Localization notificationLocalization = this.localizationService.findById(request.getLocalizationId()).get();
                task = new Task(request.getTaskName(), request.getTaskDescription(), user, request.getStartDate(), request.getEndDate(), request.isIfDone(), priority, localization, request.getDelegatedEmail(), notificationLocalization, request.isNotificationOnEnter(), request.getLocalizationRadius(), request.isNotificationOnExit());
            }

        }
        this.saveTask(task);
        this.setTags(task, tags);

        return task.getId();

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
                    this.setParentTaskInformation(task, task.getParentTask());
                    this.taskRepository.save(task);

                    tasksResponse.add(new GetTasksResponse(task, tagService.findAllByTaskId(task.getId()), task.getParentTask().getUser().getEmail(), null, task.getParentTask().getId()));
                }

            }else if(task.getChildTask() != null){
                tasksResponse.add(new GetTasksResponse(task, tagService.findAllByTaskId(task.getId()), null, task.getChildTask().getId(), null));
            }
            else {
                tasksResponse.add(new GetTasksResponse(task, tagService.findAllByTaskId(task.getId())));
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
        task.setTaskName(request.getTaskName());
        task.setStartDate(request.getStartDate());
        task.setEndDate(request.getEndDate());
        task.setPriority(this.getPriority(request.getPriority()));
        task.setIfDone(request.isIfDone());
        task.setTaskList(this.getLocalization(request.getLocalization()));
        task.setPosition(request.getPosition());
        task.setIsCanceled(request.isCanceled());
        if(!this.localizationService.findById(request.getLocalizationId()).isPresent()){
            task.setNotificationLocalization(null);
        }else {
            task.setNotificationLocalization(this.localizationService.findById(request.getLocalizationId()).get());
        }
        task.setLocalizationRadius(request.getLocalizationRadius());
        task.setNotificationOnEnter(request.isNotificationOnEnter());
        task.setNotificationOnExit(request.isNotificationOnExit());

        if(task.getChildTask() != null && (request.getLocalization().equals("TRASH") || request.getLocalization().equals("COMPLETED"))){
            if(!task.getChildTask().getIfDone()) {
                task.getChildTask().setIsCanceled(true);
            }
            task.getChildTask().setParentTask(null);

            task.setChildTask(null);
        }
        else if(userService.findByEmail(request.getDelegatedEmail()).isPresent()) {

            if(task.getChildTask() == null) {

                ApplicationUser delegatedUser = userService.findByEmail(request.getDelegatedEmail()).get();

                setLocalizationNotification(request, task, delegatedUser);

            }else if(!task.getDelegatedEmail().equals(request.getDelegatedEmail())){

                if(userService.findByEmail(request.getDelegatedEmail()).isPresent()){

                    ApplicationUser delegatedUser = userService.findByEmail(request.getDelegatedEmail()).get();
                    task.getChildTask().setIsCanceled(true);
                    task.getChildTask().setParentTask(null);

                    setLocalizationNotification(request, task, delegatedUser);

                }

            }

        }else if(task.getChildTask() != null){
            task.getChildTask().setIsCanceled(true);
            task.getChildTask().setParentTask(null);

            task.setChildTask(null);
        }

        task.setDelegatedEmail(request.getDelegatedEmail());

        if(task.getParentTask() != null){
            this.setParentTaskInformation(task, task.getParentTask());
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

                tag.setTaskId(task.getId());

                tagsToAdd.add(tag);
            }
        }

        tagService.saveAll(tagsToAdd);

        taskRepository.save(task);

    }

    private void setLocalizationNotification(UpdateTaskRequest request, Task task, ApplicationUser delegatedUser) {
        Task childTask;

        if(request.getLocalizationId() == null) {
            childTask = new Task(request.getTaskName(), request.getTaskDescription(), delegatedUser, this.getPriority(request.getPriority()), request.isIfDone(), request.getStartDate(), request.getEndDate(), task);
        }else{
            Localization notificationLocalization = task.getNotificationLocalization();

            AddLocalization childLoc = new AddLocalization(notificationLocalization.getLocalizationName(), notificationLocalization.getStreet(), notificationLocalization.getLocality(), notificationLocalization.getCountry(), notificationLocalization.getLongitude(), notificationLocalization.getLatitude());
            Long id = this.localizationService.addLocalization(delegatedUser.getEmail(), childLoc);

            Localization childTaskLocalization = this.localizationService.findById(id).get();

            childTask = new Task(request.getTaskName(), request.getTaskDescription(), delegatedUser, this.getPriority(request.getPriority()), request.isIfDone(), request.getStartDate(), request.getEndDate(), task, childTaskLocalization, request.getLocalizationRadius(), request.isNotificationOnEnter(), request.isNotificationOnExit());
        }

        task.setChildTask(childTask);
    }

    public String changeTaskStatus(long id){

        Task task = taskRepository.getOne(id);

        task.setIfDone(!task.getIfDone());

        if(task.getChildTask() != null){
            task.getChildTask().setIsCanceled(task.getIfDone());
            task.getChildTask().setIfDone(task.getIfDone());
            this.setParentTaskInformation(task.getChildTask(), task);
        }

        if(task.getParentTask() != null && task.getIfDone()){
            this.setParentTaskInformation(task, task.getParentTask());
            task.getParentTask().setIfDone(true);
        }

        taskRepository.save(task);

        return task.getTaskStatus();

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

    private TaskList getLocalization(String localizationName){
        switch(localizationName){
            case "INBOX":
                return TaskList.INBOX;
            case "SCHEDULED":
                return TaskList.SCHEDULED;
            case "ANYTIME":
                return TaskList.ANYTIME;
            case "TRASH":
                return TaskList.TRASH;
            case "COMPLETED":
                return TaskList.COMPLETED;
        }
        return TaskList.DELEGATED;
    }

    public List<String> getPriorities(){
        return Stream.of(TaskPriority.values()).map(TaskPriority::name).collect(Collectors.toList());
    }

    public List<Task> getUserTasks(String email){
        return taskRepository.findAllByUserEmail(email).get();
    }

    public void setParentTaskInformation(Task childTask, Task parentTask){
        if(childTask.getTaskList() == TaskList.INBOX && !childTask.getIfDone()){
            parentTask.setTaskStatus("Waiting");
        } else if((childTask.getTaskList() == TaskList.ANYTIME || childTask.getTaskList() == TaskList.SCHEDULED) && !childTask.getIfDone()){
            parentTask.setTaskStatus("In progress");
        } else if(childTask.getTaskList() == TaskList.COMPLETED || childTask.getIfDone()){
            parentTask.setTaskStatus("Done");
        } else if(childTask.getTaskList() == TaskList.TRASH){
            parentTask.setTaskStatus("Deleted");
        }
    }

    private void clearChildTasks(ApplicationUser user){

        List<Task> tasks = taskRepository.findAllByUserEmail(user.getEmail()).get();

        for(Task task : tasks){
            if(task.getChildTask() != null){
                task.getChildTask().setParentTask(null);
                taskRepository.save(task.getChildTask());
            }
            if(task.getParentTask() != null){
                task.getParentTask().setChildTask(null);
                taskRepository.save(task.getParentTask());
            }
        }

    }

    public void deleteAll(ApplicationUser user){
        this.clearChildTasks(user);
        taskRepository.deleteAllByUser(user);
    }

}
