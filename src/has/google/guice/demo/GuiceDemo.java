package has.google.guice.demo;

import com.google.inject.AbstractModule;
import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Inject;
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
		//------
		//Client c = new Client(service);
		
		//---
		Injector injector3 = Guice.createInjector();
		Person person = injector3.getInstance(Person.class);
		person.displayInfo();
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

class Client {
	//我们是基于构造方法层次 （Constrcctor-level）的 注入，
	//并且假设 MyService 接口的具体实现已经在应用程序的 Module 中定义映射好了
	@Inject
	public Client(MyService service){
		
	}
}
interface IService{
	void say(String s);
}
class MyService implements IService{

	@Override
	public void say(String s) {
		System.out.println("say :"+s);
	}
	
}
class MyServiceModule implements Module{

	@Override
	public void configure(Binder binder) {
		binder.bind(IService.class).to(MyService.class);
	}
	
}

//-----
//处理多个依赖 （Multiple Dependencies）
//这一小节里面，我们将探讨如何是用 @Inject 注释来处理多个依赖。
//比方说有一个对象直接依赖其它两个或者多个对象。这里我们创建一个简单的 Case ，一个人有一台笔记和一个手机。
class Laptop{
	private String model;
	private String price;
	public Laptop(){
		this.model="lenovo";
		this.price="$123456789";
	}
	@Override
	public String toString() {
		return "Laptop [model=" + model + ", price=" + price + "]";
	}
	
}
//
class Mobile{
	private String number;
	public Mobile(){
		this.number="15976543860";
	}
	@Override
	public String toString() {
		return "Mobile [number=" + number + "]";
	}
	
}
//接下来我们将会在 Person 类中使用 @Inject 注释来直接引用 Laptop 和 Mobile 对象。注意我们这儿使用的是构造方法层次上的注入
class Person{
	private Mobile mobile;
	private Laptop laptop;
	@Inject
	public Person(Mobile mobile, Laptop laptop) {
		this.mobile = mobile;
		this.laptop = laptop;
	}
	public void displayInfo(){
		System.out.println("Mobile:"+mobile);
		System.out.println("Laptop:"+laptop);
	}
	
}
