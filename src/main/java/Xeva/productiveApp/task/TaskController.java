package Xeva.productiveApp.task;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(path = "api/v1/task")
@AllArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping("/getAll/{mail}")
    public List<Task> getTasks(@PathVariable String mail){
        return taskService.getTasks(mail);
    }

    @PostMapping("/add")
    public long newTask(@RequestBody AddTaskRequest request){
        return taskService.addTask(request);
    }

    @DeleteMapping("/delete/{taskId}")
    public void deleteTask(@PathVariable long taskId){
        taskService.deleteTask(taskId);
    }

    @PutMapping("/update/{taskId}")
    public void updateTask(@RequestBody AddTaskRequest request, @PathVariable long taskId){
        taskService.updateTask(request, taskId);
    }

    @PutMapping("/done/{taskId}")
    public void changeTaskStatus(@PathVariable long taskId){
        taskService.changeTaskStatus(taskId);
    }

    @GetMapping("/priorities")
    public List<String> getPriorities(){
        return taskService.getPriorities();
    }

}
