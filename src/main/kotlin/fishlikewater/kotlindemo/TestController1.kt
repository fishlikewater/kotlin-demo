package fishlikewater.kotlindemo

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TestController1{

    @GetMapping("/test")
    fun view():String{

        return "11111111111";
    }

}

