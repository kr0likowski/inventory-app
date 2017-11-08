package com.example.macos.inventory_app.data;

import android.provider.BaseColumns;

public final class ItemContract {
    public ItemContract(){}

    public static class ItemEntry implements BaseColumns{
        public static final String _ID = BaseColumns._ID;
        public static final String TABLE_NAME = "Items";
        public static final String COLUMN_NAME_NAME = "Name";
        public static final String COLUMN_NAME_QUANTITY = "Quantity";
        public static final String COLUMN_NAME_PRICE = "Price";
    }
}