package Xeva.productiveApp.tags;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@CrossOrigin
@RequestMapping(path = "api/v1/tag")
@AllArgsConstructor
public class TagController {

    private final TagService tagService;

    @GetMapping("/getAll/{mail}")
    public Set<Tag> getTags(@PathVariable String mail){
        return tagService.findAllByEmail(mail);
    }

    @DeleteMapping("/delete/{tagName}&{email}")
    public void deleteTag(@PathVariable String tagName, @PathVariable String email){
        tagService.deleteByName(tagName, email);
    }

    @PutMapping("/update/{mail}")
    public void updateTag(@PathVariable String mail, @RequestBody UpdateRequest request){
        tagService.updateTag(mail, request);
    }

    @PostMapping("/add")
    public void addTag(@RequestBody Tag tag){
        tagService.save(tag);
    }

}
