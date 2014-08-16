package chapter3;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.google.inject.name.Names;

/**
 * 静态注入
 *
 */
public class UsingStaticInjection {

	public static void main(String[] args) {

		Guice.createInjector(new StaticModule());
	}
	
	@Inject
	public static void staticMethod(@Named("s") String str){
		System.out.println(str);
	}
	
	@Inject
	public static void staticMethod1(@Named("d") String str){
		System.out.println("call static method1");
		System.out.println(str);
	}
	
	@Inject
	public static void staticMethod2(String str){
		System.out.println("call static method2");
		System.out.println(str);//打印空字符
	}

}

class StaticModule extends AbstractModule{
	@Override
	protected void configure() {
		bindConstant().annotatedWith(Names.named("s")).to("D' OH!");
		bindConstant().annotatedWith(Names.named("d")).to("Zzzzzzz");
		//
		requestStaticInjection(UsingStaticInjection.class);
	}
}
