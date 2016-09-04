package de.drkalz.pointofalse;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import static android.support.design.widget.Snackbar.make;


public class MainActivity extends AppCompatActivity {

    private TextView mNameText;
    private TextView mQuantityText;
    private TextView mDeliveryDateText;
    final StartApp sApp = StartApp.getInstance();

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
                addItem(false);
                Snackbar.make(view, "Item added", Snackbar.LENGTH_LONG).show();
            }
        });

        registerForContextMenu(mNameText);

    }

    public void addItem(boolean isEditing) {
        AddItemDialog df = new AddItemDialog();
        df.show(getFragmentManager(), "AlertItemDialog");
        showCurrentItem();
    }

    private void showCurrentItem() {
        mNameText.setText(sApp.getCurrentItem().getName());
        mQuantityText.setText(getString(R.string.quantity_format, sApp.getCurrentItem().getQuantity()));
        mDeliveryDateText.setText(getString(R.string.date_format, sApp.getCurrentItem().getDeliveryDateString()));
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
               final Item mClearedItem = sApp.getCurrentItem();
               sApp.setCurrentItem(new Item());
               showCurrentItem();
               Snackbar snackbar = Snackbar.make(findViewById(R.id.coordinator_layout), "Item cleared", Snackbar.LENGTH_LONG)
                       .setAction("UNDO", new View.OnClickListener() {
                           @Override
                           public void onClick(View view) {
                               sApp.setCurrentItem(mClearedItem);
                               showCurrentItem();
                               make(findViewById(R.id.coordinator_layout), "Item restored", Snackbar.LENGTH_LONG).show();
                           }
                       });
               snackbar.show();
               return true;
           case R.id.action_settings:
               startActivity(new Intent(Settings.ACTION_SETTINGS));
               return true;
           case R.id.action_search:
               showSearchDialog();
               return true;
           default:
               return super.onOptionsItemSelected(item);
       }
    }

    private void showSearchDialog() {
        DialogFragment df = new DialogFragment() {
            @NonNull
            @Override
            public Dialog onCreateDialog(Bundle savedInstanceState) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(R.string.search_dialog_title);
                builder.setItems(getNames(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sApp.setCurrentItem(sApp.getItemFromArray(i));
                        showCurrentItem();
                    }
                });

                return builder.create();
            }
        };
        df.show(getFragmentManager(), "Search");
    }

    private String[] getNames() {
        String[] names = new String[sApp.getItems().size()];
        for (int i = 0; i < sApp.getItems().size(); i++) {
            names[i] = sApp.getItems().get(i).getName();
        }
        return names;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.menu_context, menu);

    }
}
