package chapter3;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Provider;
import com.google.inject.Scope;

public class DefaultScope {

	public static void main(String[] args) {
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
