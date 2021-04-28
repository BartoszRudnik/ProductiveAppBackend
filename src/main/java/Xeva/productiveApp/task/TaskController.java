package Xeva.productiveApp.task;

import Xeva.productiveApp.task.requests.AddTaskRequest;
import Xeva.productiveApp.task.requests.GetTasksResponse;
import Xeva.productiveApp.task.requests.UpdateTaskPositionRequest;
import Xeva.productiveApp.task.requests.UpdateTaskRequest;
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
    public List<GetTasksResponse> getTasks(@PathVariable String mail){
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
    public void updateTask(@RequestBody UpdateTaskRequest request, @PathVariable long taskId){
        taskService.updateTask(request, taskId);
    }

    @PutMapping("/updatePosition/{taskId}")
    public void updateTaskPosition(@RequestBody UpdateTaskPositionRequest request, @PathVariable long taskId){
        taskService.updateTaskPosition(request, taskId);
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
