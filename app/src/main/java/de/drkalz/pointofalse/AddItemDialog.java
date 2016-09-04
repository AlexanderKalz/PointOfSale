package de.drkalz.pointofalse;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.AlertDialog;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;

import java.util.Date;

/**
 * Created by Alex on 28.08.16.
 */

public class AddItemDialog extends DialogFragment {

    final StartApp sApp = StartApp.getInstance();

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_add, null);
        builder.setView(view);

        final EditText goodsEdT = (EditText) view.findViewById(R.id.goodsEdT);
        final EditText quantityEdT = (EditText) view.findViewById(R.id.quantityEdT);
        final CalendarView deliveryDate = (CalendarView) view.findViewById(R.id.deliveryDate);

        builder.setNegativeButton(android.R.string.cancel, null);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String name = goodsEdT.getText().toString();
                int  quantity = Integer.parseInt(quantityEdT.getText().toString());
                long date = deliveryDate.getDate();
                sApp.setCurrentItem(new Item(name, quantity, new Date(date)));
                sApp.addItemToArray(sApp.getCurrentItem());
            }
        });
        return builder.create();
    }
}
