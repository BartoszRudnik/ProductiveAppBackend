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
    private final AppUserService appUserService;

    public void saveAll(List<Tag> tags){
        tagRepository.saveAll(tags);
    }

    public void save(Tag tag){
        tagRepository.save(tag);
    }

    public List<Tag> findAllByTaskId(Long id){
        return tagRepository.findAllByTaskId(id).get();
    }

    public Set<Tag> findAllByEmail(String email){
        boolean isEmail = appUserService.findByEmail(email).isPresent();

        if(!isEmail){
            throw new IllegalStateException("Wrong email");
        }

        return tagRepository.findAllByOwnerEmail(email).get();

    }

    public void updateTag(String mail, UpdateRequest request){
        tagRepository.updateTagName(request.getNewName(), request.oldName, mail);
    }

    public void deleteByName(String tagName, String email){
        tagRepository.deleteByNameAndOwnerEmail(tagName, email);
    }

}
