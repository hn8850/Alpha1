package com.example.alpha1;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class activity_3 extends AppCompatActivity {

    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private static final int CAMERA_REQUEST = 1;

    ImageView pic;
    ProgressBar prog;
    String currentPhotoPath;


    Uri imageUri;
    StorageReference storageRef;
    DatabaseReference fbRef;
    StorageTask uploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_3);
        pic = findViewById(R.id.picIv);
        prog = findViewById(R.id.prog3);

        storageRef = FirebaseStorage.getInstance().getReference("pictures");
        fbRef = FirebaseDatabase.getInstance().getReference("pictures");

    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void takePic(View view) {
        askCameraPermission();
    }

    private void askCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
        } else {
            dispatchTakePictureIntent();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                dispatchTakePictureIntent();
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }

    }


    public void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {

            }


            if (photoFile != null) {
                imageUri = FileProvider.getUriForFile(this, "com.mydomain.fileprovider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST);
            }
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            File f = new File(currentPhotoPath);
            pic.setImageURI(Uri.fromFile(f));
        }
    }



    public File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        currentPhotoPath = image.getAbsolutePath();
        return image;
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
                            Toast.makeText(activity_3.this, "Upload Successful!", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(activity_3.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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

        if (st.equals("Login")) {
            Intent si = new Intent(this, login1.class);
            startActivity(si);
        }
        if (st.equals("Chat")){
            Intent si = new Intent(this, Chat.class);
            startActivity(si);
        }
        /*
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

        if (st.equals("Gallery")) {
            Intent si = new Intent(this, activity_2.class);
            startActivity(si);
        }

        if (st.equals("Camera")) {
            Toast.makeText(this, "You're in this Activity!!", Toast.LENGTH_SHORT).show();
        }

        return true;
    }
}