# Исключения (exceptions), утверждения (assertions)
* что происходит, когда выполнение функции приводит к неожиданному состоянию?
* происходит исключение... Относительно того, что ожидалось
  * попытка обращения за пределы списка: `listOf(1, 7, 4)[4] // -> ArrayIndexOutOfBoundsException`
  * попытка приведения к несовместимому типу: `listOf(1, 7, 4) as Int // -> ClassCastException`
  * попытка получить число из строки, содержащей посторонние символы: `"12q".toInt() // -> NumberFormatException`
  * попытка чтения из несуществующего файла: `FileInputStream("foo.txt") // -> FileNotFoundException`
  * попытка получить значение из `null`: `null!! // -> KotlinNullPointerException`
  * попытка поделить на 0: `1 / 0 // -> ArithmeticException`
  * и другие (тысячи их)
# Что делать с исключительными ситуациями?
* что должна делать функция, если входные данные не корректны?
  * падать тихо:
    * подставляем некоторые значения по умолчанию и продолжаем исполнение
    * плохая идея! Мы не получим сигнал, что что-то пошло не так
  * возвращать специальное значение, сигнализирующее об ошибке:
    * какое значение выбрать?
    * усложнение кода, мы должны проверять специальные значения
    * на самом деле, такой способ зачастую лучше других вариантов, но это выходит за рамки данного курса
  * прервать исполнение и сигнализировать об ошибке
    * в Kotlin: бросить исключение: `throw IllegalArgumentException("описание причин ошибки")`
# Обработка исключений
Kotlin может обрабатывать исключения, брошенные из кода:
#### `try`/`catch`/`finally`:
```kotlin
    try {
        print("Введите числитель: ")
        val a = readLine()!!.toInt()
        print("Введите знаменатель: ")
        val b = readLine()!!.toInt()
        println(a / b)
        println("Успешно")
    } catch (e: Throwable) {
        println("Неверный ввод")
        throw e
    } finally {
        println("В любом случае, спасибо")
    }
    println("До свидания")
```
* исключения, брошенные из любой части тела `try` блока обрабатываются `catch` блоком, затем выполняется `finally` блок независимо от того, было брошено исключение или нет 
#### `runCatching`:
```kotlin
    runCatching {
        print("Введите числитель: ")
        val a = readLine()!!.toInt()
        print("Введите знаменатель: ")
        val b = readLine()!!.toInt()
        println(a / b)
    }.onSuccess { println("Успешно") }.onFailure { println("Неверный ввод") }.getOrThrow()
    println("До свидания")
```
* похоже на `try`/`catch`/`finally`, но имеет свои преимущества и недостатки:
  * +позволяет сохранить результат исполнения блока `runCatching`, а обработку провести позже
  * -нет блока, аналогичного `finally`, который будет всегда исполняться
# Обработка специфических исключений
#### `try`/`catch`:
```kotlin
    try {
        print("Введите числитель: ")
        val a = readLine()!!.toInt()
        print("Введите знаменатель: ")
        val b = readLine()!!.toInt()
        println(a / b)
        println("Успешно")
    } catch (e: NumberFormatException) {
        println("Не могу преобразовать в число")
    } catch (e: ArithmeticException) {
        println("Не могу поделить на 0")
    } catch (e: Throwable) {
        println("Что-то пошло совсем не так")
    }
```
#### `runCatching`:
```kotlin
    runCatching {
        print("Введите числитель: ")
        val a = readLine()!!.toInt()
        print("Введите знаменатель: ")
        val b = readLine()!!.toInt()
        println(a / b)
    }.fold({ println("Успешно") }) {
        when (it) {
            is NumberFormatException -> println("Не могу преобразовать в число")
            is ArithmeticException -> println("Не могу поделить на 0")
            else -> println("Что-то пошло совсем не так")
        }
    }
```
# Пример использования исключений
## проверка ввода пользователя
#### `try`/`catch`:
```kotlin
    var n: Int
    while (true) try {
        n = readLine()!!.toInt()
        break // из цикла выходим только если дошли сюда без исключений
    } catch (e: NumberFormatException) {
        println("Введено не целое число; попробуйте ещё раз") // печатаем сообщение только если поймали исключение
    }
    println("Введено число $n")
```
#### `runCatching`:
```kotlin
    var n: Int by Delegates.notNull()
    while (true) if (
        runCatching { n = readLine()!!.toInt() }.onFailure {
            println("Введено не целое число; попробуйте ещё раз")
        }.isSuccess
    ) break
    println("Введено число $n")
```
# Утверждения
* хотим быть уверены, что наши предположения относительно входных данных верны
* используй `require` или `check`, чтобы бросить исключение, если предположения оказались не верны
* относятся к техникам безопасного программирования
## Пример
#### `require` - бросает `IllegalArgumentException`
```kotlin
    print("Введите натуральное число: ")
    val n = readLine()!!.toInt()
    require(n > 0) { "Ожидалось натуральное число, но введено $n" } // IllegalArgumentException
```
#### `check` - бросает `IllegalStateException`
```kotlin
    print("Введите натуральное число: ")
    val n = readLine()!!.toInt()
    check(n > 0) { "Ожидалось натуральное число, но введено $n" } // IllegalStateException
```
# Утверждения как техника безопасного программирования
* не дают программисту контроль над ответом на нештатные ситуации
* останавливают исполнение кода как только возникла нештатная ситуация
* могут использоваться для проверки ввода и не только
* могут использоваться для проверки результата функции, чтобы избежать дальнейшего исполнения, если он не корректный
* облегчают поиск ошибок
# Где использовать утверждения
* цель - упасть при возникновении нештатной ситуации как можно раньше, чтобы было ясно видно, где она возникла
* используются в тестах
* для проверки входных данных
* для проверки
  * соблюдения спецификаций входных данных
  * корректности результатов