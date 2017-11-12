package com.example.macos.inventory_app;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.example.macos.inventory_app.data.ItemContract;
import com.example.macos.inventory_app.data.ItemCursorAdapter;

import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    private FloatingActionButton floatingActionButton;
    private ItemCursorAdapter adapter;
    private ListView listView;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.Dummy:
                insertDummy();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLoaderManager().initLoader(0, null,this);
        listView = (ListView)findViewById(R.id.listView);
        setContentView(R.layout.activity_main);
        floatingActionButton = (FloatingActionButton)findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),EditorActivity.class);
             startActivity(intent);
            }
        });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                ItemContract.ItemEntry._ID,
                ItemContract.ItemEntry.COLUMN_NAME_NAME,
                ItemContract.ItemEntry.COLUMN_NAME_QUANTITY,
                ItemContract.ItemEntry.COLUMN_NAME_PRICE
        };
        return new CursorLoader(getApplicationContext(), ItemContract.ItemEntry.FULL_URI,projection,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
    adapter = new ItemCursorAdapter(getApplicationContext(),data);
        adapter.swapCursor(data);
        listView = (ListView)findViewById(R.id.listView);
        listView.setAdapter(adapter);

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
adapter.swapCursor(null);
    }
    private void insertDummy(){


        ContentValues values = new ContentValues();
        values.put(ItemContract.ItemEntry.COLUMN_NAME_NAME,"Tommy");
        values.put(ItemContract.ItemEntry.COLUMN_NAME_PRICE, "10$");
        values.put(ItemContract.ItemEntry.COLUMN_NAME_QUANTITY,5);

        //Insert row
        getContentResolver().insert(ItemContract.ItemEntry.FULL_URI,values);


    }
}
