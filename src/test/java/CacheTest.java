import jdk.jfr.Description;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CacheTest {
    @Test
    @Description("Тестирование значений в кэше")
    void DoubleValue() {
        Fractionable numProxy = Utility.Cache(new Fraction(10,2));
        // Проверяем для первого метода @Cache(их м.б. несколько)
        Assertions.assertEquals(5, numProxy.doubleValue());
        numProxy.doubleValue(); // молчит
        numProxy.doubleValue(); // молчит
        numProxy.setNum(20);
        Assertions.assertEquals(10, numProxy.doubleValue());
        numProxy.doubleValue(); // сработал
        Assertions.assertEquals(10, numProxy.doubleValue());
        numProxy.doubleValue(); // молчит
        Assertions.assertEquals(10, numProxy.doubleValue());

        // В реализацию для тестирования добавили метод setNumForTest НЕ ПОМЕЧЕННЫЙ аннотацией
        // , т.к. считаем, что только метод, помеченный аннот. Mutator изменяет значение
        numProxy.setNumForTest(2);
        // если из кэша, то получим 10 иначе 1
        Assertions.assertNotEquals(1, numProxy.doubleValue());


        // Проверяем для след. метода @Cache(их м.б. несколько)
        numProxy.setNum(2);
        numProxy.setDenum(4);
        Assertions.assertEquals(8, numProxy.multiplyValue());
        numProxy.doubleValue(); // молчит
        Assertions.assertEquals(8, numProxy.multiplyValue());
        numProxy.doubleValue(); // молчит
        Assertions.assertEquals(8, numProxy.multiplyValue());
        numProxy.setNum(4);
        Assertions.assertEquals(16, numProxy.multiplyValue());
        numProxy.doubleValue(); // сработал
        Assertions.assertEquals(16, numProxy.multiplyValue());
        numProxy.doubleValue(); // молчит
        Assertions.assertEquals(16, numProxy.multiplyValue());

        // В реализацию для тестирования добавили метод НЕ ПОМЕЧЕННЫЙ аннотацией, т.к. считаем, что только метод, помеченный аннот. Mutator изменяет значение
        numProxy.setNumForTest(5);
       Assertions.assertNotEquals(20, numProxy.multiplyValue());











    }


}
