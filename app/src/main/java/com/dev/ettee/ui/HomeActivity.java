package com.dev.ettee.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toolbar;

import com.dev.ettee.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavArgument;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.NavInflater;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    NavController nc;
    File file;
    String fileNameReceipt = "receipts.json";
    String fileNameCategory = "category.json";
    String fileNameShop = "shops.json";
    String fileNameAddress = "address.json";
    FileOutputStream outputStream;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Adding json to data file app
        createJsonReceipt();
        createJsonCategory();
        createJsonShop();
        createJsonAddress();

        BottomNavigationView navView = findViewById(R.id.nav_view);

        nc = Navigation.findNavController(this, R.id.nav_host_fragment);

        /*NavInflater navInflater = nc.getNavInflater();
        NavGraph graph = navInflater.inflate(R.navigation.mobile_navigation);

        Bundle bundle = new Bundle();
        bundle.putString("receipts", readFile());
        nc.setGraph(graph, bundle);*/

        NavigationUI.setupWithNavController(navView, nc);
        navView.setOnNavigationItemSelectedListener(this);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int fragmentId = item.getItemId();

        /*NavInflater navInflater = nc.getNavInflater();
        NavGraph graph = navInflater.inflate(R.navigation.mobile_navigation);

        Bundle bundle = new Bundle();
        bundle.putString("receipts", "5");

        nc.setGraph(graph, bundle)
        nc.navigate(fragmentId, bundle);*/

        nc.navigate(fragmentId);
        return false;
    }

    public void createJsonReceipt(){
        file = new File("data/data/"+getPackageName()+"/"+fileNameReceipt);
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            setContentJsonReceipt();
        }
    }

    public void createJsonCategory(){
        file = new File("data/data/"+getPackageName()+"/"+fileNameCategory);
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            setContentJsonCategory();
        }
    }

    public void createJsonShop(){
        file = new File("data/data/"+getPackageName()+"/"+fileNameShop);
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            setContentJsonShop();
        }
    }

    public void createJsonAddress(){
        file = new File("data/data/"+getPackageName()+"/"+fileNameAddress);
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            setContentJsonAddress();
        }
    }

    public void setContentJsonReceipt(){
        InputStream is = getResources().openRawResource(R.raw.receipts);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String contentJson = writer.toString();
        try {
            outputStream = openFileOutput(fileNameReceipt, Context.MODE_PRIVATE);
            outputStream.write(contentJson.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setContentJsonShop(){
        InputStream is = getResources().openRawResource(R.raw.shops);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String contentJson = writer.toString();
        try {
            outputStream = openFileOutput(fileNameShop, Context.MODE_PRIVATE);
            outputStream.write(contentJson.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setContentJsonCategory(){
        InputStream is = getResources().openRawResource(R.raw.category);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String contentJson = writer.toString();
        try {
            outputStream = openFileOutput(fileNameCategory, Context.MODE_PRIVATE);
            outputStream.write(contentJson.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setContentJsonAddress(){
        InputStream is = getResources().openRawResource(R.raw.address);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String contentJson = writer.toString();
        try {
            outputStream = openFileOutput(fileNameAddress, Context.MODE_PRIVATE);
            outputStream.write(contentJson.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
