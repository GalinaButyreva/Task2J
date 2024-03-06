import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;

public class CacheInvocationHandler implements InvocationHandler {
    private   Boolean changed = true;
    // Имплементация интерфейса
    private final Object  targetObject;

    // Перечень методов с аннотацией  Mutator
    private final List<Method> methodsMutatorList = new ArrayList<>();
    // Перечень методов с аннотацией  Cache
    private final List<Method> methodsCacheList = new ArrayList<>();
   //Перечень методов с аннотацией  Cache( Map на случай, если в классе два метода  Cache)
    private final Map<Method, Object> methodCaches = new HashMap<>();
    // Сравнить на  идентичность методы (имя + пар-ры) (сравнение ссылок  не работает)
    private boolean methodCmp(Method method1, Method method2) {
        if (method1.getName().equals(method2.getName()) &&
                Arrays.toString(method1.getParameterTypes()).equals(Arrays.toString(method2.getParameterTypes())))
                return true;
        return false;
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object  resultOfInvocation      = null;                 // результат выполнения
        Boolean isMethodMutator         = false;                // метод явл-ся мутатором
        Boolean isMethodCache           = false;                // метод с аннотацией Cache

       // Определяем был ли  вызван метод, аннотируемый CACHE
        for (Method m : methodsCacheList) {
            if (methodCmp(m, method))
                isMethodCache = true;
        }

        // Определяем был ли  вызван метод, аннотируемый MUTATOR
        for (Method m : methodsMutatorList) {
            if (methodCmp(m, method))
                isMethodMutator = true;
        }

        // Если вызван метод  с аннот. CACHE
        if (isMethodCache) {
            if (methodCaches.containsKey(method) == false || changed == true) {
                // Если метод  с аннот. CACHE  был вызван первый раз или значение было изменено методом с аннотацией Mutator
                resultOfInvocation = method.invoke(targetObject, args);
                // Фиксируем инфо о вычисленном значении
                methodCaches.put(method, resultOfInvocation);
                System.out.println("Значение вычислено = " +  (double)resultOfInvocation);
            }
            else
            {
                // Получаем сохраненное значение
                resultOfInvocation =  methodCaches.get(method);
                System.out.println("Вызов из кэша = " + (double)resultOfInvocation);
            }
            changed = false;
        } else {
            // Во всех остальных случаях не вмешиваемся в работу
            if (isMethodMutator) {
                changed = true;
                System.out.println("Вызов метода , помеченного аннотацией Mutator ");
            }
            // Вычисление значения
            resultOfInvocation = method.invoke(targetObject, args);
        }
        return resultOfInvocation;
    }

    public CacheInvocationHandler(Object targetObject) {
        this.targetObject = targetObject;
        // Добавлено сохранение инф о методах CACHE и MUTATOR(не хотелось бы  каждый раз дергать  методы рефлексии)
        for(Method method : targetObject.getClass().getDeclaredMethods()) {
            // Найдем методы анноттированные   CACHE
            Annotation annotationCache = method.getAnnotation(Cache.class);
            if (annotationCache != null) {
                methodsCacheList.add(method);
            }
            // Найдем методы анноттированные   MUTATOR
            Annotation annotationMutator = method.getAnnotation(Mutator.class);
            if (annotationMutator != null) {
                methodsMutatorList.add(method);
            }
        }

    }



}
