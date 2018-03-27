package fishlikewater.kotlindemo

class TestDTO(name: String){

    init {
        println("this init 1 $name");
    }
    init {
        println("this is init 2 $name")
    }

    constructor(age: Int, name: String): this(name){
        println("this is init 3 $age, $name");
    }
    constructor(age: Int, name: String, sex: Char): this(age, name){
        println("this is init 4 $age, $name,$sex");
    }
}