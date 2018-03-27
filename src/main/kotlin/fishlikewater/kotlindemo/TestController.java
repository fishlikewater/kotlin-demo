package fishlikewater.kotlindemo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {


    @GetMapping("/test1")
    public String view(){

        return "2222222";
    }
}
