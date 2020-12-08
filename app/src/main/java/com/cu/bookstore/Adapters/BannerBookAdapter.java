package com.cu.bookstore.Adapters;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.cu.bookstore.BookActivity;
import com.cu.bookstore.Model.Book;
import com.cu.bookstore.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class BannerBookAdapter extends PagerAdapter {
    Context context;
    ArrayList<Book> books;

    public BannerBookAdapter(Context context, ArrayList<Book> books) {
        this.context = context;
        this.books = books;
    }

    @Override
    public int getCount() {
        return books.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {

        View view= LayoutInflater.from(context).inflate(R.layout.banner_book_layout,null);
        ImageView bannerImage=view.findViewById(R.id.bannerImage);
        try {
            if (!TextUtils.isEmpty(books.get(position).getId())) {
                Picasso.get().load(books.get(position).getImage()).error(R.drawable.ic_launcher_foreground).into(bannerImage);
                bannerImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            Intent intent = new Intent(context, BookActivity.class);
                            intent.putExtra("id", books.get(position).getId());
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        } catch (Exception e) {
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        }
        catch (Exception e){
                Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        container.addView(view);
        return view;
    }
}
