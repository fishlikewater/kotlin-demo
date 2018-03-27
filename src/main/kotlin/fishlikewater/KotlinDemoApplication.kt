package fishlikewater

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan("fishlikewater")
class KotlinDemoApplication

fun main(args: Array<String>) {
    SpringApplication.run(KotlinDemoApplication::class.java, *args)
}
