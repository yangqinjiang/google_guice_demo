package has.google.guice.demo;

import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Provider;

public class GuiceDemo {

	public static void main(String[] args) {
		Injector injector = Guice.createInjector(new AddModule());
		//injector.getInstance(Add.class) ���ᴴ��������һ�� SimpleAdd ���͵�ʵ��
		//ʵ������ͨ�� AddModule.configure() ��������ȡ����İ���Ϣ�ġ�
		Add add = injector.getInstance(Add.class);
		//
		System.out.println(add.add(100,540));
		
		//----
		System.out.println("----------");
		
		Injector injector2 = Guice.createInjector(new AddModuleByProvider());
		add = injector2.getInstance(Add.class);
		System.out.println(add.add(1, 2));
	}
}
//����ӿ�,��ʵ�ֽӿ�
interface Add{
	public int add(int a,int b);
}
class SimpleAdd implements Add{

	@Override
	public int add(int a, int b) {
		System.out.println("Simple add");
		return a+b;
	}
	
}
//����Module��
class AddModule implements Module{

	//��һЩ Bindings ���õ�ĳ�� Module��
	@Override
	public void configure(Binder binder) {
		//������Ĵ����У����Ǹ��� Guice �� SimpleAdd ʵ����󶨵� Add �ӿ��ϣ�
		//Ҳ����˵�ڿͻ��˵���Add.add() ����ʱ��ʵ�ʻ�ȥִ�� SimpleAdd.add() ����
		binder.bind(Add.class).to(SimpleAdd.class);
		System.out.println("Module configure");
	}
	
}
//
class AddModuleByProvider implements Module{
	@Override
	public void configure(Binder binder) {
		binder.bind(Add.class).toProvider(AddProvider.class);
	}
}

class ComplexAdd implements Add{

	@Override
	public int add(int a, int b) {
		return a+b;
	}
	
}
//provider �൱�ڴ�ͳ�Ĺ���ģʽ
//��Ҫ���ƻ�һ�����󴴽�����,ʹ��Providers
class AddProvider implements Provider<Add>{

	@Override
	public Add get() {
		System.out.println("���и���Ĵ�����������");
		return new ComplexAdd();
	}
	
}
