package fishlikewater.kotlindemo

sealed class scale

object op1: scale()

object op2: scale()

class op3: scale()


fun test(op: scale){
    when(op){
        op1 -> print("op1")
        op2 -> print("op2")
        is op3 -> print("op3")
    }

}

fun main(args: Array<String>) {
    test(op1);
}