package upv.locamo.tfg.smarthome.app.restletServer;

import android.content.Context;
import android.util.Log;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

public class AndroidServerApplication extends Application{
    private static Context context;

    /**
     * Constructor for change the settings
     */
    public AndroidServerApplication(Context con) {
        Log.e("!!!INFO", "Entro en AndroidServerApplication()");
        context = con;
        setName("RESTful Server in Android");
        setDescription("Server in a smartphone to serve the position");
        setOwner("Lorena Calabuig");
        setAuthor("locamo");
        Log.e("!!!INFO", "Salgo de AndroidServerApplication()");
    }

    public static Context getContextApplication(){
        return context;
    }

    /**
     * Factory method called by the framework when the application starts
     * @return Restlet - a string formatted
     */
    @Override
    public Restlet createInboundRoot() {

        Router router = new Router(getContext());
        router.attach("/", DeviceServerResource.class);
        router.attach("/position", DevicePositionResource.class);

        return router;
    }


}
