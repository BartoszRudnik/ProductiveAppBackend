package Xeva.productiveApp.synchronization;

import Xeva.productiveApp.synchronization.dto.SynchronizeTagsRequest;
import Xeva.productiveApp.synchronization.dto.SynchronizeTagsRequestList;
import Xeva.productiveApp.tags.Tag;
import Xeva.productiveApp.tags.TagRepository;
import Xeva.productiveApp.tags.TagService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class SynchronizationService {

    private final TagService tagService;
    private final TagRepository tagRepository;

    public Set<Tag> synchronizeTags(String mail, SynchronizeTagsRequestList requestList){

        List<String> namesFromDevice = new ArrayList<>();

        for(SynchronizeTagsRequest tag : requestList.getTagList()){
            namesFromDevice.add(tag.getName());

            if(!this.tagService.tagAlreadyExist(mail, tag.getId())){

                Tag newTag = new Tag(mail, tag.getName());

                this.tagService.save(newTag);
            }else{
                Tag existingTag = this.tagRepository.findById(tag.getId()).get();

                System.out.println(existingTag.getLastUpdated());
                System.out.println(tag.getLastUpdated());

                if(existingTag.getLastUpdated().isBefore(tag.getLastUpdated())){
                    existingTag.setName(tag.getName());

                    this.tagService.save(existingTag);
                }
            }
        }

        Set<Tag> updatedTags = this.tagService.findAllByEmail(mail);

        for(Tag tag : updatedTags){
            if(!namesFromDevice.contains(tag.getName())){
                this.tagService.deleteById(tag.getId());
            }
        }

        return this.tagService.findAllByEmail(mail);
    }

}
