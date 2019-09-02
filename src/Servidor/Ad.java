package Servidor;

import java.net.Socket;
import java.util.ArrayList;

public class Ad {
	private String nameProduct;
	private Socket owner;
	private ArrayList<Socket> users;
	private int time;
	private int currentValue;
	private String description;
	private String name;

	public Ad(String name, Socket owner) {
		this.name = name;
		this.owner = owner;
		users = new ArrayList<Socket>();
		users.add(owner);
		this.time = 30;
		this.currentValue = 0;
	}

	public String getName() {
		return name;
	}

	public void addUser(Socket user) {
		if(users.contains(user)) {
			
		}else {
			users.add(user);
		}
		
	}
	
	public ArrayList<Socket> getUsers(){
		return users;
	}

	public int getTime() {
		return time;
	}

	public int getValue() {
		return currentValue;
	}

	public void setValue(int result) {
		this.currentValue=result;
		
	}

	public Socket getOwner() {
		return owner;
	}

	public void setName(String clientSentence) {
		this.nameProduct = clientSentence;
		
	}

	public void setDescrition(String clientSentence) {
		this.description = clientSentence;
		
	}

	public void setMoreTime(int result) {
		this.time = time+result;
		
	}

	public String getNameProduct() {
		return nameProduct;
	}

	public String getDescription() {
		return description;
	}
	
}
