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

enum Setting{
	INDOOR,OUTDOOR
}
class BigStage{}

class ConcerHall{
	@Inject @Named("capacity")
	private Integer capacity;
	
	@Inject @Named("stage")
	private Class<?> stageType;
	
	@Inject @Named("setting")
	private Setting setting;
	
	
	@Override
	public String toString() {
		return String.format("%s[capacity=%s,stageType=%s,setting=%s]",getClass().getName(),capacity,stageType,setting);
	}
}

class ConcerModule extends AbstractModule{
	@Override
	protected void configure() {
		//bind constant
		//Listing 3-12. ConcertExample Modified to Use String 
		bindConstant().annotatedWith(Names.named("capacity")).to("322");;
		
		bindConstant().annotatedWith(Names.named("stage")).to("chapter3.BigStage");
		
		bindConstant().annotatedWith(Names.named("setting")).to("INDOOR");
	}
}