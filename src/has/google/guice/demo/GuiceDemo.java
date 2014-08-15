package has.google.guice.demo;

import has.google.guice.demo.CommonAnnotation.Bad;
import has.google.guice.demo.CommonAnnotation.Good;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.inject.Named;

import com.google.inject.AbstractModule;
import com.google.inject.Binder;
import com.google.inject.BindingAnnotation;
import com.google.inject.Guice;
import com.google.inject.ImplementedBy;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.inject.name.Names;

public class GuiceDemo {

	public static void main(String[] args) {
		Injector injector = Guice.createInjector(new AddModule());
		// injector.getInstance(Add.class) 将会创建并返回一个 SimpleAdd 类型的实例
		// 实际上是通过 AddModule.configure() 方法来获取具体的绑定信息的。
		Add add = injector.getInstance(Add.class);
		//
		System.out.println(add.add(100, 540));

		// ----
		System.out.println("----------");

		Injector injector2 = Guice.createInjector(new AddModuleByProvider());
		add = injector2.getInstance(Add.class);
		System.out.println(add.add(1, 2));
		// ------
		// Client c = new Client(service);

		// ---
		Injector injector3 = Guice.createInjector();
		Person person = injector3.getInstance(Person.class);
		person.displayInfo();
		
		//----
		PlayerModule module = new PlayerModule();
		Injector injector4 = Guice.createInjector(module);
		
		//使用Binding 注释
		@Good Player player = (Player)injector4.getInstance(Player.class);
		player.bat();
		player.bowl();
//		@Bad
//		Player player2 =(Player)injector4.getInstance(Player.class);
//		player2.bat();
//		player2.bowl();
		//-------------
		
		//PlayerModuleByNames module2 = new PlayerModuleByNames();
		//Injector injector5 = Guice.createInjector(module2);
		
		ExplicitModule explicitModule = new ExplicitModule();
		Injector injector5 = Guice.createInjector(explicitModule);
		
		
//		@Named("Good") 
//		Player player2 = (Player)injector5.getInstance(Player.class);
//		player2.bat();
//		player2.bowl();
//		@Named("Bad") 
		Player player3 =(Player)injector5.getInstance(Player.class);
		System.out.println("-----------");
		player3.bat();
		player3.bowl();
		System.out.println("-----------");
		
		//--------------
		Injector injector6 = Guice.createInjector(new ConnectionModule());
		MockConnection connection = injector6.getInstance(MockConnection.class);
		connection.connect();
		connection.disConnect();
	}
}

// 定义接口,并实现接口
interface Add {
	public int add(int a, int b);
}

class SimpleAdd implements Add {

	@Override
	public int add(int a, int b) {
		System.out.println("Simple add");
		return a + b;
	}

}

// 定义Module类
class AddModule implements Module {

	// 将一些 Bindings 配置到某个 Module中
	@Override
	public void configure(Binder binder) {
		// 在下面的代码中，我们告诉 Guice 将 SimpleAdd 实现类绑定到 Add 接口上，
		// 也就是说在客户端调用Add.add() 方法时，实际会去执行 SimpleAdd.add() 方法
		binder.bind(Add.class).to(SimpleAdd.class);
		System.out.println("Module configure");

	}

}

//
class AddModuleByProvider implements Module {
	@Override
	public void configure(Binder binder) {
		binder.bind(Add.class).toProvider(AddProvider.class);
	}
}

class ComplexAdd implements Add {

	@Override
	public int add(int a, int b) {
		return a + b;
	}

}

// provider 相当于传统的工厂模式
// 需要定制化一个对象创建流程,使用Providers
class AddProvider implements Provider<Add> {

	@Override
	public Add get() {
		System.out.println("进行复仇的创建对象流程");
		return new ComplexAdd();
	}

}

// about module
class MyModule extends AbstractModule {

	@Override
	protected void configure() {
		// TODO Auto-generated method stub

	}

}

class Client {
	// 我们是基于构造方法层次 （Constrcctor-level）的 注入，
	// 并且假设 MyService 接口的具体实现已经在应用程序的 Module 中定义映射好了
	@Inject
	public Client(MyService service) {

	}
}

interface IService {
	void say(String s);
}

class MyService implements IService {

	@Override
	public void say(String s) {
		System.out.println("say :" + s);
	}

}

class MyServiceModule implements Module {

	@Override
	public void configure(Binder binder) {
		binder.bind(IService.class).to(MyService.class);
	}

}

// -----
// 处理多个依赖 （Multiple Dependencies）
// 这一小节里面，我们将探讨如何是用 @Inject 注释来处理多个依赖。
// 比方说有一个对象直接依赖其它两个或者多个对象。这里我们创建一个简单的 Case ，一个人有一台笔记和一个手机。
class Laptop {
	private String model;
	private String price;

	public Laptop() {// 使用@Inject时,默认调用无参构造函数
		this.model = "lenovo";
		this.price = "$123456789";
	}

	@Override
	public String toString() {
		return "Laptop [model=" + model + ", price=" + price + "]";
	}

}

//
class Mobile {
	private String number;

	public Mobile() {// 使用@Inject时,默认调用无参构造函数
		this.number = "15976543860";
	}

	@Override
	public String toString() {
		return "Mobile [number=" + number + "]";
	}

}

