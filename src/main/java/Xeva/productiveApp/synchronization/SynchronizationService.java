package Xeva.productiveApp.synchronization;

import Xeva.productiveApp.synchronization.dto.SynchronizeTagsRequest;
import Xeva.productiveApp.tags.Tag;
import Xeva.productiveApp.tags.TagService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class SynchronizationService {

    private final TagService tagService;

    public Set<Tag> synchronizeTags(String mail, List<SynchronizeTagsRequest> requestList){

        Set<Tag> onlineTags = this.tagService.findAllByEmail(mail);

        for(SynchronizeTagsRequest tag : requestList){
            if(!this.tagService.tagAlreadyExist(mail, tag.getName())){
                Tag newTag = new Tag(mail, tag.getName());

                this.tagService.save(newTag);
            }
        }

        return this.tagService.findAllByEmail(mail);
    }

}
