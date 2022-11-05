package com.example.alpha1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class mapa extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);

    }

    public void navigo(View view) {
        Intent si = new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=31.2676790,34.8109860"));
        si.setPackage("com.google.android.apps.maps");
        if (si.resolveActivity(getPackageManager()) != null) {
            startActivity(si);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        String st = item.getTitle().toString();

        if (st.equals("Gallery")) {
            Intent si = new Intent(this, activity_2.class);
            startActivity(si);
        }

        if (st.equals("Camera")) {
            Intent si = new Intent(this, activity_3.class);
            startActivity(si);
        }

        if (st.equals("Login")) {
            Intent si = new Intent(this, login1.class);
            startActivity(si);
        }

        if (st.equals("Chat")) {
            Intent si = new Intent(this, Chat.class);
            startActivity(si);
        }
        if (st.equals("Notifications")) {
            Intent si = new Intent(this, notifs.class);
            startActivity(si);
        }

        if (st.equals("Google Pay")) {
            Intent si = new Intent(this, GooglePay.class);
            startActivity(si);
        }

        if (st.equals("Maps")) {
            Toast.makeText(this, "You're in this Activity!!", Toast.LENGTH_SHORT).show();

        }

        return true;
    }
}