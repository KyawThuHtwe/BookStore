package com.cu.bookstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.Toast;

import com.cu.bookstore.Fragments.DownloadFragment;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageScrollListener;
import com.github.barteksc.pdfviewer.listener.OnRenderListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;

import java.io.File;

public class PdfViewerActivity extends AppCompatActivity implements OnPageChangeListener{

    PDFView pdfView;
    File file;
    int position=-1;
    int mCurrentPage=0;
    int totalPage=0;
    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_viewer);
        pdfView=findViewById(R.id.pdf_view);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        pdfView=findViewById(R.id.pdf_view);
        position=getIntent().getIntExtra("position",-1);

        if(getPdfName(DownloadFragment.fileList.get(position).getName()).equals(DownloadFragment.fileList.get(position).getName())){
            mCurrentPage=getPdfPage(DownloadFragment.fileList.get(position).getName());
        }

        if(!TextUtils.isEmpty(getIntent().getStringExtra("pdf"))){
            file= new File(getIntent().getStringExtra("pdf"));
        }else {
            file=DownloadFragment.fileList.get(position);
        }

        pdfView.fromFile(file)
                .defaultPage(mCurrentPage)
                .enableSwipe(true)
                .swipeHorizontal(false)
                .enableDoubletap(true)
                .password(null)
                .enableAntialiasing(true)
                .onPageChange((OnPageChangeListener) this)
                .enableAnnotationRendering(true)
                .onRender(new OnRenderListener()
                {
                    @Override
                    public void onInitiallyRendered(int nbPages, float pageWidth, float pageHeight)
                    {
                        pdfView.fitToWidth(mCurrentPage);
                    }
                })
                .onLoad(new OnLoadCompleteListener() {
                    @Override
                    public void loadComplete(int nbPages) {
                        totalPage=nbPages;
                        // Toast.makeText(getApplicationContext(),"loadComplete: totalPages " + nbPages,Toast.LENGTH_SHORT).show();
                    }
                })
                .onError(new OnErrorListener() {
                    @Override
                    public void onError(Throwable t) {
                        Toast.makeText(getApplicationContext(),"onError",Toast.LENGTH_SHORT).show();
                    }
                })
                .scrollHandle(new DefaultScrollHandle(this))
                .spacing(2)
                .load();

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPageChanged(int page, int pageCount)
    {
        mCurrentPage = page;
        //setTitle(String.format("%s %s / %s", "Page Number", page + 1, pageCount));
        int count=page+1;
        getSupportActionBar().setTitle(DownloadFragment.fileList.get(position).getName()+"("+count+"/"+totalPage+")");
        TextFile(DownloadFragment.fileList.get(position).getName(),DownloadFragment.fileList.get(position).getName(),mCurrentPage);
    }
    private void TextFile(String Name,String title,int page) {
        SharedPreferences sharedPreferences=getSharedPreferences("PdfFile", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(Name,title);
        editor.putInt(Name+"PdfPage",page);
        editor.apply();
    }
    public String getPdfName(String Name){
        SharedPreferences sharedPreferences=getSharedPreferences("PdfFile", Context.MODE_PRIVATE);
        return sharedPreferences.getString(Name,"");
    }
    public int getPdfPage(String Name){
        SharedPreferences sharedPreferences=getSharedPreferences("PdfFile", Context.MODE_PRIVATE);
        return sharedPreferences.getInt(Name+"PdfPage",0);
    }

}