package upv.locamo.tfg.smarthome.app;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import upv.locamo.tfg.smarthome.app.restlet.SmarthomeShoppingList;


public class ShoppingListActivity extends ActionBarActivity {


    private Button btn_addItem;
    private EditText et_item;

    private ListView lv_shopping;
    public ArrayAdapter<String> adapter;
    private SmarthomeShoppingList smarthomeShoppingList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        System.setProperty("java.net.preferIPv6Addresses", "false");

        et_item = (EditText) findViewById(R.id.et_item);
        btn_addItem = (Button) findViewById(R.id.btn_addItem);
        lv_shopping = (ListView) findViewById(R.id.lv_shopping);
        adapter = new ArrayAdapter<String>(this, R.layout.list_row, R.id.tv_item);

        smarthomeShoppingList = new SmarthomeShoppingList(this);
        smarthomeShoppingList.getShoppingListFromServer();

        lv_shopping.setAdapter(adapter);
        btn_addItem.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String itemToAdd = et_item.getText().toString();
                if (!itemToAdd.matches("")) {
                    String aux = itemToAdd.substring(0, 1).toUpperCase() + itemToAdd.substring(1);
                    adapter.add(aux);
                    smarthomeShoppingList.addItemToShoppingList(aux);
                    et_item.setText("");
                }
            }
        });

        lv_shopping.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int position, long arg3) {

                String itemToDelete = lv_shopping.getItemAtPosition(position).toString();
                smarthomeShoppingList.deleteItemFromShoppingList(itemToDelete);
                adapter.remove(lv_shopping.getItemAtPosition(position).toString());
                adapter.notifyDataSetChanged();
                adapter.notifyDataSetInvalidated();

                return true;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.shopping_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.about) {
            Intent aboutActivity = new Intent(ShoppingListActivity.this, AboutUsActivity.class);
            startActivity(aboutActivity);
        }
        if (id == R.id.logout) {
            Intent logoutActivity = new Intent(this, LogoutActivity.class);
            logoutActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                    Intent.FLAG_ACTIVITY_CLEAR_TASK |
                    Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(logoutActivity);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


}
