package Xeva.productiveApp.tags;

import Xeva.productiveApp.appUser.AppUserService;
import Xeva.productiveApp.appUser.ApplicationUser;
import Xeva.productiveApp.task.Task;
import Xeva.productiveApp.task.TaskRepository;
import Xeva.productiveApp.task.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class TagService {

    private final TagRepository tagRepository;
    private final TaskRepository taskRepository;
    private final AppUserService appUserService;

    public void saveAll(List<Tag> tags){
        tagRepository.saveAll(tags);
    }

    public List<Tag> findAllByTaskId(Long id){
        return tagRepository.findAllByTaskId(id).get();
    }

    public Set<Tag> findAllByEmail(String email){
        boolean isEmail = appUserService.findByEmail(email).isPresent();

        if(!isEmail){
            throw new IllegalStateException("Wrong email");
        }

        List<Task> userTasks = taskRepository.findAllByUserEmail(email).get();

        Set<Tag> tags = new HashSet<>();

        for(Task task : userTasks){
            tags.addAll(tagRepository.findAllByTaskId(task.getId_task()).get());
        }

        return tags;

    }

}
