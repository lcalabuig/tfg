package upv.locamo.tfg.utils;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.restlet.Context;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.Status;
import org.restlet.routing.Filter;

public class Blocker extends Filter{

	public final Set<String> blockedAddres;
	
	public Blocker(Context context){
		super(context);
		this.blockedAddres = new CopyOnWriteArraySet<String>();
	}
	
	public Set<String> getBlockedAddress(){
		return this.blockedAddres;
	}
	
	@Override
	protected int beforeHandle (Request request, Response response){
		int result = STOP;
		
		if (getBlockedAddress().contains(request.getClientInfo().getAddress()))
			response.setStatus(Status.CLIENT_ERROR_FORBIDDEN, "Your IP address was blocked");
		else
			result = CONTINUE;
		return result;
	}
}
