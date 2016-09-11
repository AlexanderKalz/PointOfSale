package de.drkalz.pointofalse;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by boutell on 11/14/2015.
 */
public class Item {
    private String mName;
    private int mQuantity;
    private Date mDeliveryDate;

    public Item() {
        mName = "--";
        mQuantity = 0;
        mDeliveryDate = new Date(0);
    }

    public Item(String name, int quantity, Date deliveryDate) {
        mName = name;
        mQuantity = quantity;
        mDeliveryDate = deliveryDate;
    }

    public static Item getDefaultItem() {
        return new Item("Earplugs", 5, new Date());
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public int getQuantity() {
        return mQuantity;
    }

    public void setQuantity(int quantity) {
        mQuantity = quantity;
    }

    public String getDeliveryDateString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        String date = sdf.format(mDeliveryDate);
        return date;
    }

    public long getDeliveryDateTime() {
        return mDeliveryDate.getTime();
    }

    public void setDeliveryDate(Date deliveryDate) {
        mDeliveryDate = deliveryDate;
    }


}
