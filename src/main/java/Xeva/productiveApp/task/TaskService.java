package Xeva.productiveApp.task;

import Xeva.productiveApp.appUser.AppUserService;
import Xeva.productiveApp.appUser.ApplicationUser;
import Xeva.productiveApp.attachment.Attachment;
import Xeva.productiveApp.attachment.AttachmentRepository;
import Xeva.productiveApp.localization.Localization;
import Xeva.productiveApp.localization.LocalizationRepository;
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

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
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
    private final AttachmentRepository attachmentRepository;
    private final LocalizationRepository localizationRepository;

    public void save(Task task){
        this.taskRepository.save(task);
    }

    public GetTasksResponse getSingleTaskFull(String mail, String taskUuid){

        boolean isUser = userService.findByEmail(mail).isPresent();

        if(!isUser){
            throw new IllegalStateException("Wrong user");
        }

        ApplicationUser user = userService.findByEmail(mail).get();
        user.setNewTask(false);

        Task task = new Task();
        if(taskRepository.findByUuid(taskUuid).isPresent()) {
            task = taskRepository.findByUuid(taskUuid).get();
        }

        GetTasksResponse taskResponse = new GetTasksResponse();

        if(task.getParentTask() != null){

            ApplicationUser parentUser = userService.findByEmail(task.getParentTask().getUser().getEmail()).get();
            UserRelation relation = userRelationService.getUsersRelation(user, parentUser);

            if(relation != null && relation.getState() == RelationState.ACCEPTED){
                this.setParentTaskInformation(task, task.getParentTask());
                this.taskRepository.save(task);

                taskResponse = new GetTasksResponse(task, tagService.findAllByTaskId(task.getId()), task.getParentTask().getUser().getEmail(), null, task.getParentTask().getUuid());
            }

        }else if(task.getChildTask() != null){
            taskResponse = new GetTasksResponse(task, tagService.findAllByTaskId(task.getId()), null, task.getChildTask().getUuid(), null);
        }
        else {
            taskResponse = new GetTasksResponse(task, tagService.findAllByTaskId(task.getId()));
        }

        this.userService.save(user);

        return taskResponse;
    }

    public GetSingleTaskResponse getSingleTask(String mail, String taskUuid){
        boolean isUser = userService.findByEmail(mail).isPresent();

        if(!isUser){
            throw new IllegalStateException("Wrong user");
        }

        Task task = this.taskRepository.findByUuid(taskUuid).get();

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

    public void saveTask(Task task){
        this.taskRepository.save(task);
        task.setPosition(task.getId() + 1000.0);
        this.taskRepository.save(task);
    }

    public void setTagsFromNames(Task task, List<String> tagNames){
        this.tagService.deleteByTaskId(task.getId());

        if(tagNames != null) {
            for (String tagName : tagNames) {
                Tag tag = new Tag(task.getUser().getEmail(), tagName, task.getId(), UUID.randomUUID().toString());

                this.tagService.save(tag);
            }
        }
    }

    public void setTags(Task task, List<Tag> tags){
        for(Tag tag : tags){
            if(tag.getTaskId() == null) {
                tag.setId(null);
            }
            tag.setTaskId(task.getId());
            tag.setOwnerEmail(task.getUser().getEmail());
        }
        tagService.saveAll(tags);
    }

    public void addTask(ApplicationUser user, TaskPriority priority, TaskList localization, List<String> tags, String delegatedEmail, String localizationUuid, String taskName, String taskDescription, boolean isDone, LocalDateTime startDate, LocalDateTime endDate, String uuid, double localizationRadius, boolean notificationOnEnter, boolean notificationOnExit, String taskState){
        Task task;

        if(localization == TaskList.DELEGATED && delegatedEmail != null && delegatedEmail.length() > 1){

            ApplicationUser delegatedUser = this.userService.findByEmail(delegatedEmail).get();

            if(localizationUuid == null) {
                task = new Task(taskName, taskDescription, user, localization, priority, isDone, startDate, endDate, delegatedUser, delegatedEmail, uuid, taskState);
            }else{
                Localization notificationLocalization = this.localizationService.findByUuid(localizationUuid);

                task = new Task(taskName, taskDescription, user, localization, priority,isDone, startDate, endDate, delegatedUser, delegatedEmail, notificationLocalization, localizationRadius, notificationOnEnter,
                        notificationOnExit, uuid, taskState);
            }

        }else{

            if(localizationUuid == null) {
                task = new Task(taskName, taskDescription, user, startDate, endDate, isDone, priority, localization, delegatedEmail, uuid, taskState);
            }else{
                Localization notificationLocalization = this.localizationService.findByUuid(localizationUuid);
                task = new Task(taskName, taskDescription, user, startDate, endDate, isDone, priority, localization, delegatedEmail, notificationLocalization,notificationOnEnter, localizationRadius,
                       notificationOnExit, uuid, taskState);
            }

        }
        this.saveTask(task);
        this.setTagsFromNames(task, tags);

    }

    public AddResponse addTask(AddTaskRequest request){

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

            if(request.getLocalizationUuid() == null) {
                task = new Task(request.getTaskName(), request.getTaskDescription(), user, localization, priority, request.isIfDone(), request.getStartDate(), request.getEndDate(), delegatedUser, request.getDelegatedEmail(), request.getUuid(), request.getTaskState());
            }else{
                Localization notificationLocalization = this.localizationService.findByUuid(request.getLocalizationUuid());
                task = new Task(request.getTaskName(), request.getTaskDescription(), user, localization, priority, request.isIfDone(), request.getStartDate(), request.getEndDate(), delegatedUser, request.getDelegatedEmail(), notificationLocalization, request.getLocalizationRadius(), request.isNotificationOnEnter(),
                        request.isNotificationOnExit(), request.getUuid(), request.getTaskState());
            }

        }else{

            if(request.getLocalizationUuid() == null) {
                task = new Task(request.getTaskName(), request.getTaskDescription(), user, request.getStartDate(), request.getEndDate(), request.isIfDone(), priority, localization, request.getDelegatedEmail(), request.getUuid(), request.getTaskState());
            }else{
                Localization notificationLocalization = this.localizationService.findByUuid(request.getLocalizationUuid());
                task = new Task(request.getTaskName(), request.getTaskDescription(), user, request.getStartDate(), request.getEndDate(), request.isIfDone(), priority, localization, request.getDelegatedEmail(), notificationLocalization, request.isNotificationOnEnter(), request.getLocalizationRadius(),
                        request.isNotificationOnExit(), request.getUuid(), request.getTaskState());
            }

        }
        this.saveTask(task);
        this.setTags(task, tags);

        String childTaskUuid = "";
        String parentTaskUuid = "";
        String parentTaskEmail = "";

        if(task.getChildTask() != null){
            childTaskUuid = task.getChildTask().getUuid();
        }
        if(task.getParentTask() != null){
            parentTaskUuid = task.getParentTask().getUuid();
            parentTaskEmail = task.getParentTask().getUser().getEmail();
        }

        return new AddResponse(task.getId(), childTaskUuid, parentTaskUuid, parentTaskEmail);
    }

    public void deleteTask(String uuid, Long id){
        if(!this.attachmentRepository.findAllByTaskId(id).isEmpty()){
            this.attachmentRepository.deleteAllByTaskId(id);
        }

        taskRepository.deleteByUuid(uuid);
    }

    private void clearTask(String uuid){
        if(this.taskRepository.findByUuid(uuid).isPresent()){
            Task taskToClear = this.taskRepository.findByUuid(uuid).get();

            if(taskToClear.getChildTask() != null){
                taskToClear.getChildTask().setParentTask(null);
                taskToClear.setChildTask(null);
            }

            if(taskToClear.getParentTask() != null){
                taskToClear.getParentTask().setChildTask(null);
                taskToClear.setParentTask(null);
            }

            if(taskToClear.getAttachments() != null){
                taskToClear.setAttachments(null);
            }

            this.taskRepository.save(taskToClear);
        }
    }

    @Transactional
    public void deleteAllFromList(DeleteAllRequest request){
        for(String uuid : request.getTasks()){
            this.clearTask(uuid);

            if(this.attachmentRepository.findAllByTaskUuid(uuid).isPresent()){

                List<Attachment> attachments = this.attachmentRepository.findAllByTaskUuid(uuid).get();

                for(Attachment attachment : attachments){
                    attachment.setTask(null);
                    this.attachmentRepository.save(attachment);
                }

                this.attachmentRepository.deleteAllByUuid(uuid);
            }

            this.taskRepository.deleteByUuid(uuid);
        }
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

                    tasksResponse.add(new GetTasksResponse(task, tagService.findAllByTaskId(task.getId()), task.getParentTask().getUser().getEmail(), null, task.getParentTask().getUuid()));
                }

            }else if(task.getChildTask() != null){
                tasksResponse.add(new GetTasksResponse(task, tagService.findAllByTaskId(task.getId()), null, task.getChildTask().getUuid(), null));
            }
            else {
                tasksResponse.add(new GetTasksResponse(task, tagService.findAllByTaskId(task.getId())));
            }
        }

        user.setNewTask(false);
        this.userService.save(user);

        return tasksResponse;

    }

    public Task findByUuid(String uuid){
        if(this.taskRepository.findByUuid(uuid).isPresent()) {
            return this.taskRepository.findByUuid(uuid).get();
        }else{
            return null;
        }
    }

    public void updateTaskPosition(UpdateTaskPositionRequest request, long id){
        Task task = taskRepository.findById(id).get();

        task.setPosition(request.getPosition());

        taskRepository.save(task);
    }

    public void updateTask(List<String> tags, double position, String uuid, String description, String taskName, LocalDateTime startDate, LocalDateTime endDate, String priority, boolean isDone, String taskList, boolean isCanceled, String localizationUuid, double notificationRadius, boolean notificationOnEnter, boolean notificationOnExit, String delegatedEmail, String taskState){
        if(this.taskRepository.findByUuid(uuid).isEmpty()){
            throw new IllegalStateException("Task doesn't exist");
        }

        Task task = this.taskRepository.findByUuid(uuid).get();

        this.updateTaskData(task, uuid, description, taskName, startDate, endDate, priority, isDone, taskList, position, isCanceled, taskState);

        if(localizationUuid != null) {
            task.setNotificationLocalization(this.localizationService.findByUuid(localizationUuid));
            task.setLocalizationRadius(notificationRadius);
            task.setNotificationOnEnter(notificationOnEnter);
            task.setNotificationOnExit(notificationOnExit);
        }

        if(task.getChildTask() != null && (taskList.equals("TRASH") || taskList.equals("COMPLETED"))){
            if(!task.getChildTask().getIfDone()) {
                task.getChildTask().setIsCanceled(true);
            }
        }
        else if(userService.findByEmail(delegatedEmail).isPresent()) {
            if(task.getChildTask() == null) {
                ApplicationUser delegatedUser = userService.findByEmail(delegatedEmail).get();

                this.setLocalizationNotification(task, delegatedUser, localizationUuid, taskName, description, priority, isDone, startDate, endDate, notificationRadius, notificationOnEnter, notificationOnExit);
            }else if(!task.getDelegatedEmail().equals(delegatedEmail)){
                if(userService.findByEmail(delegatedEmail).isPresent()){
                    ApplicationUser delegatedUser = userService.findByEmail(delegatedEmail).get();
                    task.getChildTask().setIsCanceled(true);
                    task.getChildTask().setParentTask(null);

                    this.setLocalizationNotification(task, delegatedUser, localizationUuid, taskName, description, priority, isDone, startDate, endDate, notificationRadius, notificationOnEnter, notificationOnExit);
                }
            }
        else if(task.getChildTask() != null && task.getDelegatedEmail().equals(delegatedEmail) && this.localizationRepository.findTopByUuid(localizationUuid).isPresent()){
            ApplicationUser delegatedUser = this.userService.findByEmail(delegatedEmail).get();

            Localization existingLocalization = this.localizationService.findByUuid(localizationUuid);
            Localization newLocalization = new Localization(existingLocalization.getLocalizationName(), existingLocalization.getStreet(), existingLocalization.getLocality(), existingLocalization.getCountry(), existingLocalization.getLongitude(), existingLocalization.getLatitude(), delegatedUser, UUID.randomUUID().toString(), false);

            if(this.localizationRepository.findTopByCountryAndLocalityAndStreetAndLatitudeAndLongitudeAndUser(existingLocalization.getCountry(), existingLocalization.getLocality(), existingLocalization.getStreet(), existingLocalization.getLatitude(), existingLocalization.getLongitude(), delegatedUser).isEmpty()) {
                this.localizationRepository.save(newLocalization);
            }else{
                newLocalization = this.localizationRepository.findTopByCountryAndLocalityAndStreetAndLatitudeAndLongitudeAndUser(existingLocalization.getCountry(), existingLocalization.getLocality(), existingLocalization.getStreet(), existingLocalization.getLatitude(), existingLocalization.getLongitude(), delegatedUser).get();
                newLocalization.setUuid(UUID.randomUUID().toString());
            }
            task.getChildTask().setNotificationLocalization(newLocalization);
            task.getChildTask().setNotificationOnExit(notificationOnExit);
            task.getChildTask().setNotificationOnEnter(notificationOnEnter);
            task.getChildTask().setLocalizationRadius(notificationRadius);
        }
        }

        task.setDelegatedEmail(delegatedEmail);

        if(!task.getIfDone() && task.getChildTask() != null && task.getChildTask().getIsCanceled() && (task.getTaskList() != TaskList.TRASH && task.getTaskList() != TaskList.COMPLETED)){
            task.getChildTask().setIsCanceled(false);

            if(task.getChildTask().getTaskList() == TaskList.COMPLETED || task.getChildTask().getTaskList() == TaskList.TRASH){
                task.getChildTask().setTaskList(TaskList.INBOX);
            }
        }

        if(task.getParentTask() != null){
            this.setParentTaskInformation(task, task.getParentTask());
        }

        this.setTagsFromNames(task, tags);


        this.taskRepository.save(task);
    }

    public AddResponse updateTask(UpdateTaskRequest request){
        if(this.taskRepository.findByUuid(request.getUuid()).isEmpty()){
            throw new IllegalStateException("Task doesn't exist");
        }

        Task task = this.taskRepository.findByUuid(request.getUuid()).get();

        this.updateTaskData(request, task);

        if(request.getLocalizationUuid() != null) {
            task.setNotificationLocalization(this.localizationService.findByUuid(request.getLocalizationUuid()));
            task.setLocalizationRadius(request.getLocalizationRadius());
            task.setNotificationOnEnter(request.isNotificationOnEnter());
            task.setNotificationOnExit(request.isNotificationOnExit());
        }

        if(task.getChildTask() != null && (request.getLocalization().equals("TRASH") || request.getLocalization().equals("COMPLETED"))){
            if(!task.getChildTask().getIfDone()) {
                task.getChildTask().setIsCanceled(true);
            }
        }
        else if(this.userService.findByEmail(request.getDelegatedEmail()).isPresent()) {
            if(task.getChildTask() == null && task.getTaskList() == TaskList.DELEGATED) {
                ApplicationUser delegatedUser = this.userService.findByEmail(request.getDelegatedEmail()).get();

                this.setLocalizationNotification(request, task, delegatedUser);
            }else if(!task.getDelegatedEmail().equals(request.getDelegatedEmail())){
                if(this.userService.findByEmail(request.getDelegatedEmail()).isPresent()){
                    ApplicationUser delegatedUser = this.userService.findByEmail(request.getDelegatedEmail()).get();
                    task.getChildTask().setIsCanceled(true);
                    task.getChildTask().setParentTask(null);

                    this.setLocalizationNotification(request, task, delegatedUser);
                }
            }else if(task.getChildTask() != null && task.getDelegatedEmail().equals(request.getDelegatedEmail()) && this.localizationRepository.findTopByUuid(request.getLocalizationUuid()).isPresent()){
               ApplicationUser delegatedUser = this.userService.findByEmail(request.getDelegatedEmail()).get();

               Localization existingLocalization = this.localizationService.findByUuid(request.getLocalizationUuid());
               Localization newLocalization = new Localization(existingLocalization.getLocalizationName(), existingLocalization.getStreet(), existingLocalization.getLocality(), existingLocalization.getCountry(), existingLocalization.getLongitude(), existingLocalization.getLatitude(), delegatedUser, UUID.randomUUID().toString(), false);

                if(this.localizationRepository.findTopByCountryAndLocalityAndStreetAndLatitudeAndLongitudeAndUser(existingLocalization.getCountry(), existingLocalization.getLocality(), existingLocalization.getStreet(), existingLocalization.getLatitude(), existingLocalization.getLongitude(), delegatedUser).isEmpty()) {
                    this.localizationRepository.save(newLocalization);
                }else{
                    newLocalization = this.localizationRepository.findTopByCountryAndLocalityAndStreetAndLatitudeAndLongitudeAndUser(existingLocalization.getCountry(), existingLocalization.getLocality(), existingLocalization.getStreet(), existingLocalization.getLatitude(), existingLocalization.getLongitude(), delegatedUser).get();
                    newLocalization.setUuid(UUID.randomUUID().toString());
                }

               task.getChildTask().setNotificationLocalization(newLocalization);
               task.getChildTask().setNotificationOnExit(request.isNotificationOnExit());
               task.getChildTask().setNotificationOnEnter(request.isNotificationOnEnter());
               task.getChildTask().setLocalizationRadius(request.getLocalizationRadius());
            }
        }
        task.setDelegatedEmail(request.getDelegatedEmail());

        if(!task.getIfDone() && task.getChildTask() != null && task.getChildTask().getIsCanceled() && (task.getTaskList() != TaskList.TRASH && task.getTaskList() != TaskList.COMPLETED)){
            task.getChildTask().setIsCanceled(false);

            if(task.getChildTask().getTaskList() == TaskList.COMPLETED || task.getChildTask().getTaskList() == TaskList.TRASH){
                task.getChildTask().setTaskList(TaskList.INBOX);
            }
        }

        if(task.getParentTask() != null){
            this.setParentTaskInformation(task, task.getParentTask());
        }

        this.updateTags(request.getTags(), task.getId(), task.getUser().getEmail());

        this.taskRepository.save(task);

        String childTaskUuid = "";
        String parentTaskUuid = "";
        String parentTaskEmail = "";

        if(task.getChildTask() != null){
            childTaskUuid = task.getChildTask().getUuid();
        }
        if(task.getParentTask() != null){
            parentTaskUuid = task.getParentTask().getUuid();
            parentTaskEmail = task.getParentTask().getUser().getEmail();
        }

        return new AddResponse(task.getId(), childTaskUuid, parentTaskUuid, parentTaskEmail);
    }

    private void updateTaskData(Task task, String uuid, String description, String taskName, LocalDateTime startDate, LocalDateTime endDate, String priority, boolean isDone, String taskList, double position, boolean isCanceled, String taskState){
        task.setUuid(uuid);
        task.setDescription(description);
        task.setTaskName(taskName);
        task.setStartDate(startDate);
        task.setEndDate(endDate);
        task.setPriority(this.getPriority(priority));
        task.setIfDone(isDone);
        task.setTaskList(this.getLocalization(taskList));
        task.setPosition(position);
        task.setIsCanceled(isCanceled);
        task.setTaskState(taskState);
    }

    private void updateTaskData(UpdateTaskRequest request, Task task){
        task.setUuid(request.getUuid());
        task.setDescription(request.getTaskDescription());
        task.setTaskName(request.getTaskName());
        task.setStartDate(request.getStartDate());
        task.setEndDate(request.getEndDate());
        task.setPriority(this.getPriority(request.getPriority()));
        task.setIfDone(request.isIfDone());
        task.setTaskList(this.getLocalization(request.getLocalization()));
        task.setPosition(request.getPosition());
        task.setIsCanceled(request.isCanceled());
        task.setTaskState(request.getTaskState());
    }

    private void updateTags(List<Tag> tags, Long taskId, String userMail){
        List<Tag> existingTags = this.tagService.findAllByTaskId(taskId);
        List<Tag> tagsToAdd = new ArrayList<>();

        for(Tag tag : tags){
            tag.setOwnerEmail(userMail);
        }

        for(Tag tag : existingTags){
            if(!tags.contains(tag)){
                this.tagService.deleteById(tag.getId());
            }
        }

        for(Tag tag : tags){
            if(!existingTags.contains(tag)) {
                if (tag.getTaskId() == null) {
                    tag.setId(null);
                }

                tag.setTaskId(taskId);

                tagsToAdd.add(tag);
            }
        }

        this.tagService.saveAll(tagsToAdd);
    }

    private void setLocalizationNotification(Task task, ApplicationUser delegatedUser, String localizationUuid, String taskName, String description, String priority, boolean isDone, LocalDateTime startDate, LocalDateTime endDate, double localizationRadius, boolean notificationOnEnter, boolean notificationOnExit) {
        Task childTask;

        if(localizationUuid == null) {
            childTask = new Task(taskName, description, delegatedUser, this.getPriority(priority), isDone, startDate, endDate, task, UUID.randomUUID().toString());
        }else{
            Localization childTaskLocalization = setDelegatedTaskLocalization(task, delegatedUser);

            childTask = new Task(taskName, description, delegatedUser, this.getPriority(priority), isDone, startDate, endDate, task, childTaskLocalization, localizationRadius, notificationOnEnter, notificationOnExit, UUID.randomUUID().toString());
        }

        task.setChildTask(childTask);
    }

    private void setLocalizationNotification(UpdateTaskRequest request, Task task, ApplicationUser delegatedUser) {
        Task childTask;

        if(request.getLocalizationUuid() == null) {
            childTask = new Task(request.getTaskName(), request.getTaskDescription(), delegatedUser, this.getPriority(request.getPriority()), request.isIfDone(), request.getStartDate(), request.getEndDate(), task, UUID.randomUUID().toString());
        }else{
            Localization childTaskLocalization = setDelegatedTaskLocalization(task, delegatedUser);

            childTask = new Task(request.getTaskName(), request.getTaskDescription(), delegatedUser, this.getPriority(request.getPriority()), request.isIfDone(), request.getStartDate(), request.getEndDate(), task, childTaskLocalization, request.getLocalizationRadius(), request.isNotificationOnEnter(), request.isNotificationOnExit(), UUID.randomUUID().toString());
        }

        task.setChildTask(childTask);
    }

    private Localization setDelegatedTaskLocalization(Task task, ApplicationUser delegatedUser) {
        Localization notificationLocalization = task.getNotificationLocalization();

        AddLocalization childLoc = new AddLocalization(notificationLocalization.getLocalizationName(), notificationLocalization.getStreet(), notificationLocalization.getLocality(), notificationLocalization.getCountry(), notificationLocalization.getLongitude(), notificationLocalization.getLatitude(), notificationLocalization.getUuid(), false);
        Long id = this.localizationService.addLocalization(delegatedUser.getEmail(), childLoc);

        return this.localizationService.findById(id).get();
    }

    public String changeTaskStatus(String uuid){

        Task task = new Task();

        if(this.taskRepository.findByUuid(uuid).isPresent()) {
            task = this.taskRepository.findByUuid(uuid).get();
        }

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

    public TaskPriority getPriority(String priorityName){
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

    public TaskList getLocalization(String localizationName){
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

    public boolean isPresent(String uuid){
        return this.taskRepository.findByUuid(uuid).isPresent();
    }

    @Transactional
    public void deleteTaskByUuid(String uuid){
        this.taskRepository.deleteByUuid(uuid);
    }

    public List<GetTasksResponse> getTaskFromCollaborator(String userMail, String collaboratorMail) {
        if(this.userService.findByEmail(collaboratorMail).isEmpty()){
            throw new IllegalStateException("Collaborator doesn't exist");
        }

        if(this.userService.findByEmail(userMail).isEmpty()){
            throw new IllegalStateException("User doesn't exist");
        }

        ApplicationUser user= this.userService.findByEmail(userMail).get();
        ApplicationUser collaborator = this.userService.findByEmail(collaboratorMail).get();

        List<Task> tasks = new ArrayList<>();
        List<Task> childTasks = new ArrayList<>();
        List<GetTasksResponse> tasksResponse = new ArrayList<>();

        if(this.taskRepository.findAllByUserAndDelegatedEmail(collaborator, userMail).isPresent()){
            tasks = this.taskRepository.findAllByUserAndDelegatedEmail(collaborator, userMail).get();
        }

        for(Task task : tasks){
            if(task.getChildTask() != null){
                childTasks.add(task.getChildTask());
            }
        }

        for(Task task : childTasks){
            if(task.getParentTask() != null){
                UserRelation relation = userRelationService.getUsersRelation(user, collaborator);

                if(relation != null && (relation.getState() == RelationState.WAITING || relation.getState() == RelationState.ACCEPTED)){
                    this.setParentTaskInformation(task, task.getParentTask());
                    this.taskRepository.save(task);

                    tasksResponse.add(new GetTasksResponse(task, tagService.findAllByTaskId(task.getId()), task.getParentTask().getUser().getEmail(), null, task.getParentTask().getUuid()));
                }

            }else if(task.getChildTask() != null){
                tasksResponse.add(new GetTasksResponse(task, tagService.findAllByTaskId(task.getId()), null, task.getChildTask().getUuid(), null));
            }
            else {
                tasksResponse.add(new GetTasksResponse(task, tagService.findAllByTaskId(task.getId())));
            }
        }

        return tasksResponse;
    }
}
