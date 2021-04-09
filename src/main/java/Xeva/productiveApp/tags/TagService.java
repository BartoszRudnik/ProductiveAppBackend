package Xeva.productiveApp.tags;

import Xeva.productiveApp.task.Task;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TagService {

    private final TagRepository tagRepository;

    public void saveAll(List<Tag> tags){
        tagRepository.saveAll(tags);
    }

    public List<Tag> findAllByTaskId(Long id){
        return tagRepository.findAllByTaskId(id).get();
    }

}
