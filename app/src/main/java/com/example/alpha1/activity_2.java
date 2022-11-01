package com.example.alpha1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

public class activity_2 extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    ImageView pic;
    ProgressBar prog;
    EditText nameOfPic;

    Uri imageUri;
    StorageReference storageRef;
    DatabaseReference fbRef;
    StorageTask uploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2xml);
        pic = findViewById(R.id.FBpic);
        prog = findViewById(R.id.prog2);
        nameOfPic = findViewById(R.id.namePic);

        storageRef = FirebaseStorage.getInstance().getReference("uploads");
        fbRef = FirebaseDatabase.getInstance().getReference("uploads");
    }

    /**
     * On Click Method for the Choose Picture Button.
     *
     * @param view
     */
    public void choosePic(View view) {
        fileChoose();
    }

    /**
     * On Click Method for the Upload to FB Button.
     *
     * @param view
     */
    public void upload(View view) {
        if (uploadTask != null && uploadTask.isInProgress()) {
            Toast.makeText(this, "Upload in Progress!!", Toast.LENGTH_SHORT).show();
        } else {
            uploadFile();
        }

    }

    /**
     * Launches the Gallery in order for the user to choose a picture.
     */
    public void fileChoose() {
        Intent si = new Intent();
        si.setType("image/*");
        si.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(si, PICK_IMAGE_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            pic.setImageURI(imageUri);
        }
    }

    /**
     * This is a sub-function of the uploadFile function.
     * Returns the type of file chosen by the user.
     *
     * @param uri
     * @return
     */
    public String getFileExtension(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }


    /**
     * Uploads picture chosen to FB storage.
     */
    public void uploadFile() {
        if (imageUri != null) {
            StorageReference fileRef = storageRef.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
            uploadTask = fileRef.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    prog.setProgress(0);
                                }
                            }, 500);
                            Toast.makeText(activity_2.this, "Upload Successful!", Toast.LENGTH_SHORT).show();
                            if (taskSnapshot.getMetadata() != null) {
                                if (taskSnapshot.getMetadata().getReference() != null) {
                                    Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                                    result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            String imageUrl = uri.toString();
                                            Upload upload = new Upload(nameOfPic.getText().toString().toLowerCase(),
                                                    imageUrl);
                                            String uploadID = fbRef.push().getKey();
                                            fbRef.child(uploadID).setValue(upload);
                                        }
                                    });
                                }
                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(activity_2.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            double currentProg = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                            prog.setProgress((int) currentProg);
                        }
                    });

        } else {
            Toast.makeText(this, "No File Selected!", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        String st = item.getTitle().toString();
/*
        if (st.equals("Activity 1")){
            Intent si = new Intent(this, activity_1.class);
            startActivity(si);
        }
        if (st.equals("Activity 4")){
            Intent si = new Intent(this, activity_4.class);
            startActivity(si);
        }
        if (st.equals("Activity 5")){
            Intent si = new Intent(this, activity_5.class);
            startActivity(si);
        }
        if (st.equals("Activity 6")){
            Intent si = new Intent(this, activity_6.class);
            startActivity(si);
        }
        if (st.equals("Activity 7")){
            Intent si = new Intent(this, activity_7.class);
            startActivity(si);
        }
 */

        if (st.equals("Activity 2")) {
            Toast.makeText(this, "You're in this Activity!!", Toast.LENGTH_SHORT).show();
        }


        if (st.equals("Activity 3")) {
            Intent si = new Intent(this, activity_3.class);
            startActivity(si);
        }

        return true;
    }

}