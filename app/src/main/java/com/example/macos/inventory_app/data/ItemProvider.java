package com.example.macos.inventory_app.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.sql.SQLException;

/**
 * Created by macos on 11.11.2017.
 */

public class ItemProvider extends ContentProvider {
    private ItemdbHelper itemdbHelper;
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "ItemReader.db";
    private SQLiteDatabase db;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    Uri uri1 = Uri.parse(ItemContract.CONTENT_URI);
    Uri _uri;

    static {
        /*
         * The calls to addURI() go here, for all of the content URI patterns that the provider
         * should recognize. For this snippet, only the calls for table 3 are shown.
         */

        /*
         * Sets the integer value for multiple rows in table 3 to 1. Notice that no wildcard is used
         * in the path
         */
        sUriMatcher.addURI("com.example.macos.inventory_app", "Items", 1);

        /*
         * Sets the code for a single row to 2. In this case, the "#" wildcard is
         * used. "content://com.example.app.provider/table3/3" matches, but
         * "content://com.example.app.provider/table3 doesn't.
         */
        sUriMatcher.addURI("com.example.macos.inventory_app", "Items/#", 2);
    }
    @Override
    public boolean onCreate() {
        itemdbHelper = new ItemdbHelper(getContext(),DATABASE_NAME,null,DATABASE_VERSION);
        db = itemdbHelper.getWritableDatabase();
        return (db==null)? false:true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        db = itemdbHelper.getReadableDatabase();
        Cursor cursor = null;
        int match = sUriMatcher.match(uri);
        switch(match){
            case 1:
                cursor= db.query(ItemContract.ItemEntry.TABLE_NAME, strings,s,strings1,null,null,s1);
                break;
            case 2:
                s = ItemContract.ItemEntry._ID + "=?";
                strings1 = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                // This will perform a query on the pets table where the _id equals 3 to return a
                // Cursor containing that row of the table.
                cursor = db.query(ItemContract.ItemEntry.TABLE_NAME, strings, s, strings1,
                        null, null, s1);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);

        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        int match = sUriMatcher.match(uri);
        switch(match){
            case 1:
                return insertItem(uri,contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        db = itemdbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        switch(match){
            case 1:
                int id = db.delete(ItemContract.ItemEntry.TABLE_NAME,s,strings);
                if(id!=0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return id;
            case 2:
                s = ItemContract.ItemEntry._ID + "=?";
                strings = new String[]{String.valueOf(ContentUris.parseId(uri))};
                int idd = db.delete(ItemContract.ItemEntry.TABLE_NAME,s,strings);
                if(idd!=0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return idd;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        int match = sUriMatcher.match(uri);
        switch (match) {
            case 1:
                int ID = updatePet(uri, contentValues, s, strings);
                if(ID!=0){
                    getContext().getContentResolver().notifyChange(uri,null);
                }
                return ID;
            case 2:
                // For the PET_ID code, extract out the ID from the URI,
                // so we know which row to update. Selection will be "_id=?" and selection
                // arguments will be a String array containing the actual ID.
                s = ItemContract.ItemEntry._ID + "=?";
                strings = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                int IDd = updatePet(uri, contentValues, s, strings);
                if(IDd!=0){
                    getContext().getContentResolver().notifyChange(uri,null);
                }
                return IDd;
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }
    public Uri insertItem(Uri uri, ContentValues values){
        db = itemdbHelper.getWritableDatabase();
        //SanityCheck
        String name = values.getAsString(ItemContract.ItemEntry.COLUMN_NAME_NAME);
        if (name == null) {
            throw new IllegalArgumentException("Item requires a name");
        }
        String quantity = values.getAsString(ItemContract.ItemEntry.COLUMN_NAME_QUANTITY);
        if (quantity == null) {
            throw new IllegalArgumentException("Item requires quantity");
        }
        long id = db.insert(ItemContract.ItemEntry.TABLE_NAME,null,values);
        if(id == -1){
            Log.e("ItemProvider", "Failed to insert row for " + uri);
            return null;
        }
        if(id!=0){
        getContext().getContentResolver().notifyChange(uri,null);
        }
       return ContentUris.withAppendedId(uri,id);
    }
    private int updatePet(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // Otherwise, get writeable database to update the data
        SQLiteDatabase database = itemdbHelper.getWritableDatabase();

        // Returns the number of database rows affected by the update statement
        int ID = database.update(ItemContract.ItemEntry.TABLE_NAME, values, selection, selectionArgs);
        if(ID!=0)
        {
            getContext().getContentResolver().notifyChange(uri,null);
        }


        return ID;
    }
}
