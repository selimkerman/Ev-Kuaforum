package com.abaskan.evkuaforum.BarberActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.abaskan.evkuaforum.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BarberAddPhotoActivity extends AppCompatActivity {
    Toolbar barberAddPhotoToolbar;
    ImageView barberAddPhotoImageview;
    Uri imageData;
    Bitmap selectedImage;
    Button barberAddPhotoButton;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference,getPhotoFromStorage;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barber_add_photo);
        findViewById();
        toolbar();
    }

    public void toolbar(){
        barberAddPhotoToolbar = findViewById(R.id.barberAddPhotoToolbar);
        barberAddPhotoToolbar.setTitle("Fotoğraf Ekle");
        barberAddPhotoToolbar.setNavigationIcon(R.drawable.icon_arrow_back);
        barberAddPhotoToolbar.setTitleTextColor(Color.WHITE);
        barberAddPhotoToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void findViewById(){
        barberAddPhotoImageview = findViewById(R.id.barberAddPhotoImageview);
        barberAddPhotoButton = findViewById(R.id.barberAddPhotoButton);
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        getPhotoFromStorage = firebaseStorage.getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
    }

    public void barberAddPhotoButtonClick(View view){
        if(imageData != null){

            UUID uuid = UUID.randomUUID();
            final String imageName = "images" + uuid + ".jpg";

            storageReference.child("barberImages").child(firebaseAuth.getUid()).child(imageName).putFile(imageData).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    getPhotoFromStorage = getPhotoFromStorage.child("barberImages").child(firebaseAuth.getUid()).child(imageName);
                    getPhotoFromStorage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String downloadUrl = uri.toString();
                            Map Url = new HashMap();
                            Url.put("barberImageUrl",downloadUrl);
                            firebaseFirestore.collection("Barbers").document(firebaseAuth.getUid())
                                    .update(Url);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });

                    Toast.makeText(BarberAddPhotoActivity.this,"Fotoğraf Kaydedildi",Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(BarberAddPhotoActivity.this,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                }
            });
        }



    }

    public void barberSelectImageClick(View view){
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
    }else {
            Intent intentToGallery = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intentToGallery,2);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intentToGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intentToGallery,2);
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 2 && resultCode == RESULT_OK && data != null){
            imageData = data.getData();

            try {

                if(Build.VERSION.SDK_INT >= 28){
                    ImageDecoder.Source source = ImageDecoder.createSource(this.getContentResolver(),imageData);
                    selectedImage = ImageDecoder.decodeBitmap(source);
                    barberAddPhotoImageview.setImageBitmap(selectedImage);
                } else {
                    selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(),imageData);
                    barberAddPhotoImageview.setImageBitmap(selectedImage);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
