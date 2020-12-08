package com.cu.bookstore.Fragments;

import android.os.Bundle;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.cu.bookstore.Adapters.SearchAdapter;
import com.cu.bookstore.Model.Book;
import com.cu.bookstore.Model.Search;
import com.cu.bookstore.R;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchFragment extends Fragment {

    RecyclerView recyclerView;
    SearchView search_value;
    ChipGroup choiceChipGroup;
    ArrayList<Search> search_list;
    String type="အားလုံး";
    String[] array=new String[]{"အားလုံး","နည်းပညာ စာအုပ်များ","ဘာသာရေး စာအုပ်များ","စီးပွားရေးအတွေးအခေါ်  စာအုပ်များ"
            ,"သမိုင်း စာအုပ်များ","သင်ကြားရေးဆိုင်ရာ စာအုပ်များ","စုံထောက် ဝတ္ထုများ","သိုင်း ဝတ္ထုများ","ရသ စာပေများ","နိုင်ငံခြား ဘာသာစကား စာအုပ်များ"
            ,"ကျန်းမာရေးလမ်းညွန် စာအုပ်များ","ကဗျာပေါင်းချုပ် စာအုပ်များ","လုံးချင်း ဝတ္ထုများ","သည်းထိတ်ရင်ဖို စာအုပ်များ","စိုက်ပျိုးရေး ဗဟုသုတ စာအုပ်များ"
            ,"ဘာသာပြန် စာအုပ်များ","မဂ္ဂဇင်း နှင့် စာစဥ်များ","တခြား စာအုပ်များ"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_search, container, false);
        search_value=view.findViewById(R.id.search_value);

        recyclerView=view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        search_value.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(TextUtils.isEmpty(newText)){
                    recyclerView.setAdapter(null);
                    choiceChipGroup.setVisibility(View.VISIBLE);
                }else{
                    choiceChipGroup.setVisibility(View.GONE);
                    search(type,newText);
                }
                return false;
            }
        });
        choiceChipGroup = view.findViewById(R.id.choice_chip_group);
        choiceChipGroup.check(R.id.choice_chip1);
        choiceChipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup chipGroup, @IdRes int i) {
                switch (chipGroup.getCheckedChipId()){
                    case R.id.choice_chip1:
                        type=array[0];
                        break;
                    case R.id.choice_chip2:
                        type=array[1];
                        break;
                    case R.id.choice_chip3:
                        type=array[2];
                        break;
                    case R.id.choice_chip4:
                        type=array[3];
                        break;
                    case R.id.choice_chip5:
                        type=array[4];
                        break;
                    case R.id.choice_chip6:
                        type=array[5];
                        break;
                    case R.id.choice_chip7:
                        type=array[6];
                        break;
                    case R.id.choice_chip8:
                        type=array[7];
                        break;
                    case R.id.choice_chip9:
                        type=array[8];
                        break;
                    case R.id.choice_chip10:
                        type=array[9];
                        break;
                    case R.id.choice_chip11:
                        type=array[10];
                        break;
                    case R.id.choice_chip12:
                        type=array[11];
                        break;
                    case R.id.choice_chip13:
                        type=array[12];
                        break;
                    case R.id.choice_chip14:
                        type=array[13];
                        break;
                    case R.id.choice_chip15:
                        type=array[14];
                        break;
                    case R.id.choice_chip16:
                        type=array[15];
                        break;
                    case R.id.choice_chip17:
                        type=array[16];
                        break;
                    case R.id.choice_chip18:
                        type=array[17];
                        break;
                }
            }
        });
        return view;
    }
    public void search(final String type, final String searchInput){
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference();
        reference.child("Book").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        search_list=new ArrayList<>();
                        search_list.clear();
                        if(snapshot.exists()){
                            for(DataSnapshot snap:snapshot.getChildren()){
                                Book book=snap.getValue(Book.class);
                                if(type.equals("အားလုံး")){
                                    if(book.getName().toLowerCase().contains(searchInput.toLowerCase())){
                                        search_list.add(new Search(book.getId(),book.getName()));
                                        SearchAdapter searchAdapter=new SearchAdapter(getContext(),search_list);
                                        recyclerView.setAdapter(searchAdapter);
                                    }
                                }else{
                                    if(book.getType().equals(type) && book.getName().toLowerCase().contains(searchInput.toLowerCase())){
                                        search_list.add(new Search(book.getId(),book.getName()));
                                        SearchAdapter searchAdapter=new SearchAdapter(getContext(),search_list);
                                        recyclerView.setAdapter(searchAdapter);
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }
}