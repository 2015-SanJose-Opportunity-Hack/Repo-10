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
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;


public class MainActivity extends ActionBarActivity {

    List<ParseObject> mBusinessData;
    ViewGroup listContent;
    ViewGroup mapContent;
    ContentType contentType = ContentType.LIST;
    enum ContentType { LIST, MAP };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText text = (EditText)findViewById(R.id.search_edit_text);
        listContent = (ViewGroup)findViewById(R.id.result_list_view);
        mapContent = (ViewGroup)findViewById(R.id.result_map_view);

        updateContentView(createQuery(""));

        Button b = (Button)findViewById(R.id.search_button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText text = (EditText)findViewById(R.id.search_edit_text);
                updateContentView(createQuery(text.getText().toString()));
            }
        });

        b = (Button)findViewById(R.id.list_button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Switch to List View", LENGTH_SHORT).show();
                if(contentType != ContentType.LIST) {
                    contentType = ContentType.LIST;
                    updateContentView(createQuery(text.getText().toString()));
                }
            }
        });

        b = (Button)findViewById(R.id.map_button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Switch to Map View", LENGTH_SHORT).show();
                if(contentType != ContentType.MAP) {
                    contentType = ContentType.MAP;
                    updateContentView(createQuery(text.getText().toString()));
                }
            }
        });
    }

    private ParseQuery createQuery(final String keyword) {
        ParseQuery query = new ParseQuery("TestObject");
        query.whereContains("Name", keyword);
        return query;
    }

    private void updateContentView(ParseQuery query) {
        if(contentType == ContentType.LIST) {
            createListViewAdapter(query);
            listContent.setVisibility(View.VISIBLE);
            mapContent.setVisibility(View.GONE);
        } else {
            query.findInBackground(new FindCallback() {
                @Override
                public void done(Object objects, Throwable t) {}
                @Override
                public void done(List objects, ParseException e) {
                    //TODO: add map code here
                }
            });
            listContent.setVisibility(View.GONE);
            mapContent.setVisibility(View.VISIBLE);
        }
    }

    private void createListViewAdapter(final ParseQuery query) {
        // Initialize a ParseQueryAdapter
        BusinessAdapter adapter = new BusinessAdapter(this,
                new ParseQueryAdapter.QueryFactory<ParseObject>() {
                    public ParseQuery<ParseObject> create() {
                        // Here we can configure a ParseQuery to our heart's desire.
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