package upv.locamo.tfg.smarthome.app.restletServer;

import android.util.Log;

import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;


public class DeviceServerResource extends ServerResource {

    public DeviceServerResource(){
        setNegotiated(false);
    }

    @Override
    protected Representation get() throws ResourceException {
        Log.i("Restlet", "Acceding to Device Server");
        return new StringRepresentation("This is a server application in Android");
    }

}
