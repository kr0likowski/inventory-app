package com.example.macos.inventory_app.data;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.CursorAnchorInfo;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.macos.inventory_app.R;

/**
 * Created by macos on 12.11.2017.
 */
public class ItemCursorAdapter extends CursorAdapter {
    private LayoutInflater cursorInflater;
    private TextView name;
    private TextView price;
    public ItemCursorAdapter(Context context, Cursor c) {
        super(context, c);
        cursorInflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return cursorInflater.inflate(R.layout.list_item,viewGroup,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        name = (TextView)view.findViewById(R.id.name);
        price = (TextView)view.findViewById(R.id.summary);

        int nameColumnIndex = cursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_NAME_NAME);
        int priceColumnIndex = cursor.getColumnIndex(ItemContract.ItemEntry.COLUMN_NAME_PRICE);

        String nameString = cursor.getString(nameColumnIndex);
        String priceString = cursor.getString(priceColumnIndex);

        name.setText(nameString);
        price.setText(priceString);
    }
}
