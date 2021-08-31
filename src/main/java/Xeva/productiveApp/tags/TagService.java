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
       if(this.tagRepository.findByNameAndOwnerEmail(request.getOldName(), mail).isPresent()){
           Tag tagToEdit = this.tagRepository.findByNameAndOwnerEmail(request.getOldName(), mail).get();

           tagToEdit.setName(request.getNewName());

           this.tagRepository.save(tagToEdit);
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
