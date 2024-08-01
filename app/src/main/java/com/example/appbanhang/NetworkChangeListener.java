package com.example.appbanhang;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;


public class NetworkChangeListener extends BroadcastReceiver {
    public static boolean connected = false;
    ProgressDialog progressDialog;
    int i = 0;
    CountDownTimer countDownTimer;
    @Override
    public void onReceive(Context context, Intent intent) {
        if(!Common.checkConnection(context)){
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View layout_dialog = LayoutInflater.from(context).inflate(R.layout.noconnection,null);
            builder.setView(layout_dialog);

            AppCompatTextView btnRetry =  layout_dialog.findViewById(R.id.btnRetry);

            //show dialog
            AlertDialog dialog = builder.create();
            dialog.show();
            dialog.setCancelable(false);

            dialog.getWindow().setGravity(Gravity.CENTER);

            btnRetry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progressDialog = new ProgressDialog(context);
                    progressDialog.setMessage("Vui lòng chờ.....");
                    progressDialog.setCancelable(false);
                    progressDialog.setProgress(i);
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.GRAY));
                    progressDialog.show();
                    countDownTimer = new CountDownTimer(2000,1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                            progressDialog.setMessage("Vui lòng chờ.....");
                        }

                        @Override
                        public void onFinish() {
                            dialog.dismiss();
                            onReceive(context,intent);
                            progressDialog.dismiss();
                        }
                    }.start();
                }
            });
        }
    }
}
