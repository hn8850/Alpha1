package com.example.alpha1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.alpha1.databinding.ActivityGooglePayBinding;

public class GooglePay extends AppCompatActivity {

    private ActivityGooglePayBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGooglePayBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());




    }
}