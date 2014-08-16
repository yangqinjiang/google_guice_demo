package chapter4;

import java.util.HashMap;
import java.util.Map;






import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

import static com.google.inject.matcher.Matchers.*;
/**
 * 引用guice-3.0.jar,因为它有aop包
 * 还要引用aopalliance.jar
 *
 */
public class PhoningHome {

	public static void main(String[] args) {
		Injector i = Guice.createInjector(new PhoneModule());
		Phone phone = i.getInstance(Phone.class);
		Receiver auntJane = phone.call(123456789);
		System.out.println(auntJane);
	}

}
class Phone{
	private static final Map<Number,Receiver> RECEIVERS=new HashMap<Number,Receiver>();
	static {
		RECEIVERS.put(123456789, new Receiver("Aunt Jane"));
	}
	public Receiver call(Number number){
		return RECEIVERS.get(number);
	}
}

class Receiver{
	private final String name;
	public Receiver(String name){
		this.name=name;
	}
	@Override
	public String toString() {
		return String.format("%s[name=%s]", getClass().getName(),name);
	}
}
//The Phone Company’s Installation
class PhoneModule extends AbstractModule{
	@Override
	protected void configure() {
		
		bindInterceptor(
				subclassesOf(Phone.class),
				returns(only(Receiver.class)),
				new PhoneLoggerInterceptor());
	}
}
class PhoneLoggerInterceptor implements MethodInterceptor{

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		//遍历被调用的方法的参数,这里是Receiver call(Number number) 里的number
		for(Object arg:invocation.getArguments()){
			if(arg instanceof Number){//判断参数是否是Number类型
				System.out.println("CALL: "+arg);
			}
		}
		return invocation.proceed();//继续执行
	}
	
}