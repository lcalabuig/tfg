package upv.locamo.tfg.ip;

import org.restlet.Component;
import org.restlet.data.Protocol;

import upv.locamo.tfg.ip.IPServerApplication;


public class IPServerComponent extends Component{

	
	public static void main (String[] args) throws Exception{		
		new IPServerComponent().start();
	}
	
	
	public IPServerComponent(){
		getServers().add(Protocol.HTTP, 80);
		//Attach the application to the default virtual host
		getDefaultHost().attachDefault(new IPServerApplication());
	}
	
}
