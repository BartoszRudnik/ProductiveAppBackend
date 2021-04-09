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

    @PostMapping("/add")
    public void addTag(@RequestBody Tag tag){
        tagService.save(tag);
    }

}
