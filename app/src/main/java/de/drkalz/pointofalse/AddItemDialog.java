package de.drkalz.pointofalse;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class AddItemDialog extends DialogFragment {

    final StartApp sApp = StartApp.getInstance();
    boolean isEditing = false;

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_add, null);
        builder.setView(view);

        final EditText goodsEdT = (EditText) view.findViewById(R.id.goodsEdT);
        final EditText quantityEdT = (EditText) view.findViewById(R.id.quantityEdT);
        final DatePicker deliveryDate = (DatePicker) view.findViewById(R.id.deliveryDate);

        Bundle bundle = getArguments();
        if (bundle != null) {
            goodsEdT.setText(bundle.getString("name"));
            quantityEdT.setText(String.valueOf(bundle.getInt("quantity")));
            Date newDate = new Date(bundle.getLong("date"));
            Calendar cal = Calendar.getInstance();
            cal.setTime(newDate);
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            deliveryDate.updateDate(year, month, day);
            isEditing = bundle.getBoolean("isEditing");
        }

        builder.setNegativeButton(android.R.string.cancel, null);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String name = goodsEdT.getText().toString();
                int  quantity = Integer.parseInt(quantityEdT.getText().toString());
                Calendar calendar = new GregorianCalendar(deliveryDate.getYear(),
                        deliveryDate.getMonth(), deliveryDate.getDayOfMonth());
                long date = calendar.getTimeInMillis();
                sApp.setCurrentItem(new Item(name, quantity, new Date(date)));
                if (!isEditing) {
                    sApp.addItemToArray(sApp.getCurrentItem());
                } else {
                    int x = sApp.getPositionInArray(sApp.getCurrentItem());
                    sApp.changeItemInArray(x, sApp.getCurrentItem());
                }
            }
        });
        return builder.create();
    }
}
