package com.facebook.myapp.ui.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.facebook.myapp.R;
import com.facebook.myapp.module.Image;
import com.facebook.myapp.ui.adapter.NinegridAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<List<Image>>  listData=new ArrayList<List<Image>>();
    private ListView  listView;
    private NinegridAdapter  adapter;
    private String[][] images=new String[][]{{
            "http://img4.duitang.com/uploads/item/201209/25/20120925201555_eUHEU.jpeg","640","960"}
            ,{"file:///android_asset/img2.jpg","250","250"}
            ,{"file:///android_asset/img3.jpg","250","250"}
            ,{"file:///android_asset/img4.jpg","250","250"}
            ,{"file:///android_asset/img5.jpg","250","250"}
            ,{"file:///android_asset/img6.jpg","250","250"}
            ,{"file:///android_asset/img7.jpg","250","250"}
            ,{"file:///android_asset/img8.jpg","250","250"}
            ,{"http://img3.douban.com/view/photo/raw/public/p1708880537.jpg","1280","800"}
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        initView();
    }

    private void initView() {
        listView=(ListView)findViewById(R.id.listview);
        List<Image>  simpleData=new ArrayList<Image>();
//        simpleData.add(new Image(images[0][0],(Integer.parseInt(images[0][1])),Integer.parseInt(images[0][2])));
//        listData.add(simpleData);
        for(int i=0;i<9;i++){
            List<Image>  itemlist=new ArrayList<Image>();
            for(int j=0;j<=i;j++) {
                itemlist.add(new Image(images[j][0], Integer.parseInt(images[j][1]), Integer.parseInt(images[j][2])));
            }
            listData.add(itemlist);
        }
        adapter=new NinegridAdapter(this,listData);
        listView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
