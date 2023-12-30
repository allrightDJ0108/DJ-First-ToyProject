package toy.project.boot.domain;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestContainer {
    @GetMapping("/test")

    public String test() {
        return "Hello World222";
    }

}
