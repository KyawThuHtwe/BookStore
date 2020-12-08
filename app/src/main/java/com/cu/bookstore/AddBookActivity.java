package com.cu.bookstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AddBookActivity extends AppCompatActivity {

    EditText book_name,writer_name,history,year,url,direct_url;
    Spinner book_type;
    Button add_book_image,add_book,book_testing;
    ImageView book_image_src;
    private static final int GalleryPick=1;
    private Uri imageUri;
    private String bookRandomKey,downloadImageUrl;
    StorageReference bookImageRef;
    DatabaseReference bookRef;
    ProgressDialog loadingBar;
    String name,type,writer,historys,years,saveCurrentDate,saveCurrentTime,urls,direct_urls;
    String[] array=new String[]{"နည်းပညာ စာအုပ်များ","ဘာသာရေး စာအုပ်များ","စီးပွားရေးအတွေးအခေါ်  စာအုပ်များ"
    ,"သမိုင်း စာအုပ်များ","သင်ကြားရေးဆိုင်ရာ စာအုပ်များ","စုံထောက် ဝတ္ထုများ","သိုင်း ဝတ္ထုများ","ရသ စာပေများ","နိုင်ငံခြား ဘာသာစကား စာအုပ်များ"
    ,"ကျန်းမာရေးလမ်းညွန် စာအုပ်များ","ကဗျာပေါင်းချုပ် စာအုပ်များ","လုံးချင်း ဝတ္ထုများ","သည်းထိတ်ရင်ဖို စာအုပ်များ","စိုက်ပျိုးရေး ဗဟုသုတ စာအုပ်များ"
    ,"ဘာသာပြန် စာအုပ်များ","မဂ္ဂဇင်း နှင့် စာစဥ်များ","တခြား စာအုပ်များ"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        book_image_src=findViewById(R.id.book_image_src);
        book_name=findViewById(R.id.book_name);
        book_type=findViewById(R.id.book_type);
        writer_name=findViewById(R.id.writer_name);
        history=findViewById(R.id.history);
        year=findViewById(R.id.year);
        url=findViewById(R.id.url);
        direct_url=findViewById(R.id.direct_url);
        add_book_image=findViewById(R.id.add_book_image);
        add_book=findViewById(R.id.add_book);
        book_testing=findViewById(R.id.book_testing);

        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,array);
        book_type.setAdapter(arrayAdapter);

        loadingBar=new ProgressDialog(this);
        bookImageRef= FirebaseStorage.getInstance().getReference().child("Book Images");
        bookRef= FirebaseDatabase.getInstance().getReference().child("Book");

        add_book_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        add_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateProductData(1);
            }
        });

        book_testing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateProductData(0);
            }
        });
    }

    private void validateProductData(int i) {
        name=book_name.getText().toString();
        type=book_type.getSelectedItem().toString();
        writer=writer_name.getText().toString();
        historys=history.getText().toString();
        years=year.getText().toString();
        urls=url.getText().toString();
        direct_urls=direct_url.getText().toString();
        if(imageUri==null){
            Toast.makeText(getApplicationContext(),"Book Image is mandatory",Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(name)){
            Toast.makeText(getApplicationContext(),"Please write your name",Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(type)){
            Toast.makeText(getApplicationContext(),"Please write your Book description",Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(writer)){
            Toast.makeText(getApplicationContext(),"Please write Writer Name",Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(historys)){
            Toast.makeText(getApplicationContext(),"Please write Writer History",Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(years)){
            Toast.makeText(getApplicationContext(),"Please write Book Produce Year",Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(urls)){
            Toast.makeText(getApplicationContext(),"Please write Book Url",Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(direct_urls)){
            Toast.makeText(getApplicationContext(),"Please write Book Direct Url",Toast.LENGTH_SHORT).show();
        }else {
            if(i==1){
                storeBookInformation();
            }else{
                testing();
            }

        }
    }

    private void testing() {
        final AlertDialog.Builder builder=new AlertDialog.Builder(AddBookActivity.this);
        View view= LayoutInflater.from(AddBookActivity.this).inflate(R.layout.test_layout,null);
        final TextView book_name=view.findViewById(R.id.book_name);
        final TextView writer_name=view.findViewById(R.id.writer_name);
        final TextView history=view.findViewById(R.id.history);
        final TextView produce=view.findViewById(R.id.produce);
        Button complete=view.findViewById(R.id.complete);

        book_name.setText(name);
        writer_name.setText(writer);
        history.setText(historys);
        produce.setText(years);

        builder.setView(view);
        final AlertDialog alertDialog=builder.create();
        complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
        alertDialog.setCancelable(false);
    }

    private void storeBookInformation() {
        loadingBar.setTitle("Add New Book");
        loadingBar.setMessage("Dear Admin, please wait while we are adding the new book.");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        Calendar calendar=Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat currentDate=new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate=currentDate.format(calendar.getTime());

        @SuppressLint("SimpleDateFormat") SimpleDateFormat currentTime=new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime=currentTime.format(calendar.getTime());

        int max=1000;
        int min=0;
        int random1= (int) (Math.random()*(max-min+1)+min);
        int random2= (int) (Math.random()*(max-min+1)+min);
        int random3= (int) (Math.random()*(max-min+1)+min);
        int random4= (int) (Math.random()*(max-min+1)+min);
        int random5= (int) (Math.random()*(max-min+1)+min);
        bookRandomKey=saveCurrentDate+" "+saveCurrentTime+" "+random1+""+random2+""+random3+""+random4+""+random5;
        final StorageReference filePaths=bookImageRef.child(imageUri.getLastPathSegment()+bookRandomKey);
        final UploadTask uploadTask=filePaths.putFile(imageUri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Error : "+e.toString(),Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getApplicationContext(),"Product Image Uploaded Successfully...",Toast.LENGTH_SHORT).show();
                Task<Uri> urlTask=uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if(!task.isSuccessful()){
                            throw task.getException();
                        }
                        return filePaths.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()){
                            downloadImageUrl=task.getResult().toString();
                            saveBookInfoToDatabase();
                        }
                    }
                });
            }
        });
    }

    private void saveBookInfoToDatabase() {
        HashMap<String,Object> productMap=new HashMap<>();
        productMap.put("id",bookRandomKey);
        productMap.put("name",name);
        productMap.put("image",downloadImageUrl);
        productMap.put("type",type);
        productMap.put("writer",writer);
        productMap.put("history",historys);
        productMap.put("produce",years);
        productMap.put("url",urls);
        productMap.put("direct_url",direct_urls);
        bookRef.child(bookRandomKey).updateChildren(productMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    loadingBar.dismiss();
                    finish();
                    Toast.makeText(getApplicationContext(),"Book is added successfully...",Toast.LENGTH_SHORT).show();
                }else {
                    loadingBar.dismiss();
                    Toast.makeText(getApplicationContext(),"Error : "+task.getException(),Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                loadingBar.dismiss();
                Toast.makeText(getApplicationContext(),"Error : "+e.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), GalleryPick);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GalleryPick && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            imageUri = data.getData();
            book_image_src.setImageURI(imageUri);
        }
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}