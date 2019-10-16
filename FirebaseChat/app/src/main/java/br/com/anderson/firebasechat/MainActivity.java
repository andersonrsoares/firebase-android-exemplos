package br.com.anderson.firebasechat;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import br.com.anderson.firebasechat.adapter.SliderAdapter;
import br.com.anderson.firebasechat.fragment.FragmentGrupos;
import br.com.anderson.firebasechat.fragment.FragmentUsuarios;

public class MainActivity extends AppCompatActivity {


    private CoordinatorLayout activitymain;
    private androidx.appcompat.widget.Toolbar toolbar;
    private ViewPager pager;
    FloatingActionButton group;
    FloatingActionButton file;
    TabLayout tabLayout;
    SliderAdapter sliderAdapter;

    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.pager = (ViewPager) findViewById(R.id.pager);
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.activitymain = (CoordinatorLayout) findViewById(R.id.activity_main);
        this.group = (FloatingActionButton) findViewById(R.id.group);
        this.file = (FloatingActionButton) findViewById(R.id.file);

        tabLayout  = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        sliderAdapter = new SliderAdapter(getSupportFragmentManager());
        sliderAdapter.addFragment(new FragmentGrupos(),"CONVERSAS");
        sliderAdapter.addFragment(new FragmentUsuarios(),"USUARIOS");
        pager.setAdapter(sliderAdapter);

        tabLayout.setupWithViewPager(pager);
//        tabLayout.addTab(tabLayout.newTab());
//        tabLayout.addTab(tabLayout.newTab());
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        this.group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // get prompts.xml view
                LayoutInflater li = LayoutInflater.from(MainActivity.this);
                View promptsView = li.inflate(R.layout.prompts, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        MainActivity.this);

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(promptsView);

                final EditText userInput = (EditText) promptsView
                        .findViewById(R.id.editTextDialogUserInput);

                // set dialog message
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int i) {
                                        // get user input and set it to result
                                        // edit text
//                                        String id = UUID.randomUUID().toString();
//                                        Map<String,Object> chat = new HashMap<>();
//                                        chat.put("single",false);
//                                        chat.put("name",userInput.getText());
//                                        chat.put(token,true);

                                        String id = UUID.randomUUID().toString();
                                        Map<String,Object> chat = new HashMap<>();
                                        chat.put("single",false);
                                        chat.put("name",userInput.getText().toString());
                                        chat.put(token,true);
                                       // database.child("group").child(id).setValue(chat);
                                    //    mFirebaseDatabaseReference.child("group").child(id).setValue(chat);
                                        database.child("group").child(id).setValue(chat, new DatabaseReference.CompletionListener() {
                                            @Override
                                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                                Log.d("CompletionListener", "onComplete: ");
                                            }
                                        });
                                    }
                                })
                        .setNegativeButton("Cancelar",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();

            }
        });

        this.file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchGetPictureIntent();
            }
        });
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        token = preferences.getString("token","");
        database = FirebaseDatabase.getInstance().getReference();
    }
    String token;

    private void firebase(Uri uri)throws IOException {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference imagesRef = storageRef.child("images");
        // Create a reference to 'images/mountains.jpg'
        final StorageReference mountainImagesRef = storageRef.child("images/" + new Date().getTime() + ".jpg");

//
//        final BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = true;
//        // Calculate inSampleSize
//        options.inSampleSize = 2;
//
//        // Decode bitmap with inSampleSize set
//        options.inJustDecodeBounds = false;
//        String path = uri.getPath();
//        Bitmap bitmap  = BitmapFactory.decodeFile(path, options);
//
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//        byte[] data = baos.toByteArray();


        InputStream iStream =   getContentResolver().openInputStream(uri);
        byte[] inputData = getBytes(iStream);

        UploadTask uploadTask = mountainImagesRef.putBytes(inputData);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Log.e("UploadTask", "onFailure: ",exception);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                String path =taskSnapshot.getMetadata().getPath();
                Uri downloadUrl = taskSnapshot. getUploadSessionUri();
                Log.i("UploadTask", "onSuccess: " +downloadUrl);

                mountainImagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String url = uri.toString();
                        Log.i("UploadTask", "onSuccess: " +url);
                    }
                });

            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                Log.i("UploadTask", "onProgress: " +taskSnapshot.getBytesTransferred());
            }
        });
    }


    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    private void dispatchGetPictureIntent() {
        Intent intent = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 888);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 888 && resultCode == Activity.RESULT_OK && data != null){
            Uri selectedImage = data.getData();
//            Log.e("onActivityResult", selectedImage.toString());
//
//            String[] filePathColumn = { MediaStore.Images.Media.DATA };
//            Cursor cursor = this.getContentResolver().query(selectedImage,filePathColumn, null, null, null);
//            cursor.moveToFirst();
//
//            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//            String picturePath = cursor.getString(columnIndex);
//            cursor.close();
//            Uri uri = new Uri.Builder().scheme("file").path(picturePath).build();
            try {
                firebase(selectedImage);
            }catch (Exception ex){
                ex.printStackTrace();
            }


        }
    }

}
