package has.google.guice.demo;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import static has.google.guice.AssertUtils.*;

public class FortuneDemo {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		chefTest();
		chefTestWithFactory();
	}

	private static void chefTestWithFactory() {
		FortuneServiceMock mock = new FortuneServiceMock();
		Chef chef = new Chef(mock);
		chef.makeFortuneCookie();
		assertTrue(mock.calledOnce());
	}

	private static void chefTest() {
		final FortuneService original =
				FortuneServiceFactory.getFortuneService();
		try{
			FortuneServiceMock mock = new FortuneServiceMock();
			FortuneServiceFactory.setFortuneService(mock);
			Chef chef = new Chef();
			chef.makeFortuneCookie();
			assertTrue(mock.calledOnce());
		}finally{
			FortuneServiceFactory.setFortuneService(original);
		}
		
	}
	

}

//---------------
interface FortuneService{
	String randomForturn();
}
class ForturnServiceImpl implements FortuneService{
	private static final List<String> MESSAGES =Arrays.asList(
			"Today you will have some refreshing juice",
			"Larry just bought your company");

	@Override
	public String randomForturn() {
		//随机获取其中一条数据
		return MESSAGES.get(new Random().nextInt(MESSAGES.size()));
	}
}
class FortuneServiceMock implements FortuneService{
	private int invocationCount;

	@Override
	public String randomForturn() {
		invocationCount++;
		return "MOCK";
	}
	public boolean calledOnce(){
		return invocationCount==1;
	}
	
}
//-----
class Chef{
	private FortuneService fortuneService;
	public Chef(){
		this.fortuneService = FortuneServiceFactory.getFortuneService();
	}
	//摆脱构造工厂 Chef goes DI
	public Chef(FortuneService fortuneService){
		this.fortuneService = fortuneService;
	}
	public void makeFortuneCookie(){
		new FortuneCookie(fortuneService.randomForturn());
	}
}
//
class FortuneCookie{
	public FortuneCookie(String s){
		//do sth.
	}
}
//
class FortuneServiceFactory{
	private FortuneServiceFactory(){}
	
	private static FortuneService fortuneService = new ForturnServiceImpl();
	//Static method calls are hard to test
	public static FortuneService getFortuneService(){
		return fortuneService;
	}
	public static void setFortuneService(FortuneService  mockFortuneService){
		fortuneService = mockFortuneService;
	}
}
