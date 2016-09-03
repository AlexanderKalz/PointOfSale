package de.drkalz.pointofalse;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

import static android.support.design.widget.Snackbar.make;


public class MainActivity extends AppCompatActivity {

    private TextView mNameText;
    private TextView mQuantityText;
    private TextView mDeliveryDateText;
    private de.drkalz.pointofalse.Item mCurrentItem;
    private ArrayList<Item> mItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mNameText = (TextView) findViewById(R.id.name_text);
        mQuantityText = (TextView) findViewById(R.id.quantity_text);
        mDeliveryDateText = (TextView) findViewById(R.id.date_text);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItem();
                Snackbar.make(view, "Item added", Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void addItem() {
        DialogFragment df = new DialogFragment() {
            @Override
            public Dialog onCreateDialog(Bundle savedInstanceState) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_add, null);
                builder.setView(view);

                final EditText goodsEdT = (EditText) view.findViewById(R.id.goodsEdT);
                final EditText quantityEdT = (EditText) view.findViewById(R.id.quantity_text);
                final CalendarView deliveryDate = (CalendarView) view.findViewById(R.id.deliveryDate);

                builder.setNegativeButton(android.R.string.cancel, null);
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String name = goodsEdT.getText().toString();
                        int  quantity = Integer.parseInt(quantityEdT.getText().toString());
                        long date = deliveryDate.getDate();
                        mCurrentItem = new Item(name, quantity, new Date(date));
                        mItems.add(mCurrentItem);
                        showCurrentItem();
                    }
                });
                return builder.create();
            }
        };
        df.show(getSupportFragmentManager(), "AlertItemDialog");
    }

    private void showCurrentItem() {
        mNameText.setText(mCurrentItem.getName());
        mQuantityText.setText(getString(R.string.quantity_format, mCurrentItem.getQuantity()));
        mDeliveryDateText.setText(getString(R.string.date_format, mCurrentItem.getDeliveryDateString()));
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
       switch (item.getItemId()) {
           case R.id.action_reset:
               final Item mClearedItem = mCurrentItem;
               mCurrentItem = new Item();
               showCurrentItem();
               Snackbar snackbar = Snackbar.make(findViewById(R.id.coordinator_layout), "Item cleared", Snackbar.LENGTH_LONG)
                       .setAction("UNDO", new View.OnClickListener() {
                           @Override
                           public void onClick(View view) {
                               mCurrentItem = mClearedItem;
                               showCurrentItem();
                               make(findViewById(R.id.coordinator_layout), "Item restored", Snackbar.LENGTH_LONG).show();
                           }
                       });
               snackbar.show();
               return true;
           case R.id.action_settings:
               startActivity(new Intent(Settings.ACTION_SETTINGS));
               return true;
           default:
               return super.onOptionsItemSelected(item);
       }
    }
}
