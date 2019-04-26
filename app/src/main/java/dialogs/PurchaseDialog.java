package dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import helper.AsyncRequest;
import masterpiece.wing.R;
import model.PreferenceModel;

/**
 * Created by User on 12/24/2018.
 */

public class PurchaseDialog  extends DialogFragment {

    private int mSelectedItem = 1;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder

                .setTitle(R.string.title)
//                .setMessage(R.string.increase_credit)
//                cannot show both message and
//                .setItems(R.array.prices_array, new DialogInterface.OnClickListener() {
                .setSingleChoiceItems(R.array.prices_array, mSelectedItem, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mSelectedItem = which;
                    }
                } )

                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        System.out.println(mSelectedItem);
                        goForPurchase();
//                        startActivity(new Intent());
                        // FIRE ZE MISSILES!
                    }})
//                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        // User cancelled the dialog
//                    }})
        ;
        // Create the AlertDialog object and return it
        return builder.create();
    }

    public void goForPurchase(){
        int price = 10000;
        switch (mSelectedItem){
            case 0:
                price = 5000;
                break;
            case 1:
                price = 10000;
                break;
            case 2:
                price = 20000;
                break;
            case 3:
                price = 50000;
                break;
            default:
                break;
        }
        System.out.println(price);
//        openInBrowser(price, preferenceModel.getPhone());
        openInBrowser(price, getArguments().getString("phone"));
//        getActivity().finish();
    }

    private void openInBrowser(int amount, String phone) {
        String uriString = String.format(AsyncRequest.BASE_URL, "zarinpal/request/?amount=",amount,"&phone=",phone);
//        Uri uri = Uri.parse(uriString);
        Uri uri = Uri.parse(AsyncRequest.BASE_URL + "zarinpal/request/?amount=" + amount + "&phone=" + phone);
//        Uri uri = Uri.parse("return://zarinpalpayment/");

        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}
