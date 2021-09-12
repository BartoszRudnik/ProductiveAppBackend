package Xeva.productiveApp.task;

import Xeva.productiveApp.task.dto.*;
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

    @GetMapping("/getSingleTask/{mail}/{taskId}")
    public GetSingleTaskResponse getSingleTask(@PathVariable String mail, @PathVariable Long taskId){
        return taskService.getSingleTask(mail, taskId);
    }

    @GetMapping("/getSingleTaskFull/{mail}/{taskId}")
    public GetTasksResponse getSingleTaskFull(@PathVariable String mail, @PathVariable Long taskId){
        return this.taskService.getSingleTaskFull(mail, taskId);
    }

    @PostMapping("/deleteAllFromList")
    public void deleteAllFromList(@RequestBody DeleteAllRequest request){
        this.taskService.deleteAllFromList(request);
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

    @PostMapping("/done/{taskUuid}")
    public String changeTaskStatus(@PathVariable String taskUuid){
        return taskService.changeTaskStatus(taskUuid);
    }

    @GetMapping("/priorities")
    public List<String> getPriorities(){
        return taskService.getPriorities();
    }

}
