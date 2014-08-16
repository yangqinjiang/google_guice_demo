package chapter3;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import chapter3.Anno.DefaultScoped;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.Scope;
import com.google.inject.ScopeAnnotation;

public class DefaultScope {

	public static void main(String[] args) {
		bindingToAScope();
		bindingToAScopeAnnotation();
	}

	private static void bindingToAScopeAnnotation() {
		System.out.println("-----------");
		Injector i= Guice.createInjector(new CustomScopeByAnnotationModule());
		i.getInstance(Person.class);
		i.getInstance(Person.class);
	}

	private static void bindingToAScope() {
		Injector i= Guice.createInjector(new CustomScopeModule());
		i.getInstance(Person.class);
		i.getInstance(Person.class);
	}

}
class CustomScopes{
	public static final Scope DEFAULT = new Scope() {
		
		@Override
		public <T> Provider<T> scope(Key<T> key, Provider<T> creator) {
			System.out.println("Scoping "+key);
			
			return creator;
		}
		@Override
		public String toString() {
			return CustomScopes.class.getSimpleName()+".DEFAULT";
		}
	};
	//
	
}

@DefaultScoped
class Person{
	public Person(){
		System.out.printf("Hi,I'm a Person.With hashCode '%s' ,I'm unique!%n",super.hashCode());
	}
}

class CustomScopeModule extends AbstractModule{

	@Override
	protected void configure() {
		bind(Person.class).in(CustomScopes.DEFAULT);
		
	}
	
}
class Anno{
	@Target(ElementType.TYPE) 
	@Retention(RetentionPolicy.RUNTIME) 
	@ScopeAnnotation 
	public @interface DefaultScoped {}
}
class CustomScopeByAnnotationModule extends AbstractModule{
	@Override
	protected void configure() {
		bindScope(DefaultScoped.class, CustomScopes.DEFAULT);//绑定scope
		//bind(Person.class).in(DefaultScoped.class);//使用注解方式,就不需要这行代码了 @DefaultScoped
	}
}
