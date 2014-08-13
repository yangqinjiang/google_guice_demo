package has.google.guice.demo;

import com.google.inject.AbstractModule;
import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Provider;

public class GuiceDemo {

	public static void main(String[] args) {
		Injector injector = Guice.createInjector(new AddModule());
		//injector.getInstance(Add.class) 将会创建并返回一个 SimpleAdd 类型的实例
		//实际上是通过 AddModule.configure() 方法来获取具体的绑定信息的。
		Add add = injector.getInstance(Add.class);
		//
		System.out.println(add.add(100,540));
		
		//----
		System.out.println("----------");
		
		Injector injector2 = Guice.createInjector(new AddModuleByProvider());
		add = injector2.getInstance(Add.class);
		System.out.println(add.add(1, 2));
	}
}
//定义接口,并实现接口
interface Add{
	public int add(int a,int b);
}
class SimpleAdd implements Add{

	@Override
	public int add(int a, int b) {
		System.out.println("Simple add");
		return a+b;
	}
	
}
//定义Module类
class AddModule implements Module{

	//将一些 Bindings 配置到某个 Module中
	@Override
	public void configure(Binder binder) {
		//在下面的代码中，我们告诉 Guice 将 SimpleAdd 实现类绑定到 Add 接口上，
		//也就是说在客户端调用Add.add() 方法时，实际会去执行 SimpleAdd.add() 方法
		binder.bind(Add.class).to(SimpleAdd.class);
		System.out.println("Module configure");
		
	}
	
}
//
class AddModuleByProvider implements Module{
	@Override
	public void configure(Binder binder) {
		binder.bind(Add.class).toProvider(AddProvider.class);
	}
}

class ComplexAdd implements Add{

	@Override
	public int add(int a, int b) {
		return a+b;
	}
	
}
//provider 相当于传统的工厂模式
//需要定制化一个对象创建流程,使用Providers
class AddProvider implements Provider<Add>{

	@Override
	public Add get() {
		System.out.println("进行复仇的创建对象流程");
		return new ComplexAdd();
	}
	
}

//about module
class MyModule extends AbstractModule{

	@Override
	protected void configure() {
		// TODO Auto-generated method stub
		
	}
	
	
}
