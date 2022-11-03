package com.example.alpha1;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Iterator;

public class Chat extends AppCompatActivity {
    TextView chatBox, currUser;
    EditText chatEt;
    DatabaseReference chatRoot;
    String Email, message, time;
    FirebaseUser user;
    ScrollView scrollView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        chatEt = findViewById(R.id.chatEt);
        currUser = findViewById(R.id.UserStatus);
        chatBox = findViewById(R.id.message);
        scrollView = findViewById(R.id.scroll);

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) currUser.setText("STATUS: The current user is " + user.getEmail());
        else currUser.setText("STATUS: The current user is No One!");
        chatRoot = FirebaseDatabase.getInstance().getReference("Chat");

        chatRoot.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                readChat(snapshot);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                readChat(snapshot);

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void readChat(DataSnapshot snapshot) {
        Iterator<DataSnapshot> i = snapshot.getChildren().iterator();
        String name = "";
        while (i.hasNext()) {
            message = (String) ((DataSnapshot) i.next()).getValue();
            time = (String) ((DataSnapshot) i.next()).getValue();
            Email = (String) ((DataSnapshot) i.next()).getValue();
            name = Email.substring(0, Email.indexOf("@"));

        }
        chatBox.append(name + ": " + message + " -- " + time + " \n" + " \n");
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(View.FOCUS_DOWN);
            }
        });

    }

    /**
     * Uploads ChatLog object to Database
     *
     * @param view
     */
    public void send(View view) {
        if (user == null)
            Toast.makeText(this, "Need to log in to use chat!", Toast.LENGTH_SHORT).show();
        else if (chatEt.getText().toString().equals(""))
            Toast.makeText(this, "Need to write something", Toast.LENGTH_SHORT).show();
        else {
            Email = user.getEmail();
            message = chatEt.getText().toString();
            Calendar now = Calendar.getInstance();
            if (("" + now.get(Calendar.MINUTE)).length() == 1)
                time = now.get(Calendar.HOUR_OF_DAY) + ":0" + now.get(Calendar.MINUTE);
            else time = now.get(Calendar.HOUR_OF_DAY) + ":" + now.get(Calendar.MINUTE);

            ChatLog upload = new ChatLog(Email, message, time);
            chatRoot.child((System.currentTimeMillis() + "").trim()).setValue(upload);
            Toast.makeText(this, "Message Sent!", Toast.LENGTH_SHORT).show();
            chatEt.setText("");
            scrollView.post(new Runnable() {
                @Override
                public void run() {
                    scrollView.fullScroll(View.FOCUS_DOWN);
                }
            });
        }
    }


    /**
     * On Click for the logOut Button.
     *
     * @param view
     */
    public void logOut(View view) {
        FirebaseAuth.getInstance().signOut();
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setTitle("Successfully logged out!");
        Intent si = new Intent(this, login1.class);
        adb.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(si);
            }
        });
        AlertDialog ad = adb.create();
        ad.show();
    }


}