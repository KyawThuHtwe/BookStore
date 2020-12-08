package com.cu.bookstore.Fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cu.bookstore.Adapters.PDFAdapter;
import com.cu.bookstore.PdfViewerActivity;
import com.cu.bookstore.R;

import java.io.File;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class DownloadFragment extends Fragment {
    ListView listView;
    public static ArrayList<File> fileList=new ArrayList<>();
    PDFAdapter adapter;
    public static int REQUEST_PERMISSION=1;
    boolean boolean_permission;

    TextView hasNot;

    File dir;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View  view= inflater.inflate(R.layout.fragment_download, container, false);
        listView=view.findViewById(R.id.listView);
        hasNot=view.findViewById(R.id.hasNot);
        dir=new File(Environment.getExternalStorageDirectory()+ "/" + "Book Store");

        /*
        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //downloadPdf();
            }
        },1000);

         */
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getContext(), PdfViewerActivity.class);
                intent.putExtra("position",position);
                startActivity(intent);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                confirmDelete(fileList.get(position),view);
                return true;
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            downloadPdf();
        }catch (Exception e){
            Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
    /*
    public void actionAlertDialog(final int position){
        try {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.AppTheme);
            View view = LayoutInflater.from(this).inflate(R.layout.action_layout, (LinearLayout)findViewById(R.id.layout));
            builder.setView(view);
            TextView name=view.findViewById(R.id.name);
            name.setText(MainActivity.fileList.get(position).getName());
            TextView rename=view.findViewById(R.id.rename);
            TextView delete=view.findViewById(R.id.delete);
            final AlertDialog dialog = builder.create();
            dialog.show();
            rename.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    renameFileAlertDialog(MainActivity.fileList.get(position));
                    dialog.dismiss();
                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    confirmDelete(MainActivity.fileList.get(position));
                    dialog.dismiss();
                }
            });
        }catch (Exception e){
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }

    }

     */

    public void confirmDelete(final File file,View v){
        try {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(v.getContext());
            builder.setCancelable(false);
            builder.setTitle(file.getName());
            builder.setMessage("Are you sure you want to this PDF Book Delete?");
            builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    file.delete();
                    onStart();
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            builder.create().show();
        } catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }
    /*
    public void renameFileAlertDialog(final File file){
        try {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.AlertDialogTheme);
            final View view = LayoutInflater.from(this).inflate(R.layout.rename_layout, (LinearLayout)findViewById(R.id.layout));
            builder.setView(view);
            final EditText editText=view.findViewById(R.id.edit);
            editText.setText(file.getName());
            final TextView ok=view.findViewById(R.id.ok);
            TextView cancel=view.findViewById(R.id.cancel);
            final AlertDialog dialog = builder.create();
            dialog.show();
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if(editText.getText().toString().endsWith(".pdf")) {
                            //dir = new File(Environment.getExternalStorageDirectory().toString());
                            File old = new File(file.getPath() + "");
                            boolean res = old.renameTo(new File( file.getParent(), editText.getText() + ""));
                            if (res) {
                                dialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Rename : "+editText.getText() + "", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Not changed : " + file.getName(), Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            editText.setText(editText.getText()+".pdf");
                        }
                    }catch (Exception e){
                        Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                    permission_fn();

                }
            });
        }catch (Exception e){
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

     */
    private void downloadPdf() {
        boolean_permission=true;
        if(fileList.size()>0){
            fileList.clear();
        }
        getFile(dir);

        if(fileList.size()>0){
            adapter=new PDFAdapter(getContext(),fileList);
            listView.setAdapter(adapter);
            listView.setTextFilterEnabled(true);
            hasNot.setVisibility(View.GONE);
        }else{
            listView.setAdapter(null);
            hasNot.setVisibility(View.VISIBLE);
        }
    }

    public ArrayList<File> getFile(File dir){
        File listFile[]=dir.listFiles();
        if(listFile!=null && listFile.length>0){
            for(int i=0;i<listFile.length;i++){
                if(listFile[i].isDirectory()){
                    getFile(listFile[i]);
                }else {
                    boolean booleanpdf=false;
                    if(listFile[i].getName().endsWith(".pdf")){
                        for(int j=0;j<fileList.size();j++){
                            if(fileList.get(j).getName().equals(listFile[i].getName())){
                                booleanpdf=true;

                            }else {

                            }
                        }
                        if(booleanpdf){
                            booleanpdf=false;
                        }else {
                            fileList.add(listFile[i]);
                        }
                    }
                }
            }
        }
        return fileList;
    }
}