package has.google.guice.demo;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class FortuneDemo {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		chefTest();
	}

	private static void chefTest() {
		final FortuneService original =
				FortuneServiceFactory.getFortuneService();
		try{
			FortuneServiceMock mock = new FortuneServiceMock();
			FortuneServiceFactory.setFortuneService(mock);
			Chef chef = new Chef();
			chef.makeFortuneCookie();
			if(mock.calledOnce()){
				System.out.println("OKkkkkk");
			}
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
	
	public static FortuneService getFortuneService(){
		return fortuneService;
	}
	public static void setFortuneService(FortuneService  mockFortuneService){
		fortuneService = mockFortuneService;
	}
}
