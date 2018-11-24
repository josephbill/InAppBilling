package com.demoscad.androidinappevans;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;

public class MainActivity extends AppCompatActivity implements BillingProcessor.IBillingHandler {
    BillingProcessor bp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar =findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bp = new BillingProcessor(this,String.valueOf(R.string.license_key), this);

        FloatingActionButton fab =findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //para ver de manera diferente, primero debemos pensar diferente
                selectSubscription();

            }
        });

    }

    private void selectSubscription() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        View viewPopup = getLayoutInflater().inflate(R.layout.custompopup, null);

        Button month = viewPopup.findViewById(R.id.buttonMonth);
        Button year = viewPopup.findViewById(R.id.buttonYear);
        alertDialogBuilder.setView(viewPopup);
        final AlertDialog dialog=alertDialogBuilder.create();
        dialog.show();
        month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //monthly subscription
                bp.purchase(MainActivity.this,"mwezi1");
                dialog.dismiss();
            }
        });
        year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //yearly subscription
                bp.purchase(MainActivity.this,"mwaka1");
                dialog.dismiss();
            }
        });
//        alertDialogBuilder.setView(viewPopup);
//        AlertDialog dialog=alertDialogBuilder.create();
//        dialog.show();
    }



    @Override
    public void onProductPurchased(@NonNull String productId, @Nullable TransactionDetails details) {
        Toast.makeText(this, "Youve purchased something", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPurchaseHistoryRestored() {

    }

    @Override
    public void onBillingError(int errorCode, @Nullable Throwable error) {
        Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBillingInitialized() {

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!bp.handleActivityResult(requestCode, resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    @Override
    public void onDestroy() {
        if (bp != null) {
            bp.release();
        }
        super.onDestroy();
    }
}
