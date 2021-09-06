package Xeva.productiveApp.tags;

import Xeva.productiveApp.appUser.AppUserService;
import Xeva.productiveApp.tags.dto.UpdateRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

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

    public List<Tag> findByTagName(String tagName){
        return this.tagRepository.findAllByName(tagName);
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

    public void deleteByTaskId(Long taskId){
        this.tagRepository.deleteAllByTaskId(taskId);
    }

    public void updateTag(String mail, String oldName, String newName){
        List<Tag> tagsToEdit = this.tagRepository.findAllByNameAndOwnerEmail(oldName, mail);

        if(tagsToEdit != null) {
            for(Tag tagToEdit : tagsToEdit) {
                tagToEdit.setName(newName);

                this.tagRepository.save(tagToEdit);
            }
        }
    }

    public void updateTag(String mail, UpdateRequest request){
           List<Tag> tagsToEdit = this.tagRepository.findAllByNameAndOwnerEmail(request.getOldName(), mail);

           if(tagsToEdit != null) {
               for(Tag tagToEdit : tagsToEdit) {
                   tagToEdit.setName(request.getNewName());

                   this.tagRepository.save(tagToEdit);
               }
           }
    }

    public boolean tagAlreadyExist(String mail, Long id){
        return this.tagRepository.findByOwnerEmailAndId(mail ,id).isPresent();
    }

    public void deleteByName(String tagName, String email){
        tagRepository.deleteByNameAndOwnerEmail(tagName, email);
    }

    public void deleteById(Long id){
        tagRepository.deleteById(id);
    }

    public void deleteByUser(String userMail){
        tagRepository.deleteAllByOwnerEmail(userMail);
    }

}
