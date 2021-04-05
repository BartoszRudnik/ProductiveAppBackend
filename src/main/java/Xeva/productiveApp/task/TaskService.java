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

    public void addTask(TaskRequest request){

        boolean isUser = userService.findByEmail(request.getUserEmail()).isPresent();

        if(!isUser){
            throw new IllegalStateException("Wrong user");
        }

        ApplicationUser user = userService.findByEmail(request.getUserEmail()).get();

        Task task = new Task(request.getTaskName(), request.getTaskDescription(), user, request.getStartDate(), request.getEndDate());

        taskRepository.save(task);
    }

    public void deleteTask(long id){
        taskRepository.deleteById(id);
    }

    public List<Task> getTasks(){
        return taskRepository.findAll();
    }

    public void updateTask(TaskRequest request, long id){
        Task task = taskRepository.findById(id).get();

        task.setDescription(request.getTaskDescription());
        task.setTask_name(request.getTaskName());
        task.setStartDate(request.getStartDate());
        task.setEndDate(request.getEndDate());

        taskRepository.save(task);

    }

}
