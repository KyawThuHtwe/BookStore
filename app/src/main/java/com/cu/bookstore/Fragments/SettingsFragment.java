package com.cu.bookstore.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cu.bookstore.Adapters.BookAdapter;
import com.cu.bookstore.AddBookActivity;
import com.cu.bookstore.Model.Book;
import com.cu.bookstore.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

public class SettingsFragment extends Fragment {

    LinearLayout add_book,admin,admin_view,admin_logout;
    TextView account_id;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        add_book=view.findViewById(R.id.add_book);
        add_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AddBookActivity.class));
            }
        });
        admin=view.findViewById(R.id.admin);
        admin_view=view.findViewById(R.id.admin_view);
        if(hasLogin()){
            admin_view.setVisibility(View.VISIBLE);
        }else {
            admin_view.setVisibility(View.GONE);
        }
        admin_logout=view.findViewById(R.id.logout);
        admin_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                admin_view.setVisibility(View.GONE);
                saveLogin(false);
            }
        });

        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!hasLogin()){
                    adminLogin(v);
                }
            }
        });
        account_id=view.findViewById(R.id.account_id);
        account_id.setText("id:"+has());
        return view;
    }
    public void adminLogin(View v){
        final AlertDialog.Builder builder=new AlertDialog.Builder(v.getContext());
        View view= LayoutInflater.from(v.getContext()).inflate(R.layout.admin_login_layout,null);
        final TextView name=view.findViewById(R.id.name);
        final TextView password=view.findViewById(R.id.password);
        Button cancel=view.findViewById(R.id.cancel);
        Button login=view.findViewById(R.id.login);
        builder.setView(view);
        final AlertDialog alertDialog=builder.create();
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(name.getText().toString())){
                    Toast.makeText(getContext(),"Please Fill Name",Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(password.getText().toString())){
                    Toast.makeText(getContext(),"Please Fill Password",Toast.LENGTH_SHORT).show();
                }else{
                    if(name.getText().toString().equals("kyawthuhtwe") && password.getText().toString().equals("12345678")){
                        admin_view.setVisibility(View.VISIBLE);
                        saveLogin(true);
                        alertDialog.dismiss();
                    }else{
                        Toast.makeText(getContext(),"Fail, Try Again",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        alertDialog.show();
        alertDialog.setCancelable(false);
    }
    public String has(){
        SharedPreferences preferences=getActivity().getSharedPreferences("ID", Context.MODE_PRIVATE);
        return preferences.getString("ID","");
    }
    public void saveLogin(boolean res){
        SharedPreferences preferences=getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putBoolean("Login",res).apply();
    }
    public boolean hasLogin(){
        SharedPreferences preferences=getActivity().getSharedPreferences("Login", Context.MODE_PRIVATE);
        return preferences.getBoolean("Login",false);
    }
}