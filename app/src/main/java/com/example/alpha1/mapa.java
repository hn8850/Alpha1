package com.example.alpha1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

public class mapa extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);

    }

    public void navigo(View view) {
        Intent si = new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=31.2676790,34.8109860"));
        si.setPackage("com.google.android.apps.maps");
        if (si.resolveActivity(getPackageManager())!= null){
            startActivity(si);
        }
    }
}