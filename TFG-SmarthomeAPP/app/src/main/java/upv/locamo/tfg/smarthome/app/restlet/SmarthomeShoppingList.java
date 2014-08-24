package upv.locamo.tfg.smarthome.app.restlet;


import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import upv.locamo.tfg.smarthome.app.ShoppingListActivity;

public class SmarthomeShoppingList {

    private ArrayList<String> shoppingList;

    protected ShoppingListActivity context;

    // Constructor with the context of ShoppingListActivity
    public SmarthomeShoppingList(ShoppingListActivity context){
        this.context = context;
    }

    public SmarthomeShoppingList(){
        shoppingList = null;
    }

    public void getShoppingListFromServer(){
        ReceiveShoppingListAndDrawItTask receiveShoppingListTask = new ReceiveShoppingListAndDrawItTask();
        receiveShoppingListTask.execute();
    }

    private class ReceiveShoppingListAndDrawItTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... args) {
            ClientResource clientResource = new ClientResource(Method.GET, "http://locamo.no-ip.org:8284/shoppingList");
            Representation rep = clientResource.get();
            final ArrayList<String> list = new ArrayList<String>();
            try {
                String result = (new JsonRepresentation(rep)).getText();
                JSONObject jsonRep = new JSONObject(result);
                JSONArray jsonArray = jsonRep.getJSONArray("shopping_list");
                if (jsonArray.length() != 0) {
                    for (int i = 0; i < jsonArray.length();i++){
                        list.add(jsonArray.get(i).toString());
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (list.size() != 0) {
                        for (String s : list)
                            context.adapter.add(s);
                    }
                }
            });

            return null;
        }
    }

    public void addItemToShoppingList(String item){
        SendItemToShoppingListTask sendItemToShoppingListTask = new SendItemToShoppingListTask();
        sendItemToShoppingListTask.execute(item);
    }

    private class SendItemToShoppingListTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... args) {
            String item = args[0];
            ClientResource clientResource = new ClientResource(Method.PUT, "http://locamo.no-ip.org:8284/shoppingList");
            try {
                JSONObject json = new JSONObject();
                JSONArray jsonArray = new JSONArray();
                jsonArray.put(item);
                json.put("shopping_list", jsonArray);
                Representation representation = new JsonRepresentation(json);
                clientResource.put(representation);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    public void deleteItemFromShoppingList(String item){
        DeleteItemFromShoppingListTask deleteItemFromShoppingList = new DeleteItemFromShoppingListTask();
        deleteItemFromShoppingList.execute(item);
    }

    private class DeleteItemFromShoppingListTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... args) {
            String item = args[0];
            ClientResource clientResource = new ClientResource(Method.DELETE, "http://locamo.no-ip.org:8284/shoppingList/" + item);
            clientResource.delete();

            return null;
        }
    }

    public ArrayList<String> getShoppingList(){
        GetShoppingListTask getShoppingListTask = new GetShoppingListTask();
        try {
            shoppingList = getShoppingListTask.execute().get();
            Log.e("IFNO - SmarthomeShoppingList", "El tamaño de la lista es: " + shoppingList.size());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return shoppingList;
    }


    private class GetShoppingListTask extends AsyncTask<Void, Void, ArrayList<String>> {
        @Override
        protected ArrayList<String> doInBackground(Void... args) {
            ClientResource clientResource = new ClientResource(Method.GET, "http://locamo.no-ip.org:8284/shoppingList");
            Representation rep = clientResource.get();
            ArrayList<String> list = new ArrayList<String>();
            try {
                String result = (new JsonRepresentation(rep)).getText();
                JSONObject jsonRep = new JSONObject(result);
                JSONArray jsonArray = jsonRep.getJSONArray("shopping_list");
                if (jsonArray.length() != 0) {
                    for (int i = 0; i < jsonArray.length();i++){
                        list.add(jsonArray.get(i).toString());
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.e("IFNO - SmarthomeShoppingList TASK", "El tamaño de la lista es: " + list.size());
            return list;
        }
    }

}
