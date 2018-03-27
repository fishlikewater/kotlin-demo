package fishlikewater.kotlindemo

import org.apache.commons.lang.math.NumberUtils
import org.junit.Test
import java.util.*
import java.util.stream.Stream


class KotlinDemoApplicationTests {

    var a = 1;
    val b = 2.31;


    @Test
    fun contextLoads() {

        val inputStream = Stream.of(
                Arrays.asList(1),
                Arrays.asList(2, 3),
                Arrays.asList(4, 5, 6)
        )
        val outputStream = inputStream.flatMap({ childList -> childList.stream() })
    }

    fun sum(a: Int, b: Int) = a + b

    @Test
    fun testStramMap() {
        a = 4;
        println("a=$a")
    }

    @Test
    fun testStream() {
        val setTea: Set<Int> = setOf<Int>(1, 2, 3);
        val list: List<Int> = listOf<Int>(4, 5, 6)
        println(setTea.toString())
        print(list.toString())
    }

    @Test
    fun main() {
        //sampleStart
        var a = 1
        // 模板中的简单名称：
        val s1 = "a is $a"
        a = 2
        // 模板中的任意表达式：
        val s2 = "${s1.replace("is", "was")}, but now is $a"
        //sampleEnd
        println(s2)
    }


    @Test
    fun testNumber() {
        print(NumberUtils.isNumber("500242198005481547"))
    }

    @Test
    fun testIn() {

        val list = listOf<String>("1", "2")
        for (index in list.indices) {
            println(index);
        }
        if (list.size !in list.indices) {
            print("aaaaaaaaaaaaaaaaaaa")
        }
    }

    @Test
    fun testString() {
        val text1 = """
            for (c in "foo")
                print(c)
        """
        val text = """
            >for (c in "foo")
            >    print(c)
        """.trimMargin(">")
        //print(text1)
        println(text)
        val price = """
        >    ${'$'}9.99
        """.trimMargin(">")
        print(price)
    }

    @Test
    fun testClass(){
        val a = TestDTO("ZHANGX");
        val b = TestDTO(20, "12121",'1');
    }

}
