package com.cu.bookstore;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cu.bookstore.Adapters.BookAdapter;
import com.cu.bookstore.Download.DownloadTask;
import com.cu.bookstore.Model.Book;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class BookActivity extends AppCompatActivity {

    private Button btnDownload,btnView;
    String URL ="", DIRECT_URL="", TITLE="", ID="", WRITER="";

    RecyclerView recyclerView;
    ArrayList<Book> books;
    TextView book_name,book_type,writer,history,produce;
    ImageView book_image;
    FloatingActionButton favorite_border,favorite;
    boolean is_fav=false;

    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        ID=getIntent().getStringExtra("id");

        book_name=findViewById(R.id.book_name);
        book_type=findViewById(R.id.book_type);
        writer=findViewById(R.id.writer_name);
        history=findViewById(R.id.history);
        produce=findViewById(R.id.produce);
        book_image=findViewById(R.id.book_image);

        final DatabaseReference rootRef;
        rootRef= FirebaseDatabase.getInstance().getReference().child("Book");
        rootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    books=new ArrayList<>();
                    books.clear();
                    for(DataSnapshot snap:snapshot.getChildren()){
                        Book book=snap.getValue(Book.class);
                        if(book.getId().equals(ID)) {
                            ID=book.getId();
                            URL=book.getUrl();
                            DIRECT_URL=book.getDirect_url();
                            TITLE=book.getName();
                            Toolbar toolbar=findViewById(R.id.toolbar);
                            setSupportActionBar(toolbar);
                            getSupportActionBar().setTitle(book.getName());
                            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                            Picasso.get().load(book.getImage()).error(R.drawable.ic_launcher_foreground).into(book_image);
                            book_type.setText(book.getType());
                            writer.setText(book.getWriter());
                            history.setText(book.getHistory());
                            produce.setText(book.getProduce());
                            otherBook(book.getWriter());
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
        btnDownload = findViewById(R.id.btnDownload);
        btnDownload.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                new DownloadTask(BookActivity.this, DIRECT_URL,TITLE);

            }
        });
        btnView = findViewById(R.id.btnView);
        btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),PdfReaderActivity.class);
                intent.putExtra("url",URL);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));

        favorite=findViewById(R.id.favorite);
        favorite_border=findViewById(R.id.favorite_border);

        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRemove(ID);
                favorite_border.setVisibility(View.VISIBLE);
                favorite.setVisibility(View.GONE);
            }
        });
        favorite_border.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFavorite(ID);
                favorite.setVisibility(View.VISIBLE);
                favorite_border.setVisibility(View.GONE);
            }
        });
        DatabaseReference favRef= FirebaseDatabase.getInstance().getReference().child("Favorite");
        favRef.child(has()).child(ID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            is_fav=true;
                            favorite.setVisibility(View.VISIBLE);
                            favorite_border.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        if(is_fav){
            favorite.setVisibility(View.VISIBLE);
            favorite_border.setVisibility(View.GONE);
        }else {
            favorite_border.setVisibility(View.VISIBLE);
            favorite.setVisibility(View.GONE);
        }

    }

    private void addFavorite(final String id) {
        final DatabaseReference favRef= FirebaseDatabase.getInstance().getReference();
        HashMap<String,Object> favMap=new HashMap<>();
        favMap.put(id,id);
        favRef.child("Favorite").child(has()).updateChildren(favMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            favRef.child("Popular").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(!snapshot.child(id).exists()){
                                        HashMap<String,Object> popularMap=new HashMap<>();
                                        popularMap.put("id",id);
                                        favRef.child("Popular").child(id).updateChildren(popularMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                    }
                });
    }
    private void addRemove(String id) {
        DatabaseReference favRef= FirebaseDatabase.getInstance().getReference().child("Favorite");
        favRef.child(has()).child(id)
                .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //Toast.makeText(getApplicationContext(),"Remove Favorite",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void otherBook(final String writer){
        final DatabaseReference rootRef;
        rootRef= FirebaseDatabase.getInstance().getReference().child("Book");
        rootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    books=new ArrayList<>();
                    books.clear();
                    for(DataSnapshot snap:snapshot.getChildren()){
                        Book book=snap.getValue(Book.class);
                        if(book.getWriter().equals(writer)) {
                            books.add(book);
                        }
                    }
                    BookAdapter bookAdapter=new BookAdapter(books,getApplicationContext());
                    recyclerView.setAdapter(bookAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }

    public String has(){
        SharedPreferences preferences=getSharedPreferences("ID", Context.MODE_PRIVATE);
        return preferences.getString("ID","");
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}