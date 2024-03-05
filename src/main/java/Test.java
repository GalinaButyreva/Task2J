import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;


public class Test {

    public static <T> Object Cache(T  fr)  {
        Utility cacheInvocationHandler =  new Utility(fr);
        Class[] interfaces = fr.getClass().getInterfaces();
        Object proxyObject = Proxy.newProxyInstance(Fraction.class.getClassLoader(), new Class[] {interfaces[0]} , cacheInvocationHandler);

        return proxyObject;
    }



    public static void main(String[] args) throws ClassNotFoundException {
       //List<String>  listProxy = Utility.Cache(new ArrayList<>()); -- проверяем для другого объекта

        Fractionable numProxy = Utility.Cache(new Fraction(2,3));
        numProxy.doubleValue(); // sout сработал
        numProxy.doubleValue(); // молчит
        numProxy.doubleValue(); // молчит
        numProxy.setNum(5);
        numProxy.doubleValue(); // сработал
        numProxy.doubleValue(); // молчит


        numProxy.setNum(7);
        numProxy.multiplyValue(); // sout сработал
        numProxy.multiplyValue(); // молчит
        numProxy.multiplyValue(); // молчит
        numProxy.setNum(3);
        numProxy.multiplyValue(); // сработал
        numProxy.multiplyValue(); // молчит


    }

}
