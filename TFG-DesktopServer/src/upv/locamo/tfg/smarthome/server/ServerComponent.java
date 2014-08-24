package upv.locamo.tfg.smarthome.server;

import java.util.Scanner;

import org.restlet.Component;
import org.restlet.data.Protocol;


public class ServerComponent extends Component{

	private static String url = "";
	private static String port = "";
	
	public static String getSmartHomeURL(){
		return "http://" + url + ":" + port;
	}
	
	public static void main (String[] args) throws Exception{		
		new ServerComponent().start();
	}
	
	private void defineSmartHomeServer(){
        System.out.println ("Please introduce the URL and port of SmartHome Server (separated by a space)");
        String inputData = "";
        Scanner scanner = new Scanner (System.in);
        inputData = scanner.nextLine();
        if (!inputData.contains(" ")){
        	System.out.println ("Please introduce it again (incorrect format)");
        	inputData = scanner.nextLine();
        }
        String[] parts = inputData.split(" ");
		url = parts[0];
		port = parts[1];
		scanner.close();
	}
	
	public ServerComponent(){
		//Define the URL for the SmartHome Server
		defineSmartHomeServer();		
		//Add a new Server at port 8284
		getServers().add(Protocol.HTTP, 8284);
		//Attach the application to the default virtual host
		getDefaultHost().attachDefault(new ServerApplication());		
	}
	
}
