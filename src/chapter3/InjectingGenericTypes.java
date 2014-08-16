package chapter3;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
/**
 * 注入泛型
 *
 */
public class InjectingGenericTypes {

	public static void main(String[] args) {
		Injector i = Guice.createInjector(new TypeLiteralModule());
		System.out.println(i.getInstance(ListUser.class));
	}

}

class ListUser{
	@Inject @Named("list") List<String> strings;
	@Inject @Named("list") List<Integer> integers;
	@Override
	public String toString() {
		return String.format("%s[strings=%s, integers=%s]",
				getClass().getName(),
				System.identityHashCode(strings),
				System.identityHashCode(integers)
				);
	}
}
//
class TypeLiteralModule extends AbstractModule{

	@Override
	protected void configure() {
		//list String
		bind(new TypeLiteral<List<String>>(){})
			.annotatedWith(Names.named("list"))
			.to(new TypeLiteral<ArrayList<String>>(){});
		//list Integer
		bind(new TypeLiteral<List<Integer>>(){})
		.annotatedWith(Names.named("list"))
		.to(new TypeLiteral<ArrayList<Integer>>(){});
	}
	
}
