package com.kivalocalteam10.kivalocal;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import java.util.List;


public class MainActivity extends ActionBarActivity {

    List<ParseObject> mBusinessData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchNearbyPlaces("");

        Button b = (Button)findViewById(R.id.search_button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText text = (EditText)findViewById(R.id.search_edit_text);
                searchNearbyPlaces(text.getText().toString());
            }
        });
    }

    private void searchNearbyPlaces(final String keyword) {
        // Initialize a ParseQueryAdapter
        BusinessAdapter adapter = new BusinessAdapter(this,
                new ParseQueryAdapter.QueryFactory<ParseObject>() {
                    public ParseQuery<ParseObject> create() {
                        // Here we can configure a ParseQuery to our heart's desire.
                        ParseQuery query = new ParseQuery("TestObject");
                        query.whereContains("Name", keyword);
                        return query;
                    }
                });
// Custom ParseQueryAdapter, for all ParseQueryAdapter setting please check our doc
        adapter.setTextKey("Name");
        //adapter.addOnQueryLoadListener();
        //adapter.setImageKey("photo");
        ListView listView = (ListView) findViewById(R.id.result_list_view);
        listView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
class BusinessAdapter extends ParseQueryAdapter<ParseObject> {

    public BusinessAdapter(Context context, QueryFactory<ParseObject> queryFactory) {
        super(context, queryFactory);
    }

    @Override
    public View getItemView(ParseObject object, View v, ViewGroup parent) {
        if (v == null) {
            v = View.inflate(getContext(), R.layout.list_item, null);
        }

        // Take advantage of ParseQueryAdapter's getItemView logic for
        // populating the main TextView/ImageView.
        // The IDs in your custom layout must match what ParseQueryAdapter expects
        // if it will be populating a TextView or ImageView for you.
        super.getItemView(object, v, parent);

        // Do additional configuration before returning the View.
        TextView addressView = (TextView) v.findViewById(R.id.address);
        addressView.setText(object.getString("Address"));
        return v;
    }
}