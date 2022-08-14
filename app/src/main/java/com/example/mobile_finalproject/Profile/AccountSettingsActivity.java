package com.example.mobile_finalproject.Profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.mobile_finalproject.Profile.AccountSettings.DeactivateAccountActivity;
import com.example.mobile_finalproject.Profile.AccountSettings.UpdateNameActivity;
import com.example.mobile_finalproject.Profile.AccountSettings.UpdatePasswordActivity;
import com.example.mobile_finalproject.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import android.Manifest;
import android.annotation.SuppressLint;
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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AccountSettingsActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView userNameValue, emailValue, passwordValue, closeAccountText;
    // Arrows to redirect to next activity
    private TextView arrow1_name, arrow2_password, arrow3_closeAccount;

    private FirebaseUser user;
    private Uri filePath;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 101;
    FirebaseStorage storage;
    StorageReference storageReference;
    ImageView user_profile_image;
    String emailforimage;
    private StorageReference mStorageStickerReference1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_settings);

        user = FirebaseAuth.getInstance().getCurrentUser();

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        userNameValue = findViewById(R.id.textView_User_Name_Value);
        //userNameValue.setText(user.getDisplayName());
        userNameValue.setOnClickListener(this);
        arrow1_name = findViewById(R.id.arrow1_name);
        arrow1_name.setOnClickListener(this);

        emailValue = findViewById(R.id.textView_Email_Value);

        passwordValue = findViewById(R.id.textView_Password_Value);
        passwordValue.setOnClickListener(this);
        arrow2_password = findViewById(R.id.arrow2_password);
        arrow2_password.setOnClickListener(this);

        closeAccountText = findViewById(R.id.textView_Close_Account_Value);
        closeAccountText.setOnClickListener(this);
        arrow3_closeAccount = findViewById(R.id.arrow3_closeAccount);
        arrow3_closeAccount.setOnClickListener(this);


        user_profile_image = findViewById(R.id.user_profile_image);
        displayNameEmail();

        Button b = findViewById(R.id.button_Update_picture);
        b.setOnClickListener(v -> {

            System.out.println("raoooooooooooooooooooooooooooooooooooooooooooo");
            checkAndRequestPermissions(AccountSettingsActivity.this);
            chooseImage(AccountSettingsActivity.this);
            System.out.println("lll" + filePath);
            uploadImage();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        userNameValue.setText(user.getDisplayName());
    }

    // Helper method to display the name and email of the logged in user
    private void displayNameEmail() {

        String name = user.getDisplayName();

        if (name == null) {
            for (UserInfo userInfo : user.getProviderData()) {
                if (userInfo.getDisplayName() != null) {
                    name = userInfo.getDisplayName();
                }
            }
        }
        userNameValue.setText(name);

        String email = user.getEmail();
        emailforimage = email;
        emailValue.setText(email);


        mStorageStickerReference1 = FirebaseStorage.getInstance().getReference().child("Profiles/" + email);
        if(mStorageStickerReference1!=null) {
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
                        user_profile_image.setImageBitmap(bitmap1);
                    });
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            // Updating User Name
            case R.id.arrow1_name:
            case R.id.textView_User_Name_Value:
                startActivity(new Intent(this, UpdateNameActivity.class));
                break;

            // Updating user's password
            case R.id.textView_Password_Value:
            case R.id.arrow2_password:
                startActivity(new Intent(this, UpdatePasswordActivity.class));
                break;

            // Deactivating user's account
            case R.id.textView_Close_Account_Value:
            case R.id.arrow3_closeAccount:
                startActivity(new Intent(this, DeactivateAccountActivity.class));
                break;
        }
    }




    // UploadImage method
    private void uploadImage()
    {

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
                            "Profiles/"
                                    + emailforimage);

            // adding listeners on upload
            // or failure of image
            System.out.println(ref + "ref------------------------------");
            ref.putFile(filePath)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {

                                    // Image uploaded successfully
                                    // Dismiss dialog
                                    progressDialog.dismiss();
                                    Toast
                                            .makeText(AccountSettingsActivity.this,
                                                    "Image Uploaded!!",
                                                    Toast.LENGTH_SHORT)
                                            .show();
                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {

                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast
                                    .makeText(AccountSettingsActivity.this,
                                            "Failed " + e.getMessage(),
                                            Toast.LENGTH_SHORT)
                                    .show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {

                                // Progress Listener for loading
                                // percentage on the dialog box
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {
                                    double progress
                                            = (100.0
                                            * taskSnapshot.getBytesTransferred()
                                            / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage(
                                            "Uploaded "
                                                    + (int)progress + "%");
                                }
                            });
        }
        System.out.println("-----------------------------------------------------");
    }



    public void chooseImage(Context context){


        final CharSequence[] optionsMenu = {"Take Photo", "Choose from Gallery", "Exit" }; // create a menuOption Array
        // create a dialog for showing the optionsMenu
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        // set the items in builder
        builder.setItems(optionsMenu, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(optionsMenu[i].equals("Take Photo")){
                    // Open the camera and get the photo
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    System.out.println("received");
                    startActivityForResult(takePicture, 0);
                    //someActivityResultLauncher.launch(takePicture);
                    dialogInterface.dismiss();
                }
                else if(optionsMenu[i].equals("Choose from Gallery")){
                    // choose from  external storage
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    System.out.println("received");
                    startActivityForResult(pickPhoto , 1);
                    //someActivityResultLauncher.launch(pickPhoto);
                    dialogInterface.dismiss();
                }
                else if (optionsMenu[i].equals("Exit")) {
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        filePath = data.getData();
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        user_profile_image.setImageBitmap(selectedImage);
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
                                user_profile_image.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                cursor.close();
                            }
                        }
                    }
                    break;
            }
        }
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
                if (ContextCompat.checkSelfPermission(AccountSettingsActivity.this,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(),
                                    "FlagUp Requires Access to Camara.", Toast.LENGTH_SHORT)
                            .show();
                } else if (ContextCompat.checkSelfPermission(AccountSettingsActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(),
                            "FlagUp Requires Access to Your Storage.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    chooseImage(AccountSettingsActivity.this);
                }
                break;
        }
    }
}