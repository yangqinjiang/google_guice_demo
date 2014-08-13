package no.google.guice.demo;

public class StorageClient {

	public static void main(String[] args) {
		Storage storage = new FileStorage();
		storage.store("123","your data");
		
		storage = new DatabaseStorage();
		storage.store("123","your data");
	}

}
interface Storage{
	public void store(String uniqueId,String data);//´æ´¢ 
	public String retrieve(String uniqueId);//´æ´¢ 
}
//File Storage
class FileStorage implements Storage{

	@Override
	public void store(String uniqueId, String data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String retrieve(String uniqueId) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
//Database Storage
class DatabaseStorage implements Storage{

	@Override
	public void store(String uniqueId, String data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String retrieve(String uniqueId) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
