package com.example.macos.inventory_app;

import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.app.LoaderManager;
import android.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import com.example.macos.inventory_app.data.ItemContract.ItemEntry;

public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    NumberPicker np;
    Uri currentItemUri;
    private EditText mNameEditText;
    private EditText mpPriceEditText;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.editor_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.item1:
                savePet();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        mNameEditText = (EditText)findViewById(R.id.name);
        mpPriceEditText= (EditText)findViewById(R.id.price);
        Intent intent = getIntent();
        currentItemUri = intent.getData();
        if(currentItemUri==null){
            setTitle("Add an item");
        }else{
            setTitle("Edit an item");
            getLoaderManager().initLoader(0, null, this);
        }
        np = (NumberPicker)findViewById(R.id.np);
        np.setMinValue(0);
        np.setMaxValue(99);
        np.setValue(1);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                ItemEntry._ID,
                ItemEntry.COLUMN_NAME_NAME,
                ItemEntry.COLUMN_NAME_PRICE,
                ItemEntry.COLUMN_NAME_QUANTITY

        };
        return new CursorLoader(this,currentItemUri,projection,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if(data.moveToNext()) {
            int nameColumnIndex = data.getColumnIndex(ItemEntry.COLUMN_NAME_NAME);
            int priceColumnIndex = data.getColumnIndex(ItemEntry.COLUMN_NAME_PRICE);
            int quantityColumnIndex = data.getColumnIndex(ItemEntry.COLUMN_NAME_QUANTITY);
            mNameEditText.setText(data.getString(nameColumnIndex));
            mpPriceEditText.setText(data.getString(priceColumnIndex));
            np.setValue(data.getInt(quantityColumnIndex));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
    private void savePet() {
        if(currentItemUri==null) {
            ContentValues values = new ContentValues();
            values.put(ItemEntry.COLUMN_NAME_NAME, mNameEditText.getText().toString());
            values.put(ItemEntry.COLUMN_NAME_PRICE, mpPriceEditText.getText().toString());
            values.put(ItemEntry.COLUMN_NAME_QUANTITY, np.getValue());
            getContentResolver().insert(ItemEntry.FULL_URI, values);
           // mPetHasChanged = true;
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);

        }
        else{
            ContentValues values = new ContentValues();
            values.put(ItemEntry.COLUMN_NAME_NAME, mNameEditText.getText().toString());
            values.put(ItemEntry.COLUMN_NAME_PRICE, mpPriceEditText.getText().toString());
            values.put(ItemEntry.COLUMN_NAME_QUANTITY, np.getValue());
            getContentResolver().update(currentItemUri,values,null,null);
          //  mPetHasChanged = true;
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        }
    }
}
