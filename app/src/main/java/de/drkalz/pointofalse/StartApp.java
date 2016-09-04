package de.drkalz.pointofalse;

import android.app.Application;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Alex on 04.09.16.
 */

public class StartApp extends Application {

    private static StartApp singleInstance = null;
    private de.drkalz.pointofalse.Item mCurrentItem;
    private ArrayList<Item> mItems = new ArrayList<>();

    public static StartApp getInstance() {
        return singleInstance;
    }

    public Item getCurrentItem() {
        return mCurrentItem;
    }

    public void setCurrentItem(Item mCurrentItem) {
        this.mCurrentItem = mCurrentItem;
    }

    public void addItemToArray(Item item) {
        this.mItems.add(item);
    }

    public Item getItemFromArray (int position) {
        return mItems.get(position);
    }

    public ArrayList<Item> getItems() {
        return mItems;
    }

    public void setItems(ArrayList<Item> mItems) {
        this.mItems = mItems;
    }

    public void emptyCurrentItem() {
        mCurrentItem.setName("");
        mCurrentItem.setQuantity(0);
        mCurrentItem.setDeliveryDate(new Date(0));
    }

    @Override
    public void onCreate() {
        super.onCreate();
        singleInstance = this;
    }


}
