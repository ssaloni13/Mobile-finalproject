package com.example.mobile_finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mobile_finalproject.Models.Event;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class EditEventActivity extends AppCompatActivity {

    String eventId, hostEmailId;
    private Uri filePath;
    private StorageReference mStorageStickerReference1;
    private ImageView imageview;
    private EditText editTextEventName, editTextAddress, editTextDes, editTextMax, editTextMin, editTextStart, editTextEnd, editTextCap, editTextCost;
    FirebaseStorage storage;
    StorageReference storageReference;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 101;
    private ArrayList<String> registeredusers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            eventId = extras.getString("eventId");
        }

        editTextEventName = findViewById(R.id.event_name1);
        editTextAddress = findViewById(R.id.event_address1);
        editTextDes = findViewById(R.id.event_description1);
        editTextMax = findViewById(R.id.event_max1);
        editTextMin = findViewById(R.id.event_min1);
        editTextStart = findViewById(R.id.event_start1);
        editTextEnd = findViewById(R.id.event_end1);
        editTextCap = findViewById(R.id.event_capacity1);
        editTextCost = findViewById(R.id.event_cost1);
        imageview = findViewById(R.id.imageView_event_poster);

        Runnable iterateRunnable = () -> FirebaseDatabase.getInstance().getReference("Events").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                // Iterate over all the users(key) in the child users in the db
                for (DataSnapshot userValue : snapshot.getChildren()) {

                    if(userValue.getValue() != null &&
                            userValue.child("eventId").getValue().toString().equals(eventId)) {
                        String name = userValue.child("eventName").getValue().toString();
                        String description = userValue.child("eventDescription").getValue().toString();
                        String eventId = userValue.child("eventId").getValue().toString();

                        hostEmailId = userValue.child("hostEmailId").getValue().toString();

                        editTextEventName.setText(userValue.child("eventName").getValue().toString());
                        editTextAddress.setText(userValue.child("eventAddress").getValue().toString());
                        editTextDes.setText(userValue.child("eventDescription").getValue().toString());
                        editTextMax.setText(userValue.child("maxAgelimit").getValue().toString());
                        editTextMin.setText(userValue.child("minAgelimit").getValue().toString());
                        editTextStart.setText(userValue.child("eventStartDate").getValue().toString());
                        editTextEnd.setText(userValue.child("eventEndDate").getValue().toString());
                        editTextCap.setText(userValue.child("eventUsersMaxCapacity").getValue().toString());
                        editTextCost.setText(userValue.child("eventTicketCost").getValue().toString());

                        registeredusers = (ArrayList<String>) userValue.child("registeredusers").getValue();

                        mStorageStickerReference1 = FirebaseStorage.getInstance().getReference().child("Images/" + eventId);
                        if(mStorageStickerReference1==null){ continue;}
                        File localFileSticker1 = null;
                        try {
                            localFileSticker1 = File.createTempFile("sticker1", "jpg");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        File finalLocalFileSticker = localFileSticker1;
                        mStorageStickerReference1.getFile(localFileSticker1)
                                .addOnSuccessListener(taskSnapshot -> {
                                    Bitmap bitmap1 = BitmapFactory.decodeFile(finalLocalFileSticker.getAbsolutePath());
                                    imageview.setImageBitmap(bitmap1);
                                });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Thread iterateThread = new Thread(iterateRunnable);
        iterateThread.start();

        Button button = findViewById(R.id.submiteditbutton);
        button.setOnClickListener(view -> {
            editsubmit();
            Intent intent  = new Intent(EditEventActivity.this, HostMainActivity.class);
            intent.putExtra("hostemail", hostEmailId);
            finishAffinity();
            startActivity(intent);
            EditEventActivity.this.finish();
        });

        //for delete
        Button button1 = findViewById(R.id.button_delete_event);
        button1.setOnClickListener(v -> deletesubmit());

        Button button2 = findViewById(R.id.textView_upload_image_text);
        button2.setOnClickListener(v -> {
            checkAndRequestPermissions(EditEventActivity.this);
            chooseImage(EditEventActivity.this);
        });
    }

    public void editsubmit(){

        String event_Name = editTextEventName.getText().toString().trim();
        String event_Address = editTextAddress.getText().toString().trim();
        String event_description = editTextDes.getText().toString().trim();
        int event_max = Integer.parseInt(String.valueOf(editTextMax.getText()));
        int event_min = Integer.parseInt(String.valueOf(editTextMin.getText()));
        String event_start = editTextStart.getText().toString().trim();
        String event_end = editTextEnd.getText().toString().trim();
        int event_cap = Integer.parseInt(String.valueOf(editTextCap.getText()));
        int event_cost = Integer.parseInt(String.valueOf(editTextCost.getText()));

        if (event_Name.isEmpty()) {
            editTextEventName.setError("Event Name is Required");
            editTextEventName.requestFocus();
            return;
        }
        if (event_Address.isEmpty()) {
            editTextAddress.setError("Event Address is Required");
            editTextAddress.requestFocus();
            return;
        }
        if (event_description.isEmpty()) {
            editTextDes.setError("Event Description is Required");
            editTextDes.requestFocus();
            return;
        }
        if (Integer.toString(event_min).isEmpty() || event_min <=0) {
            editTextMin.setError("Event Min age is Required and should be greater than 0");
            editTextMin.requestFocus();
            return;
        }
        if (Integer.toString(event_max).isEmpty() || event_max < event_min) {
            editTextMax.setError("Event Max age is Required and greater than min age");
            editTextMax.requestFocus();
            return;
        }
        if (Integer.toString(event_cap).isEmpty() || event_cap <= 0) {
            editTextCap.setError("Event Capacity is Required and should be positive");
            editTextCap.requestFocus();
            return;
        }
        if (Integer.toString(event_cost).isEmpty() || event_cost < 0) {
            editTextCost.setError("Event Cost is Required and should be positive");
            editTextCost.requestFocus();
            return;
        }
        if (event_start.isEmpty()) {
            editTextStart.setError("Event Start date is Required");
            editTextStart.requestFocus();
            return;
        }
        else {
            if (!event_start.matches("^(1[0-9]|0[1-9]|3[0-1]|2[1-9])/(0[1-9]|1[0-2])/[0-9]{4}$")){
                editTextStart.setError("Event Start date format is wrong");
                editTextStart.requestFocus();
                return;
            }
            SimpleDateFormat format=new SimpleDateFormat("dd/MM/yyyy");
            try {
                format.parse(event_start);
            }catch (ParseException e){
                editTextStart.setError("Event Start date format is wrong");
                editTextStart.requestFocus();
                return;
            }
        }
        if (event_end.isEmpty()) {
            editTextEnd.setError("Event End date is Required");
            editTextEnd.requestFocus();
            return;
        }
        else {
            if (!event_end.matches("^(1[0-9]|0[1-9]|3[0-1]|2[1-9])/(0[1-9]|1[0-2])/[0-9]{4}$")){
                editTextEnd.setError("Event Start date format is wrong");
                editTextEnd.requestFocus();
                return;
            }
            SimpleDateFormat format=new SimpleDateFormat("dd/MM/yyyy");
            try {
                format.parse(event_end);
            }catch (ParseException e){
                editTextEnd.setError("Event Start date format is wrong");
                editTextEnd.requestFocus();
                return;
            }
        }

        String[] s = event_start.split("/");
        String[] e = event_end.split("/");

        if(
                !( Integer.parseInt(e[2]) >= Integer.parseInt(s[2]) &&
                        ((Integer.parseInt(e[1]) > Integer.parseInt(s[1]) ) ||
                        (Integer.parseInt(e[1]) == Integer.parseInt(s[1]) && Integer.parseInt(e[0]) >= Integer.parseInt(s[0])) ))
        ){
            editTextEnd.setError("Event End date should be greater than Start date");
            editTextEnd.requestFocus();
            return;
        }

        FirebaseDatabase fireBasedatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRefFireBase = fireBasedatabase.getReferenceFromUrl("https://mobile-finalproject-17b4f-default-rtdb.firebaseio.com/");

        Runnable iterateRunnable2 = () -> FirebaseDatabase.getInstance().getReference("Events").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                // Iterate over all the users(key) in the child users in the db
                for (DataSnapshot userValue : snapshot.getChildren()) {

                    if(userValue.getValue() != null &&
                            userValue.child("eventId").getValue().toString().equals(eventId)) {
                        System.out.println("rao1" + userValue);
                        String hostemail = userValue.child("hostEmailId").getValue().toString();

                        Event event = new Event(hostemail, event_Name, event_Address, event_description, event_start, event_end, event_cost, event_cap, event_min, event_max, registeredusers);
                        event.setEventId(eventId);

                        myRefFireBase.child("Events").child(userValue.getKey()).setValue(event);

                        Runnable uploadImageRunnable = () -> uploadImage(eventId);

                        Thread uploadImageThread = new Thread(uploadImageRunnable);
                        uploadImageThread.start();

                        Toast.makeText(EditEventActivity.this,
                                "Event has been Edited Successfully!",
                                Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

        Thread iterateThread2 = new Thread(iterateRunnable2);
        iterateThread2.start();
    }

    // Need to delete this event for users who registered and notify
    public void deletesubmit(){

        FirebaseDatabase fireBasedatabase = FirebaseDatabase.getInstance();
        DatabaseReference myRefFireBase = fireBasedatabase.getReferenceFromUrl("https://mobile-finalproject-17b4f-default-rtdb.firebaseio.com/");

        Runnable iterateRunnable3 = () -> FirebaseDatabase.getInstance().getReference("Events").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                // Iterate over all the users(key) in the child users in the db
                for (DataSnapshot userValue : snapshot.getChildren()) {

                    if(userValue.getValue() != null &&
                            userValue.child("eventId").getValue().toString().equals(eventId)) {


                        myRefFireBase.child("Events").child(userValue.getKey()).removeValue();

                        Toast.makeText(EditEventActivity.this,
                                "Event has been Removed Successfully!",
                                Toast.LENGTH_LONG).show();

                        Intent intent  = new Intent(EditEventActivity.this, HostMainActivity.class);
                        intent.putExtra("hostemail", hostEmailId);
                        startActivity(intent);
                        EditEventActivity.this.finish();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

        Thread iterateThread3 = new Thread(iterateRunnable3);
        iterateThread3.start();
    }

    // UploadImage method
    private void uploadImage(String eventid) {

        if (filePath != null) {

            // Code for showing progressDialog while uploading
            ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            // Defining the child of storageReference
            StorageReference ref
                    = storageReference
                    .child(
                            "Images/"
                                    + eventid);

            // adding listeners on upload
            // or failure of image
            // Progress Listener for loading
// percentage on the dialog box
            ref.putFile(filePath)
                    .addOnSuccessListener(
                            taskSnapshot -> {

                                // Image uploaded successfully
                                // Dismiss dialog
                                progressDialog.dismiss();
                                Toast
                                        .makeText(EditEventActivity.this,
                                                "Image Uploaded!!",
                                                Toast.LENGTH_SHORT)
                                        .show();
                            })

                    .addOnFailureListener(e -> {

                        // Error, Image not uploaded
                        progressDialog.dismiss();
                        Toast
                                .makeText(EditEventActivity.this,
                                        "Failed " + e.getMessage(),
                                        Toast.LENGTH_SHORT)
                                .show();
                    })
                    .addOnProgressListener(
                            taskSnapshot -> {
                                double progress
                                        = (100.0
                                        * taskSnapshot.getBytesTransferred()
                                        / taskSnapshot.getTotalByteCount());
                                progressDialog.setMessage(
                                        "Uploaded "
                                                + (int)progress + "%");
                            });
        }
    }



    public void chooseImage(Context context){

        Runnable chooseImageRunnable = () -> {

            final CharSequence[] optionsMenu = {"Take Photo", "Choose from Gallery", "Exit" }; // create a menuOption Array
            // create a dialog for showing the optionsMenu
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            // set the items in builder
            builder.setItems(optionsMenu, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if(optionsMenu[i].equals("Take Photo")){
                        // Open the camera and get the photo
                        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        System.out.println("received");
                        startActivityForResult(takePicture, 0);
                        dialogInterface.dismiss();
                        //someActivityResultLauncher.launch(takePicture);
                    }
                    else if(optionsMenu[i].equals("Choose from Gallery")){
                        // choose from  external storage
                        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        System.out.println("received");
                        startActivityForResult(pickPhoto , 1);
                        dialogInterface.dismiss();
                        //someActivityResultLauncher.launch(pickPhoto);
                    }
                    else if (optionsMenu[i].equals("Exit")) {
                        dialogInterface.dismiss();
                    }
                }
            });
            builder.show();

        };

        Thread chooseImageThread = new Thread(chooseImageRunnable);
        chooseImageThread.start();
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
        }catch (Exception e) {
            System.out.println("Exception for OnActivity: " + e);
        }
        Runnable onActivityResultRunnable = () -> {
            try {
                filePath = data.getData();
                if (resultCode != RESULT_CANCELED) {
                    switch (requestCode) {
                        case 0:
                            if (resultCode == RESULT_OK && data != null) {
                                Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                                imageview.setImageBitmap(selectedImage);
                            }
                            break;
                        case 1:
                            if (resultCode == RESULT_OK && data != null) {
                                Uri selectedImage = data.getData();
                                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                                if (selectedImage != null) {
                                    Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                                    if (cursor != null) {
                                        cursor.moveToFirst();
                                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                        String picturePath = cursor.getString(columnIndex);
                                        imageview.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                        cursor.close();
                                    }
                                }
                            }
                            break;
                    }
                }
            } catch (Exception e) {
                System.out.println("Can't upload empty Posters!");
            }
        };

        Thread onActivityResultThread = new Thread(onActivityResultRunnable);
        onActivityResultThread.start();
    }


    //check permissions
    public static boolean checkAndRequestPermissions(final Activity context) {
        int WExtstorePermission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int cameraPermission = ContextCompat.checkSelfPermission(context,
                Manifest.permission.CAMERA);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (WExtstorePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded
                    .add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(context, listPermissionsNeeded
                            .toArray(new String[listPermissionsNeeded.size()]),
                    REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }


    // Handled permission Result
    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS:
                if (ContextCompat.checkSelfPermission(EditEventActivity.this,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(),
                                    "FlagUp Requires Access to Camara.", Toast.LENGTH_SHORT)
                            .show();
                } else if (ContextCompat.checkSelfPermission(EditEventActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(),
                            "FlagUp Requires Access to Your Storage.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    chooseImage(EditEventActivity.this);
                }
                break;
        }
    }
}