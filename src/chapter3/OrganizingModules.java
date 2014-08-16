package chapter3;

import chapter3.Anno.DefaultScoped;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class OrganizingModules {

	public static void main(String[] args) {
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
		bind(Person.class).in(DefaultScoped.class);
	}
	
}