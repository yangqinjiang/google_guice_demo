package has.google.guice;

public class AssertUtils {


	public static void assertTrue(boolean ok){
		if(!ok){
			throw new RuntimeException();
		}
	}
}
