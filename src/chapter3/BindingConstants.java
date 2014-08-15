package chapter3;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.name.Named;
import com.google.inject.name.Names;

public class BindingConstants {

	public static void main(String[] args) {
		Injector i = Guice.createInjector(new ConcerModule());
		ConcerHall hall = i.getInstance(ConcerHall.class);
		System.out.println(hall);
	}

}
// Binding Constants
//bindConstant().annotatedWith(…).to(…); 

class ConcerHall{
	@Inject @Named("capacity")
	private int capacity;
	
	@Override
	public String toString() {
		return String.format("%s[capacity=%s]",getClass().getName(),capacity);
	}
}

class ConcerModule extends AbstractModule{
	@Override
	protected void configure() {
		//bind constant
		bindConstant().annotatedWith(Names.named("capacity")).to(322);;
	}
}