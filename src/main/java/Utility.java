import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.text.DecimalFormat;
import java.util.*;

public class Utility implements InvocationHandler {
    private   Boolean changed = true;
    // Имплементация интерфейса
    private final Object  targetObject;

    // Перечень методов с аннотацией  Mutator
    private final List<String> methodsMutatorList = new ArrayList<>();
    // Перечень методов с аннотацией  Cache
    private final List<String> methodsCacheList = new ArrayList<>();

   //Перечень методов с аннотацией  Cache( Map на случай, если в классе два метода  Cache)
    private final Map<String, Object> methodCaches = new HashMap<>();
    // Получить строку идентифицирующую метод (имя + пар-ры)

    // Метод Cache
    public static <T> T Cache(T  fr)  {
        Utility cacheInvocationHandler =  new Utility(fr);
        Class[] interfaces = fr.getClass().getInterfaces();
        T proxyObject = (T)Proxy.newProxyInstance(Fraction.class.getClassLoader(), new Class[] {interfaces[0]} , cacheInvocationHandler);
        return proxyObject;
    }

    private String methodStr(Method method) {

        return method.getName() + Arrays.toString(method.getParameterTypes());
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object  resultOfInvocation      = null;                 // результат выполнения
        String  strMethod               = methodStr(method);    // строка идентифицирующая метод
        Boolean isMethodMutator         = false;                // метод явл-ся мутатором
        Boolean isMethodCache           = false;                // метод с аннотацией Cache


        // Определяем был ли  вызван метод, аннотируемый CACHE
        for (String s : methodsCacheList) {
            if (strMethod.equals(s)) {
                isMethodCache = true;
            }
        }
        // Определяем был ли  вызван метод, аннотируемый MUTATOR
        for (String s : methodsMutatorList) {
            if (strMethod.equals(s)) {
                isMethodMutator = true;
            }
        }
        // Если вызван метод  с аннот. CACHE
        if (isMethodCache == true) {
            if (methodCaches.containsKey(strMethod) == false || changed == true) {
                // Если метод  с аннот. CACHE  был вызван первый раз или значение было изменено методом с аннотацией Mutator
                resultOfInvocation = method.invoke(targetObject, args);
                // Фиксируем инфо о вычисленном значении
                methodCaches.put(strMethod, resultOfInvocation);
                System.out.println("Значение вычислено = " +  (double)resultOfInvocation);
            }
            else
            {
                // Получаем сохраненное значение
                resultOfInvocation =  methodCaches.get(strMethod);
                System.out.println("Вызов из кэша = " + (double)resultOfInvocation);
            }
            changed = false;
        } else {
            // Во всех остальных случаях не вмешиваемся в работу
            if (isMethodMutator == true) {
                changed = true;
                System.out.println("Вызов метода , помеченного аннотацией Mutator ");
            }
            // Вычисление значения
            resultOfInvocation = method.invoke(targetObject, args);
        }
        return resultOfInvocation;

    }


    public Utility(Object targetObject) {
        this.targetObject = targetObject;
        // Добавлено сохранение инф о методах CACHE и MUTATOR(не хотелось бы  каждый раз дергать  методы рефлексии)
        for(Method method : targetObject.getClass().getDeclaredMethods()) {
            // Найдем методы анноттированные   CACHE
            Annotation annotationCache = method.getAnnotation(Cache.class);
            if (annotationCache != null  && annotationCache instanceof Cache) {
                methodsCacheList.add(methodStr(method));
            }
            // Найдем методы анноттированные   MUTATOR
            Annotation annotationMutator = method.getAnnotation(Mutator.class);
            if (annotationMutator != null  && annotationMutator instanceof Mutator) {
                methodsMutatorList.add(methodStr(method));
            }
        }

    }



}
