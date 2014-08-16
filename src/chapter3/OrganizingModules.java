package chapter3;

import chapter3.Anno.DefaultScoped;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class OrganizingModules {

	public static void main(String[] args) {
		//separateModule();
		binderinstall();
	}
	

	private static void binderinstall() {
		Injector i=Guice.createInjector(new BindingsModule());
		i.getInstance(Person.class);
		i.getInstance(Person.class);
	}


	private static void separateModule() {
		//
		Injector i=Guice.createInjector(new DefaultScopeModule(),//注意顺序,BindingsModule依赖DefaultScopeModule
										new BindingsModule());
		i.getInstance(Person.class);
		i.getInstance(Person.class);
	}

}
//Listing 3-26. Scope Registration in a Separate Module 
//在不同的module里完成scope的注册
class DefaultScopeModule extends AbstractModule{

	@Override
	protected void configure() {
		bindScope(DefaultScoped.class, CustomScopes.DEFAULT);
	}
	
}
class BindingsModule extends AbstractModule{

	@Override
	protected void configure() {
		//自己绑定依赖项
		install(new DefaultScopeModule());
		bind(Person.class).in(DefaultScoped.class);
	}
	
}