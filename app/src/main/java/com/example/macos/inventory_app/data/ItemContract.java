package com.example.macos.inventory_app.data;

import android.content.ContentResolver;
import android.provider.BaseColumns;

public final class ItemContract {
    public ItemContract(){}
    public static final String CONTENT_URI = "com.example.macos.inventory_app";
    public static final String CONTENT_LIST_TYPE =
            ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_URI + "/Items";
    public static final String CONTENT_ITEM_TYPE =
            ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_URI + "/Items/#";
    public static class ItemEntry implements BaseColumns{
        public static final String _ID = BaseColumns._ID;
        public static final String TABLE_NAME = "Items";
        public static final String COLUMN_NAME_NAME = "Name";
        public static final String COLUMN_NAME_QUANTITY = "Quantity";
        public static final String COLUMN_NAME_PRICE = "Price";
    }
}