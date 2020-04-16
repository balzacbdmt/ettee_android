package com.dev.ettee.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.dev.ettee.Address;
import com.dev.ettee.Category;
import com.dev.ettee.R;
import com.dev.ettee.Receipt;
import com.dev.ettee.Shop;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddReceiptFragment extends Fragment implements View.OnClickListener {
    List<String> listCat;
    List<String> listPaymentMethod;
    List<String> listShop;

    EditText etName;
    DatePicker etDate;
    EditText etAmount;
    EditText etCommentary;

    Spinner spinnerPM;
    Spinner spinnerCat;
    Spinner spinnerShop;

    LinearLayout addImageLayout;

    Integer REQUEST_CAMERA = 0, SELECT_FILE = 1;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_add_receipt, container, false);

        spinnerPM = (Spinner) root.findViewById(R.id.addReceipt_paymentmethod_spinner);
        listPaymentMethod = new ArrayList<String>();
        listPaymentMethod.add("Credit card");
        listPaymentMethod.add("Cash");
        listPaymentMethod.add("Cheque");
        listPaymentMethod.add("Bank transfer");
        listPaymentMethod.add("Lydia");
        ArrayAdapter<String> dataAdapterPM = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, listPaymentMethod);
        dataAdapterPM.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPM.setAdapter(dataAdapterPM);

        spinnerCat = (Spinner) root.findViewById(R.id.addReceipt_category_spinner);
        listCat = new ArrayList<String>();
        fillListCat();
        ArrayAdapter<String> dataAdapterCat = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, listCat);
        dataAdapterCat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCat.setAdapter(dataAdapterCat);

        spinnerShop = (Spinner) root.findViewById(R.id.addReceipt_shop_spinner);
        listShop = new ArrayList<String>();
        fillListShops();
        ArrayAdapter<String> dataAdapterShop = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, listShop);
        dataAdapterShop.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerShop.setAdapter(dataAdapterShop);

        etName = (EditText) root.findViewById(R.id.addReceipt_name_et);
        etDate = (DatePicker) root.findViewById(R.id.addReceipt_date_dp);
        etAmount  = (EditText) root.findViewById(R.id.addReceipt_amount_et);
        etCommentary = (EditText) root.findViewById(R.id.addReceipt_commentary_et);

        Button button_submit = (Button)root.findViewById(R.id.addReceipt_btn_add);
        ImageView button_add_shop = (ImageView) root.findViewById(R.id.addReceipt_image_add_Shop);
        addImageLayout = root.findViewById(R.id.addReceipt_add_image_layout);
        button_submit.setOnClickListener(this);
        button_add_shop.setOnClickListener(this);
        addImageLayout.setOnClickListener(this);

        return root;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addReceipt_btn_add:
                if (etAmount.getText().toString().trim().length() == 0 ) {
                    etAmount.setText("0");
                }
                if (etName.getText().toString().trim().length() == 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle(R.string.dialog_empty_field_title);
                    builder.setMessage(R.string.dialog_empty_field_message);
                    builder.setPositiveButton(R.string.dialog_empty_field_ok, null);
                    builder.show();
                } else {
                    String name = etName.getText().toString();
                    Date date = getDateFromDatePicker(etDate);
                    Double amount = 0.0+Double.parseDouble(etAmount.getText().toString());
                    String paymentMethod = spinnerPM.getSelectedItem().toString();
                    String commentary = etCommentary.getText().toString() + "";

                    String catName = spinnerCat.getSelectedItem().toString();
                    Category category = null;
                    try {
                        JSONObject jsonCategory = new JSONObject(readJson("category.json"));
                        JSONArray jsonArrayCategory = jsonCategory.getJSONArray("category");
                        for (int j = 0; j < jsonArrayCategory.length(); j++) {
                            JSONObject jsonObjCategory = jsonArrayCategory.getJSONObject(j);
                            Integer idCat = jsonObjCategory.getInt("id");
                            String nameCat = jsonObjCategory.getString("name");
                            String colorCat = jsonObjCategory.getString("color");
                            if (nameCat.equals(catName)) {
                                category = new Category(idCat, nameCat);
                                category.setColor(colorCat);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    String shopName = spinnerShop.getSelectedItem().toString();
                    Shop shop = null;
                    try {
                        JSONObject jsonCategory = new JSONObject(readJson("shops.json"));
                        JSONArray jsonArrayCategory = jsonCategory.getJSONArray("shops");
                        for (int j = 0; j < jsonArrayCategory.length(); j++) {
                            JSONObject jsonObjCategory = jsonArrayCategory.getJSONObject(j);
                            Integer idShop = jsonObjCategory.getInt("id");
                            String nameShop = jsonObjCategory.getString("name");
                            Integer idAddressShop = jsonObjCategory.getInt("idAddress");
                            if (nameShop.equals(shopName)) {
                                shop = new Shop(nameShop);
                                shop.setId(idShop);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Receipt receiptToAdd = new Receipt(name, date, shop, amount, commentary);
                    receiptToAdd.setPaymentMethod(paymentMethod);
                    receiptToAdd.setCategory(category);

                    try {
                        addReceipt(receiptToAdd);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    etName.setText("");
                    etAmount.setText("");
                    etCommentary.setText("");
                }
                break;
            case R.id.addReceipt_image_add_Shop:
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                LayoutInflater inflater = requireActivity().getLayoutInflater();
                View dialogViewShop = inflater.inflate(R.layout.dialog_new_shop, null);
                builder.setView(dialogViewShop);
                final EditText dialogEditTextShopName = dialogViewShop.findViewById(R.id.dialog_new_shop_name_et);
                final EditText dialogEditTextShopNumber = dialogViewShop.findViewById(R.id.dialog_address_number_et_new_shop);
                final EditText dialogEditTextShopStreet = dialogViewShop.findViewById(R.id.dialog_address_street_et_new_shop);
                final EditText dialogEditTextShopCity = dialogViewShop.findViewById(R.id.dialog_address_city_et_new_shop);
                final EditText dialogEditTextShopCountry = dialogViewShop.findViewById(R.id.dialog_address_country_et_new_shop);
                final EditText dialogEditTextShopZipcode = dialogViewShop.findViewById(R.id.dialog_address_zipcode_et_new_shop);
                builder.setPositiveButton(R.string.dialog_new_shop_add, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        //TODO if an field is empty return;
                        Shop newShop = new Shop(dialogEditTextShopName.getText().toString());
                        Address newShopAddress = new Address(Integer.parseInt(dialogEditTextShopNumber.getText().toString()),dialogEditTextShopStreet.getText().toString(),dialogEditTextShopCity.getText().toString(),dialogEditTextShopCountry.getText().toString(),dialogEditTextShopZipcode.getText().toString());
                        newShop.setAddress(newShopAddress);
                        try {
                            addNewShop(newShop);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                builder.setNegativeButton(R.string.cancel, null);
                builder.show();
                break;
            case R.id.addReceipt_add_image_layout:
                final CharSequence[] items={getResources().getString(R.string.camera), getResources().getString(R.string.gallery), getResources().getString(R.string.cancel)};

                AlertDialog.Builder builderAddImg = new AlertDialog.Builder(getContext());
                builderAddImg.setTitle(getResources().getString(R.string.new_image));
                builderAddImg.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (items[which].equals(getResources().getString(R.string.camera))){
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, REQUEST_CAMERA);
                        }
                        else if (items[which].equals(getResources().getString(R.string.gallery))){
                            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            intent.setType("image/*");
                            startActivityForResult(intent.createChooser(intent, getResources().getString(R.string.select_file)), SELECT_FILE);
                        }
                        else if (items[which].equals(getResources().getString(R.string.cancel))){
                            dialog.dismiss();
                        }
                    }
                });
                builderAddImg.show();
                break;
        }
    }

    private void addNewShop(Shop newShop) throws JSONException {
        String jsonFileAddress = readJson("address.json");
        JSONObject jsAddress = new JSONObject(jsonFileAddress);
        JSONArray contentJsonAddress = new JSONObject(jsonFileAddress).getJSONArray("address");

        JSONObject jsonObjAddressLast = contentJsonAddress.getJSONObject(contentJsonAddress.length()-1);
        Integer idAddressLast = jsonObjAddressLast.getInt("id");

        JSONObject newAddress = new JSONObject();
        newAddress.put("id", idAddressLast + 1);
        newShop.getAddress().setId(contentJsonAddress.length() + 1);
        newAddress.put("number", newShop.getAddress().getNumber());
        newAddress.put("street", newShop.getAddress().getStreet());
        newAddress.put("city", newShop.getAddress().getCity());
        newAddress.put("country", newShop.getAddress().getCountry());
        newAddress.put("zipcode", newShop.getAddress().getZipCode());

        contentJsonAddress.put(newAddress);
        jsAddress.put("address", contentJsonAddress);
        String finalContent = jsAddress.toString();
        try {
            OutputStream outputStream = getActivity().openFileOutput("address.json", Context.MODE_PRIVATE);
            outputStream.write(finalContent.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String jsonFileShop = readJson("shops.json");
        JSONObject jsShop = new JSONObject(jsonFileShop);
        JSONArray contentJsonShop = new JSONObject(jsonFileShop).getJSONArray("shops");

        JSONObject jsonObjShopLast = contentJsonShop.getJSONObject(contentJsonShop.length()-1);
        Integer idShopLast = jsonObjShopLast.getInt("id");

        JSONObject newShopJson = new JSONObject();
        newShopJson.put("id", idShopLast + 1);
        newShopJson.put("name", newShop.getName());
        newShopJson.put("idAddress", newShop.getAddress().getId());

        contentJsonShop.put(newShopJson);
        jsShop.put("shops", contentJsonShop);
        String finalContent2 = jsShop.toString();
        try {
            OutputStream outputStream = getActivity().openFileOutput("shops.json", Context.MODE_PRIVATE);
            outputStream.write(finalContent2.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //TODO refresh shop lists
    }

    public static java.util.Date getDateFromDatePicker(DatePicker datePicker) {
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        return calendar.getTime();
    }

    private void addReceipt(Receipt receiptToAdd) throws JSONException {
        String content = readJson("receipts.json");
        JSONObject js = new JSONObject(content);
        JSONArray contentJson = new JSONObject(content).getJSONArray("receipts");

        JSONObject jsonObjCategoryLast = contentJson.getJSONObject(contentJson.length()-1);
        Integer idShopLast = jsonObjCategoryLast.getInt("id");

        JSONObject newReceipt = new JSONObject();
        newReceipt.put("id", idShopLast+1);
        newReceipt.put("purchaseDate", receiptToAdd.getPurcharseDate());
        newReceipt.put("amount", receiptToAdd.getAmount());
        newReceipt.put("paymentMethod", receiptToAdd.getPaymentMethod());
        newReceipt.put("name", receiptToAdd.getName());
        newReceipt.put("commentary", receiptToAdd.getCommentary());
        newReceipt.put("pinned", false);
        newReceipt.put("userId", 1);
        newReceipt.put("shopId", receiptToAdd.getShop().getId());
        newReceipt.put("categoryId", receiptToAdd.getCategory().getId());

        contentJson.put(newReceipt);
        js.put("receipts", contentJson);
        String finalContent = js.toString();
        try {
            OutputStream outputStream = getActivity().openFileOutput("receipts.json", Context.MODE_PRIVATE);
            outputStream.write(finalContent.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //TODO add in DB
    }

    public String readJson(String filename) {
        String content = null;
        try {
            InputStream is = getActivity().openFileInput(filename);
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

    private void fillListCat() {
        try {
            JSONObject jsonCategory = new JSONObject(readJson("category.json"));
            JSONArray jsonArrayCategory = jsonCategory.getJSONArray("category");
            Category category = null;
            for (int j = 0; j < jsonArrayCategory.length(); j++) {
                JSONObject jsonObjCategory = jsonArrayCategory.getJSONObject(j);
                String nameCat = jsonObjCategory.getString("name");
                listCat.add(nameCat);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void fillListShops() {
        try {
            JSONObject jsonCategory = new JSONObject(readJson("shops.json"));
            JSONArray jsonArrayCategory = jsonCategory.getJSONArray("shops");
            Shop shop = null;
            for (int j = 0; j < jsonArrayCategory.length(); j++) {
                JSONObject jsonObjCategory = jsonArrayCategory.getJSONObject(j);
                String nameShop = jsonObjCategory.getString("name");
                listShop.add(nameShop);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
