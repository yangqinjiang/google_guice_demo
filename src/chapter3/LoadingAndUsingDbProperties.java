package chapter3;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.name.Named;
import com.google.inject.name.Names;

/**
 * 加载配置文件
 *
 */
public class LoadingAndUsingDbProperties {

	@Inject 
	public void databaseURL(@Named("db.url") String url){
		System.out.println(url);
	}
	public static void main(String[] args) {
		Injector i =Guice.createInjector(new PropertiesModule());
		i.getInstance(LoadingAndUsingDbProperties.class);
	}

}

class PropertiesModule extends AbstractModule{

	@Override
	protected void configure() {

		try{
			Properties dababaseProperties = loadProperties("db.properties");
			Names.bindProperties(binder(), dababaseProperties);
		}catch(RuntimeException e){
			addError("Could not configure database properties",e);
		}
	}

	private static Properties loadProperties(String name) {
		Properties properties = new Properties();
		InputStream is = new Object()
							.getClass()
							.getEnclosingClass()
							.getResourceAsStream(name);
		try{
			properties.load(is);
		}catch(IOException e){
			throw new RuntimeException(e);
		}finally{
			if(is!=null){
				try{
					is.close();
				}catch(IOException dontCare){}
			}
		}
		
		return properties;
	}
	
}