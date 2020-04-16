package com.dev.ettee.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dev.ettee.Category;
import com.dev.ettee.R;
import com.dev.ettee.Receipt;
import com.dev.ettee.ReceiptAdapter;
import com.dev.ettee.Shop;
import com.dev.ettee.VerticalSpaceItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;

public class HomeFragment extends Fragment implements AdapterView.OnItemClickListener {
    TextView titlePinned;
    TextView titleRecent;

    private RecyclerView recyclerViewRecent;
    private RecyclerView recyclerViewPinned;

    private static RecyclerView.Adapter rvAdapterRecent;
    private static RecyclerView.Adapter rvAdapterPinned;

    private RecyclerView.LayoutManager rvLayoutManager;

    private static ArrayList<Receipt> listRecentReceipt;
    private static ArrayList<Receipt> listPinnedReceipt;

    private static final int VERTICAL_ITEM_SPACE = 25;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        titlePinned = root.findViewById(R.id.home_pinnedReceipt);
        titleRecent = root.findViewById(R.id.home_recentReceipt);
        titlePinned.setVisibility(View.VISIBLE);

        listRecentReceipt = new ArrayList<>();
        listPinnedReceipt = new ArrayList<>();
        parseJSON(readJsonReceipt());

        recyclerViewRecent = root.findViewById(R.id.home_rcRecentReceipt);
        rvLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewRecent.setLayoutManager(rvLayoutManager);
        recyclerViewRecent.addItemDecoration(new VerticalSpaceItemDecoration(VERTICAL_ITEM_SPACE));
        rvAdapterRecent = new ReceiptAdapter(listRecentReceipt);
        recyclerViewRecent.setAdapter(rvAdapterRecent);

        recyclerViewPinned = root.findViewById(R.id.home_rcPinnedReceipt);
        rvLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewPinned.setLayoutManager(rvLayoutManager);
        recyclerViewPinned.addItemDecoration(new VerticalSpaceItemDecoration(VERTICAL_ITEM_SPACE));
        rvAdapterPinned = new ReceiptAdapter(listPinnedReceipt);
        recyclerViewPinned.setAdapter(rvAdapterPinned);

        return root;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), AccountFragment.class);
        startActivity(intent);
    }

    public String readJsonReceipt(){
        String content = null;
        try {
            InputStream is = getActivity().openFileInput("receipts.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            content = new String(buffer);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return content;
    }

    private void parseJSON(String leJson){
        try {
            JSONObject jsonReceipts = new JSONObject(leJson);
            JSONArray jsonArray = jsonReceipts.getJSONArray("receipts");
            Receipt receipt = null;
            for (int i = jsonArray.length(); i > 0; i--) {

                JSONObject jsonObj = jsonArray.getJSONObject(i-1);

                Integer id = jsonObj.getInt("id");
                Date purchaseDate = new Date(jsonObj.getString("purchaseDate"));
                Double amount = jsonObj.getDouble("amount");
                String paymentMethod = jsonObj.getString("paymentMethod");
                String name = jsonObj.getString("name");
                String commentary = jsonObj.getString("commentary");
                Boolean pinned = jsonObj.getBoolean("pinned");
                Integer userId = jsonObj.getInt("userId");
                Integer shopId = jsonObj.getInt("shopId");
                Integer categoryId = jsonObj.getInt("categoryId");

                JSONObject jsonCategory = new JSONObject(readJsonCategory());
                JSONArray jsonArrayCategory = jsonCategory.getJSONArray("category");
                Category category = null;
                for (int j = 0; j < jsonArrayCategory.length(); j++) {
                    JSONObject jsonObjCategory = jsonArrayCategory.getJSONObject(j);
                    Integer idCat = jsonObjCategory.getInt("id");
                    if (categoryId == idCat) {
                        String nameCat = jsonObjCategory.getString("name");
                        String colorCat = jsonObjCategory.getString("color");
                        category = new Category(idCat, nameCat);
                        category.setColor(colorCat);
                    }
                }

                JSONObject jsonShop = new JSONObject(readJsonShop());
                JSONArray jsonArrayShop = jsonShop.getJSONArray("shops");
                Shop shop = null;
                for (int j = 0; j < jsonArrayShop.length(); j++) {
                    JSONObject jsonObjShop= jsonArrayShop.getJSONObject(j);
                    Integer idShop = jsonObjShop.getInt("id");
                    if (shopId == idShop) {
                        String nameShop = jsonObjShop.getString("name");
                        Integer addressIdShop = jsonObjShop.getInt("idAddress");
                        shop = new Shop(nameShop);
                        shop.setId(idShop);
                        //TODO for to setAddress from addressId
                    }
                }

                receipt = new Receipt(name, purchaseDate, shop, amount, commentary);
                receipt.setId(id);
                receipt.setPaymentMethod(paymentMethod);
                receipt.setPinned(pinned);
                receipt.setCategory(category);

                if (i > jsonArray.length()-5) {
                    listRecentReceipt.add(receipt);
                }
                if (pinned) {
                    listPinnedReceipt.add(receipt);
                }
            }
            if (listPinnedReceipt.size() == 0) {
                titlePinned.setVisibility(View.INVISIBLE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String readJsonCategory(){
        String content = null;
        try {
            InputStream is = getActivity().openFileInput("category.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            content = new String(buffer);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return content;
    }

    public String readJsonShop(){
        String content = null;
        try {
            InputStream is = getActivity().openFileInput("shops.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            content = new String(buffer);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return content;
    }
}
