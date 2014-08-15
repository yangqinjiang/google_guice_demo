package chapter3;

import has.google.guice.AssertUtils;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;
import static has.google.guice.AssertUtils.*;
public class Providers {

	public static void main(String[] args) {
		Injector i = Guice.createInjector(new GumModule());
		GumballMachine m = i.getInstance(GumballMachine.class);
		assertTrue(m.dispense() != m.dispense());
		System.out.println(m.dispense());
		System.out.println(m.dispense());
	}

}

class Gum{}
class GumballMachine{
	@Inject
	private Provider<Gum> gumProvider;
	
	public Gum dispense(){
		return gumProvider.get();
	}
}

class GumProvider implements Provider<Gum>{

	@Override
	public Gum get() {
		
		return new Gum();
	}
	
}
class GumModule extends AbstractModule{

	@Override
	protected void configure() {
		bind(Gum.class).toProvider(GumProvider.class);
		
	}
	
}