// 接下来我们将会在 Person 类中使用 @Inject 注释来直接引用 Laptop 和 Mobile 对象。注意我们这儿使用的是构造方法层次上的注入
class Person {
	private Mobile mobile;
	private Laptop laptop;

	@Inject
	public Person(Mobile mobile, Laptop laptop) {
		this.mobile = mobile;
		this.laptop = laptop;
	}

	public void displayInfo() {
		System.out.println("Mobile:" + mobile);
		System.out.println("Laptop:" + laptop);
	}

}

// --------
// 使用 Binding 注释
// 在 Guice 中，一个类型不能绑定多个实现，如下，代码会抛 Runtime Error.

// binderObject.bind(SomeType.class).to(ImplemenationOne.class);
// binderObject.bind(SomeType.class).to(ImplemenationTwo.class);
// 由于 Guice 并不知道客户端究竟要绑定哪一个实现类，因此抛出了异常。但是在类似 Java 的语言中，
// 一个类可以实现多个接口，基于这个思想，Guice 提供了一种依赖 Binding 注释的方式来实现一个类型绑定多个实现。
// 例如，接口 Player 定义如下，

@ImplementedBy(GoodPlayer.class)
interface Player {
	void bat();

	void bowl();
}

// 接着我们提供了 Player 的两种实现类， GoodPlayer 和 BadPlayer。
class GoodPlayer implements Player {

	@Override
	public void bat() {
		System.out.println("I can hit any ball");
	}

	@Override
	public void bowl() {
		System.out.println("I can also bowl");

	}

}
class BadPlayer implements Player {

	@Override
	public void bat() {
		System.out.println("I think i can face the ball");
	}

	@Override
	public void bowl() {
		System.out.println("I don't know bowling");

	}

}
//通过一些注释机制 （Annotaion mechanisms） 我们可以指示 Guice 使用不同的实现
class PlayerModule implements Module{

	@Override
	public void configure(Binder binder) {
		//我们分别使用了.annotatedWith(Good.class) 和 .annotatedWith(Bad.class)， 
		//这两处代码指明了如果使用Good注释，那么就绑定GoodPlayer实现类，如果使用了Bad注释，那么就绑定BadPlayer实现类
		binder.bind(Player.class).annotatedWith(Good.class).to(GoodPlayer.class);
		binder.bind(Player.class).annotatedWith(Bad.class).to(BadPlayer.class);
	}
	
}
//Named注释
class PlayerModuleByNames implements Module{

	@Override
	public void configure(Binder binder) {
		//像上面例子中，如果只是为了标记实现类以便于客户端使用，而为每一个实现类创建新的 Annotation ，那么是完全没有必要的。我们可以使用 @Named 注释来命名这些 entities。这儿有一个工具方法 － Names.named() ，当你给它一个命名，它会返回好一个命名好的 Annotation。
		//例如上面的例子中，在 Player Module 中可以使用 Names.named() 来完成一些相同的事情。
		binder.bind(Player.class).annotatedWith(Names.named("Good")).to(GoodPlayer.class);
		binder.bind(Player.class).annotatedWith(Names.named("Bad")).to(BadPlayer.class);
	}
	
}
//AbstractModule
class ExplicitModule extends AbstractModule{
	@Override
	protected void configure() {
		bind(BadPlayer.class);
	}
}
//Eager Singleton Loading
class EagerSingletonModule extends AbstractModule{
	@Override
	protected void configure() {
		//方式一,不推荐
		//bind(Player.class).to(BadPlayer.class).in(Singleton.class);
		//Eager Singleton Loading,提交加载单例
		bind(Player.class).to(BadPlayer.class).asEagerSingleton();
		//
	}
}
//我们使用了两个自定义的 Annotation，Good 和 Bad。下面我们给出 Good annotation 和 Bad annotation 的代码。
class CommonAnnotation{
	
	@Retention(RetentionPolicy.RUNTIME)
	@BindingAnnotation
	@Target(ElementType.LOCAL_VARIABLE)
	public @interface Good{}
	
	@Retention(RetentionPolicy.RUNTIME)
	@BindingAnnotation
	@Target(ElementType.LOCAL_VARIABLE)
	public @interface Bad{}
	
}

//一个简单的 Provider
//在 Guice 中 Providers 就像 Factories 一样创建和返回对象。
//在大部分情况下，客户端可以直接依赖 Guice 框架来为服务（Services）创建依赖的对象。
//但是少数情况下，应用程序代码需要为一个特定的类型定制对象创建流程（Object creation process），
//这样可以控制对象创建的数量，提供缓存（Cache）机制等，这样的话我们就要依赖 Guice 的 Provider 类。
class MockConnection{
	public void connect(){
		System.out.println("Connecting to the mock database");
	}
	public void disConnect(){
		System.out.println("Dis-connecting from the mock database");
	}
}
//现在我们来写一个简单的 Provider 类来实现 Guice 的 Provider 接口，使用它创建并返 MockConnection对象，代码如下
class ConnectionProvider implements Provider<MockConnection>{

	@Override
	public MockConnection get() {
		MockConnection connection = new MockConnection();
		return connection;
	}
	
}
class ConnectionModule implements Module{

	@Override
	public void configure(Binder binder) {
		//注意第10行，我们使用 toProvider() 方法将 MockConnection.class 绑定到一个 Provider 上。
		binder.bind(MockConnection.class).toProvider(ConnectionProvider.class);
	}
	
}