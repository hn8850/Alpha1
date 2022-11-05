package com.example.alpha1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.alpha1.databinding.ActivityGooglePayBinding;

public class GooglePay extends AppCompatActivity {

    private ActivityGooglePayBinding binding;
    public static final String GOOGLE_PAY_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user";
    int GOOGLE_PAY_REQUEST_CODE = 123;
    String amount;
    String name = "Highbrow Director";
    String upiId = "hashimads123@oksbi";
    String transactionNote = "pay test";
    String status;
    Uri uri;

    private static boolean isAppInstalled(Context context, String packageName) {
        try {
            context.getPackageManager().getApplicationInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private static Uri getUpiPaymentUri(String name, String upiId, String transactionNote, String amount) {
        return new Uri.Builder()
                .scheme("upi")
                .authority("pay")
                .appendQueryParameter("pa", upiId)
                .appendQueryParameter("pn", name)
                .appendQueryParameter("tn", transactionNote)
                .appendQueryParameter("am", amount)
                .appendQueryParameter("cu", "INR")
                .build();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGooglePayBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.googlePayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amount = binding.amountEditText.getText().toString();
                if (!amount.isEmpty()) {
                    uri = getUpiPaymentUri(name, upiId, transactionNote, amount);
                    payWithGPay();
                } else {
                    binding.amountEditText.setError("Amount is required!");
                    binding.amountEditText.requestFocus();
                }

            }
        });
    }

    private void payWithGPay() {
        if (isAppInstalled(this, GOOGLE_PAY_PACKAGE_NAME)) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(uri);
            intent.setPackage(GOOGLE_PAY_PACKAGE_NAME);
            startActivityForResult(intent, GOOGLE_PAY_REQUEST_CODE);
        } else {
            Toast.makeText(GooglePay.this, "Please Install GPay", Toast.LENGTH_SHORT).show();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            status = data.getStringExtra("Status").toLowerCase();
        }

        if ((RESULT_OK == resultCode) && status.equals("success")) {
            Toast.makeText(GooglePay.this, "Transaction Successful", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(GooglePay.this, "Transaction Failed", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        String st = item.getTitle().toString();

        if (st.equals("Login")) {
            Intent si = new Intent(this, login1.class);
            startActivity(si);
        }

        if (st.equals("Gallery")) {
            Intent si = new Intent(this, activity_2.class);
            startActivity(si);
        }

        if (st.equals("Camera")) {
            Intent si = new Intent(this, activity_3.class);
            startActivity(si);
        }

        if (st.equals("Chat")){
            Intent si = new Intent(this, Chat.class);
            startActivity(si);
        }

        if (st.equals("Notifications")){
            Intent si = new Intent(this, notifs.class);
            startActivity(si);
        }

        if (st.equals("Google Pay")) {
            Toast.makeText(this, "You're in this Activity!!", Toast.LENGTH_SHORT).show();

        }

        if (st.equals("Maps")) {
            Intent si = new Intent(this, mapa.class);
            startActivity(si);
        }

        return true;
    }

}