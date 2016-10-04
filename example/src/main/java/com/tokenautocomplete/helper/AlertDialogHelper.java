package com.tokenautocomplete.helper;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;

//import android.support.v4.app.AlertDialog;

/**
 * Created by fiyyanp on 6/21/2016.
 */
public class AlertDialogHelper {
    private Context context;

    public AlertDialogHelper(Context context) {
        this.context = context;
    }

    public void show(String msg){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage(msg);
        alertDialogBuilder.setNeutralButton("OK", null).create().show();

    }

    public void show(String msg, final Class c){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage(msg);
        alertDialogBuilder.setNeutralButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent myIntent = new Intent(context, c);
                context.startActivity(myIntent);

            }
        }).create().show();
    }
}
