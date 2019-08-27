package course.lecture4

fun main() {
    var x = 100500
    fun f() {
        var x = 1/*переменная x переопределена в области видимости f, переменная x из области видимости main не изменится*/
        println("В области видимости f: x = $x")
        x++
        println("В области видимости f: x = $x")
    }

    fun g() {
        println("В области видимости g: x = $x")/*переменная x берётся из области видимости main*/
        println("В области видимости g: x + 1 = ${x + 1}")
    }

    fun h() {
        println("В области видимости h: x = $x")
        x++/*переменная x берётся из области видимости main и изменяется*/
        println("В области видимости h: x = $x")
    }

    x = 5
    println("вызов f:\nВ области видимости main: x = $x")
    f()
    println("В области видимости main: x = $x")

    x = 5
    println("\nвызов g:\nВ области видимости main: x = $x")
    g()
    println("В области видимости main: x = $x")

    x = 5
    println("\nвызов h:\nВ области видимости main: x = $x")
    h()
    println("В области видимости main: x = $x")
}