package com.cu.bookstore;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.Toast;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {
    public static int REQUEST_PERMISSION_READ=1;
    public static int REQUEST_PERMISSION_WRITE=0;
    private BadgeDrawable btmFavorite,btmDownload;
    private int[] gravityValues={BadgeDrawable.TOP_END};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home,R.id.navigation_search,R.id.navigation_favorite, R.id.navigation_download, R.id.navigation_settings)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        int max=1000;
        int min=0;

        int random1= (int) (Math.random()*(max-min+1)+min);
        int random2= (int) (Math.random()*(max-min+1)+min);
        int random3= (int) (Math.random()*(max-min+1)+min);
        int random4= (int) (Math.random()*(max-min+1)+min);
        int random5= (int) (Math.random()*(max-min+1)+min);

        if(TextUtils.isEmpty(has())){
            save(random1+""+random2+""+random3+""+random4+""+random5);
        }
        permission1();
        permission2();
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                permission1();
                permission2();
            }
        },1000);

    }

    public void save(String id){
        SharedPreferences preferences=getSharedPreferences("ID", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString("ID",id).apply();
    }
    public String has(){
        SharedPreferences preferences=getSharedPreferences("ID", Context.MODE_PRIVATE);
        return preferences.getString("ID","");
    }

    public void permission1() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {

            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION_READ);
            }
        }
    }
    public void permission2() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_READ);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==REQUEST_PERMISSION_READ){
            if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){

                Toast.makeText(this,"Allow the permission",Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this,"Please allow the permission",Toast.LENGTH_SHORT).show();
            }
        }else if(requestCode==REQUEST_PERMISSION_WRITE){
            if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){

                Toast.makeText(this,"Allow the permission",Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this,"Please allow the permission",Toast.LENGTH_SHORT).show();
            }
        }
    }
}