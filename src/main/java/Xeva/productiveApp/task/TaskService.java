package Xeva.productiveApp.task;

import Xeva.productiveApp.appUser.AppUserService;
import Xeva.productiveApp.appUser.ApplicationUser;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final AppUserService userService;

    public long addTask(AddTaskRequest request){

        boolean isUser = userService.findByEmail(request.getUserEmail()).isPresent();

        if(!isUser){
            throw new IllegalStateException("Wrong user");
        }

        ApplicationUser user = userService.findByEmail(request.getUserEmail()).get();

        Task task = new Task(request.getTaskName(), request.getTaskDescription(), user, request.getStartDate(), request.getEndDate(), request.isIfDone());

        taskRepository.save(task);

        return task.getId_task();

    }

    public void deleteTask(long id){
        taskRepository.deleteById(id);
    }

    public List<Task> getTasks(String email){

        boolean isUser = userService.findByEmail(email).isPresent();

        if(!isUser){
            throw new IllegalStateException("Wrong user");
        }

        ApplicationUser user = userService.findByEmail(email).get();

        return taskRepository.findAllByUserEmail(user.getEmail()).get();

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

}
