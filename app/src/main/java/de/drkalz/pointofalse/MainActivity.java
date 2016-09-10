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

import java.util.Date;

import static android.support.design.widget.Snackbar.make;


public class MainActivity extends AppCompatActivity {

    private TextView mNameText;
    private TextView mQuantityText;
    private TextView mDeliveryDateText;
    final StartApp sApp = StartApp.getInstance();
    public Item myCurrentItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mNameText = (TextView) findViewById(R.id.name_text);
        mQuantityText = (TextView) findViewById(R.id.quantity_text);
        mDeliveryDateText = (TextView) findViewById(R.id.date_text);

        sApp.setCurrentItem(new Item());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItem(false);
                showCurrentItem();
                Snackbar.make(view, "Item added", Snackbar.LENGTH_LONG).show();
            }
        });

        mNameText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callEditRemoveMenu();
            }
        });
    }

    private void callEditRemoveMenu() {
        final CharSequence[] menuItems = {"Edit", "Remove"};
        DialogFragment df = new DialogFragment() {
            @NonNull
            @Override
            public Dialog onCreateDialog(Bundle savedInstanceState) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Please Choose");
                builder.setItems(menuItems, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 0) {
                            addItem(true);
                        } else {

                            sApp.setCurrentItem(new Item());
                        }
                    }
                });

                return builder.create();
            }
        };
        df.show(getFragmentManager(), "EditRemove");
    }

    public void addItem(boolean isEditing) {
        if (isEditing) {
            Bundle bundle = new Bundle();
            bundle.putString("name", sApp.getCurrentItem().getName());
            bundle.putInt("quantity", sApp.getCurrentItem().getQuantity());
            bundle.putLong("date", sApp.getCurrentItem().getDeliveryDateTime());
            bundle.putBoolean("isEditing", true);
            AddItemDialog df = new AddItemDialog();
            df.setArguments(bundle);
            df.show(getFragmentManager(), "AlertItemDialog");
            showCurrentItem();
        } else {
            AddItemDialog df = new AddItemDialog();
            df.show(getFragmentManager(), "AlertItemDialog");
            showCurrentItem();
        }
    }

    private void showCurrentItem() {
        mNameText.setText(sApp.getCurrentItem().getName());
        mQuantityText.setText(String.valueOf(sApp.getCurrentItem().getQuantity()));
        mDeliveryDateText.setText(getString(R.string.date_format, new Date(sApp.getCurrentItem().getDeliveryDateTime())));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       switch (item.getItemId()) {
           case R.id.action_reset:
               final Item mClearedItem = myCurrentItem;
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
