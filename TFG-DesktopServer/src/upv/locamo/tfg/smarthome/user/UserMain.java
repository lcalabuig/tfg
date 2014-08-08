package upv.locamo.tfg.smarthome.user;

import java.util.ArrayList;

public class UserMain {

	private static ArrayList<SmartHomeUser> users = new ArrayList<SmartHomeUser>();

	// Hacer el manejo de la lista de los usuarios y hacer los GET, PUT y DELETE
	// en restlet

	public ArrayList<SmartHomeUser> getUsers() {
		return users;
	}
	
	public static void addUsers() {		
		users.add(new SmartHomeUser("locamo", "158.42.63.55"));
		users.add(new SmartHomeUser("temocas", "158.42.78.302"));
	}
	
	public static String getIPByUser (String uname){
		String ip = "";
		for (SmartHomeUser user : users){
			if (user.getUsername().equals(uname))
				ip = user.getIP();
		}
		return ip;
	}
	
	public static void main(String args[]){
		addUsers();
		
		String ip = getIPByUser("locamo");
		System.out.println("La ip es: " + ip);
	}
	
}
