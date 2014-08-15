package has.google.guice.demo;

import has.google.guice.demo.Anno.Mega;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.google.inject.AbstractModule;
import com.google.inject.Binder;
import com.google.inject.BindingAnnotation;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Scopes;
import com.google.inject.Stage;

import static has.google.guice.AssertUtils.*;

public class FortuneDemo {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		chefTest();
		chefTestWithFactory();
		//
//		bootstrapping1();
		bootstrapping2();
	}

	private static void bootstrapping1() {
		Injector i = Guice.createInjector(Stage.DEVELOPMENT, new ChefModule());
		Chef chef = i.getInstance(Chef.class);
		chef.makeFortuneCookie();

	}

	private static void bootstrapping2() {
		Injector i = Guice.createInjector(Stage.DEVELOPMENT,
				new BindingAnnotationModule());
		Chef chef = i.getInstance(Chef.class);
		chef.makeFortuneCookie();

	}

	private static void chefTestWithFactory() {
		FortuneServiceMock mock = new FortuneServiceMock();
		Chef chef = new Chef(mock);
		chef.makeFortuneCookie();
		assertTrue(mock.calledOnce());
	}

	private static void chefTest() {
		final FortuneService original = FortuneServiceFactory
				.getFortuneService();
		try {
			FortuneServiceMock mock = new FortuneServiceMock();
			FortuneServiceFactory.setFortuneService(mock);
			Chef chef = new Chef();
			chef.makeFortuneCookie();
			assertTrue(mock.calledOnce());
		} finally {
			FortuneServiceFactory.setFortuneService(original);
		}

	}

}

// ---------------
interface FortuneService {
	String randomForturn();
}

class ForturnServiceImpl implements FortuneService {
	private static final List<String> MESSAGES = Arrays.asList(
			"Today you will have some refreshing juice",
			"Larry just bought your company");

	@Override
	public String randomForturn() {
		// 随机获取其中一条数据
		return MESSAGES.get(new Random().nextInt(MESSAGES.size()));
	}
}

class FortuneServiceMock implements FortuneService {
	private int invocationCount;

	@Override
	public String randomForturn() {
		invocationCount++;
		return "MOCK";
	}

	public boolean calledOnce() {
		return invocationCount == 1;
	}

}

class MegaFortuneService implements FortuneService {
	private static final List<FortuneService> SERVICES = Arrays.asList(
			new FunnyFortuneService(), new QuoteFortuneService());

	@Override
	public String randomForturn() {
		return SERVICES.get(new Random().nextInt(SERVICES.size()))
				.randomForturn();
	}
}

// -----
class Chef {
	private FortuneService fortuneService;

	public Chef() {
		this.fortuneService = FortuneServiceFactory.getFortuneService();
	}

	// 摆脱构造工厂 Chef goes DI
	// Tagged with @Inject
	//Guice Style 使用Inject //选择MegaFortuneService
	@Inject
	public Chef(@Mega FortuneService fortuneService) {
		this.fortuneService = fortuneService;
	}

	public void makeFortuneCookie() {
		System.out.println("makeFortuneCookie");
		new FortuneCookie(fortuneService.randomForturn());
	}
}

//
class FortuneCookie {
	public FortuneCookie(String s) {
		// do sth.
	}
}

//
class FortuneServiceFactory {
	private FortuneServiceFactory() {
	}

	private static FortuneService fortuneService = new ForturnServiceImpl();

	// Static method calls are hard to test
	public static FortuneService getFortuneService() {
		return fortuneService;
	}

	public static void setFortuneService(FortuneService mockFortuneService) {
		fortuneService = mockFortuneService;
	}
}

// --------------------
//
// Google Guice Style
//
// ------------------------

class ChefModule implements Module {

	@Override
	public void configure(Binder binder) {
		// 读法: bind FortuneService to FortuneServiceImpl in Singleton Scope
		binder.bind(FortuneService.class).to(ForturnServiceImpl.class)//
				.in(Scopes.SINGLETON);// 单例
	}

}

class CommonSenseModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(FortuneService.class).to(ForturnServiceImpl.class);
		bind(FortuneService.class).to(MegaFortuneService.class);
	}
}
/**
 * A Module Using Binding Annotations
 *
 */
class BindingAnnotationModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(FortuneService.class).to(ForturnServiceImpl.class);
		bind(FortuneService.class).annotatedWith(Mega.class).to(
				MegaFortuneService.class);

	}
}


class Anno {

	@Retention(RetentionPolicy.RUNTIME)
	@Target({ ElementType.FIELD, ElementType.PARAMETER })
	@BindingAnnotation
	public @interface Mega {
	}
}
