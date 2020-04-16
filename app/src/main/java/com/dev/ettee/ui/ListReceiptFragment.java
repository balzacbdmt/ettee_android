package com.dev.ettee.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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

import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;

public class ListReceiptFragment extends Fragment {
    private RecyclerView recyclerView;
    private static RecyclerView.Adapter rvAdapter;
    private RecyclerView.LayoutManager rvLayoutManager;
    private static ArrayList<Receipt> listReceipt;
    private static final int VERTICAL_ITEM_SPACE = 25;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_list_receipt, container, false);

        listReceipt = new ArrayList<>();
        parseJSON(readJsonReceipt());

        recyclerView = root.findViewById(R.id.listreceipt_rc);
        rvLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(rvLayoutManager);
        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(VERTICAL_ITEM_SPACE));
        rvAdapter = new ReceiptAdapter(listReceipt);
        recyclerView.setAdapter(rvAdapter);

        return root;
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

                listReceipt.add(receipt);
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
