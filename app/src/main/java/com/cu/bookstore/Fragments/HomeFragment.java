package com.cu.bookstore.Fragments;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.cu.bookstore.Adapters.BannerBookAdapter;
import com.cu.bookstore.Adapters.BookAdapter;
import com.cu.bookstore.Adapters.FrameAdapter;
import com.cu.bookstore.Adapters.SearchAdapter;
import com.cu.bookstore.Model.Book;
import com.cu.bookstore.Model.Search;
import com.cu.bookstore.R;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

public class HomeFragment extends Fragment {

    RecyclerView recyclerView,popular_recyclerView,search_recyclerView;
    ArrayList<Book> bannerBook=new ArrayList<>(),popular=new ArrayList<>(),books=new ArrayList<>();
    ViewPager viewPager;
    BannerBookAdapter bannerBookAdapter;
    TabLayout tabLayout;
    private Handler sliderHandler=new Handler();

    SearchView search_value;
    ArrayList<Search> search_list;
    LinearLayout action_view;
    RecyclerView recyclerView_frame,popular_recyclerView_frame;


    String[] array=new String[]{"အားလုံး","နည်းပညာ စာအုပ်များ","ဘာသာရေး စာအုပ်များ","စီးပွားရေးအတွေးအခေါ်  စာအုပ်များ"
            ,"သမိုင်း စာအုပ်များ","သင်ကြားရေးဆိုင်ရာ စာအုပ်များ","စုံထောက် ဝတ္ထုများ","သိုင်း ဝတ္ထုများ","ရသ စာပေများ","နိုင်ငံခြား ဘာသာစကား စာအုပ်များ"
            ,"ကျန်းမာရေးလမ်းညွန် စာအုပ်များ","ကဗျာပေါင်းချုပ် စာအုပ်များ","လုံးချင်း ဝတ္ထုများ","သည်းထိတ်ရင်ဖို စာအုပ်များ","စိုက်ပျိုးရေး ဗဟုသုတ စာအုပ်များ"
            ,"ဘာသာပြန် စာအုပ်များ","မဂ္ဂဇင်း နှင့် စာစဥ်များ","တခြား စာအုပ်များ"};

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_home, container, false);

        action_view=view.findViewById(R.id.action_view);

        popular_recyclerView=view.findViewById(R.id.popular_recyclerView);
        popular_recyclerView.setHasFixedSize(true);
        popular_recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));

        recyclerView=view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3,GridLayoutManager.VERTICAL,false));

        popular_recyclerView_frame=view.findViewById(R.id.popular_recyclerView_frame);
        popular_recyclerView_frame.setHasFixedSize(true);
        popular_recyclerView_frame.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));

        recyclerView_frame=view.findViewById(R.id.recyclerView_frame);
        recyclerView_frame.setHasFixedSize(true);
        recyclerView_frame.setLayoutManager(new GridLayoutManager(getContext(),3,GridLayoutManager.VERTICAL,false));


        viewPager=view.findViewById(R.id.banner_viewPager);
        tabLayout=view.findViewById(R.id.tabLayout);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable, 3000);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        tabLayout.setupWithViewPager(viewPager);


        search_value=view.findViewById(R.id.search_value);

        search_recyclerView=view.findViewById(R.id.search_recyclerView);
        search_recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        search_value.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(TextUtils.isEmpty(newText)){
                    search_recyclerView.setAdapter(null);
                    //action_view.setVisibility(View.VISIBLE);
                }else{
                    search(newText);
                }
                return false;
            }
        });

        final ChipGroup choiceChipGroup = view.findViewById(R.id.choice_chip_group);
        choiceChipGroup.check(R.id.choice_chip1);
        choiceChipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup chipGroup, @IdRes int i) {
                switch (chipGroup.getCheckedChipId()){
                    case R.id.choice_chip1:
                        bookLoading(array[0]);
                        setBannerBook(array[0]);
                        break;
                    case R.id.choice_chip2:
                        bookLoading(array[1]);
                        setBannerBook(array[1]);
                        break;
                    case R.id.choice_chip3:
                        bookLoading(array[2]);
                        setBannerBook(array[2]);
                        break;
                    case R.id.choice_chip4:
                        bookLoading(array[3]);
                        setBannerBook(array[3]);
                        break;
                    case R.id.choice_chip5:
                        bookLoading(array[4]);
                        setBannerBook(array[4]);
                        break;
                    case R.id.choice_chip6:
                        bookLoading(array[5]);
                        setBannerBook(array[5]);
                        break;
                    case R.id.choice_chip7:
                        bookLoading(array[6]);
                        setBannerBook(array[6]);
                        break;
                    case R.id.choice_chip8:
                        bookLoading(array[7]);
                        setBannerBook(array[7]);
                        break;
                    case R.id.choice_chip9:
                        bookLoading(array[8]);
                        setBannerBook(array[8]);
                        break;
                    case R.id.choice_chip10:
                        bookLoading(array[9]);
                        setBannerBook(array[9]);
                        break;
                    case R.id.choice_chip11:
                        bookLoading(array[10]);
                        setBannerBook(array[10]);
                        break;
                    case R.id.choice_chip12:
                        bookLoading(array[11]);
                        setBannerBook(array[11]);
                        break;
                    case R.id.choice_chip13:
                        bookLoading(array[12]);
                        setBannerBook(array[12]);
                        break;
                    case R.id.choice_chip14:
                        bookLoading(array[13]);
                        setBannerBook(array[13]);
                        break;
                    case R.id.choice_chip15:
                        bookLoading(array[14]);
                        setBannerBook(array[14]);
                        break;
                    case R.id.choice_chip16:
                        bookLoading(array[15]);
                        setBannerBook(array[15]);
                        break;
                    case R.id.choice_chip17:
                        bookLoading(array[16]);
                        setBannerBook(array[16]);
                        break;
                    case R.id.choice_chip18:
                        bookLoading(array[17]);
                        setBannerBook(array[17]);
                        break;
                }
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        setBannerBook("အားလုံး");
        loadingPopular();
        bookLoading("အားလုံး");
        loadingFrame();
    }
    public void loadingFrame(){
        FrameAdapter frameAdapter=new FrameAdapter();
        if(books.size()==0){
            recyclerView_frame.setAdapter(frameAdapter);
            recyclerView_frame.setVisibility(View.VISIBLE);
        }else{
            recyclerView_frame.setVisibility(View.GONE);
        }
        if(popular.size()==0){
            popular_recyclerView_frame.setAdapter(frameAdapter);
            popular_recyclerView_frame.setVisibility(View.VISIBLE);
        }else{
            popular_recyclerView_frame.setVisibility(View.GONE);
        }

    }

    public void loadingPopular(){
        final DatabaseReference reference= FirebaseDatabase.getInstance().getReference();
        reference.child("Popular").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //popular=new ArrayList<>();
                popular.clear();
                if(snapshot.exists()){
                    for(DataSnapshot snap:snapshot.getChildren()){
                        String pID=snap.child("id").getValue().toString();
                        reference.child("Book").child(pID).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if(snapshot.exists()){
                                            Book book=snapshot.getValue(Book.class);
                                            popular.add(book);
                                            BookAdapter bookAdapter=new BookAdapter(popular,getContext());
                                            popular_recyclerView.setAdapter(bookAdapter);
                                            if(popular.size()>0){
                                                popular_recyclerView_frame.setVisibility(View.GONE);
                                            }else{
                                                popular_recyclerView_frame.setVisibility(View.VISIBLE);
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void setBannerBook(final String type){
        final DatabaseReference rootRef;
        rootRef= FirebaseDatabase.getInstance().getReference().child("Book");
        rootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    //bannerBook=new ArrayList<>();
                    bannerBook.clear();
                    for(DataSnapshot snap:snapshot.getChildren()){
                        Book book=snap.getValue(Book.class);
                        if(type.equals("အားလုံး")){
                            if(bannerBook.size()<5){
                                bannerBook.add(book);
                            }
                        }else if(book.getType().equals(type)){
                            bannerBook.add(book);
                        }
                    }
                    bannerBookAdapter=new BannerBookAdapter(getContext(),bannerBook);
                    viewPager.setAdapter(bannerBookAdapter);
                    sliderHandler.postDelayed(sliderRunnable, 3000);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }

    public void bookLoading(final String type){
        final DatabaseReference rootRef;
        rootRef= FirebaseDatabase.getInstance().getReference().child("Book");
        rootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    //books=new ArrayList<>();
                    books.clear();
                    for(DataSnapshot snap:snapshot.getChildren()){
                        Book book=snap.getValue(Book.class);
                        if(type.equals("အားလုံး")){
                            books.add(book);
                        }else if(book.getType().equals(type)){
                            books.add(book);
                        }
                    }
                    BookAdapter bookAdapter=new BookAdapter(books,getContext());
                    recyclerView.setAdapter(bookAdapter);
                    if(books.size()>0){
                        recyclerView_frame.setVisibility(View.GONE);
                    }else{
                        recyclerView_frame.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }

    private Runnable sliderRunnable=new Runnable() {
        @Override
        public void run() {
            if(viewPager.getCurrentItem()==bannerBook.size()-1){
                viewPager.setCurrentItem(0);
            }else{
                viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
            }
        }
    };
    @Override
    public void onStop() {
        super.onStop();
        sliderHandler.removeCallbacks(sliderRunnable);
    }
    public void search(final String searchInput){
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference();
        reference.child("Book").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                search_list=new ArrayList<>();
                search_list.clear();
                if(snapshot.exists()){
                    for(DataSnapshot snap:snapshot.getChildren()){
                        Book book=snap.getValue(Book.class);
                        if(book.getName().toLowerCase().contains(searchInput.toLowerCase())){
                            search_list.add(new Search(book.getId(),book.getName()));
                            SearchAdapter searchAdapter=new SearchAdapter(getContext(),search_list);
                            search_recyclerView.setAdapter(searchAdapter);
                            //action_view.setVisibility(View.GONE);
                        }else{
                           // action_view.setVisibility(View.VISIBLE);
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