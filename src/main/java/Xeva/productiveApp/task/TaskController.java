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

    @GetMapping("/getAll")
    public List<Task> getTasks(){
        return taskService.getTasks();
    }

    @PostMapping("/add")
    public void newTask(@RequestBody TaskRequest request){
        taskService.addTask(request);
    }

    @DeleteMapping("/delete/{taskId}")
    public void deleteTask(@PathVariable long taskId){
        taskService.deleteTask(taskId);
    }

    @PutMapping("/update/{taskId}")
    public void updateTask(@RequestBody TaskRequest request, @PathVariable long taskId){
        taskService.updateTask(request, taskId);
    }

}
