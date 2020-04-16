package com.dev.ettee.ui;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

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
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DetailReceiptActivity extends AppCompatActivity implements View.OnClickListener {
    TextView TVname;
    TextView TVdate;
    TextView TVshop;
    TextView TVamount;
    TextView TVpaymentMethod;
    TextView TVcategory;
    TextView TVcommentary;
    ImageView IVdelete;
    ImageView IVshare;
    ImageView IVpinned;

    Address addressShop = null;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_receipt);

        TVname = findViewById(R.id.detailreceipt_name_value);
        TVname.setOnClickListener(this);
        TVdate = findViewById(R.id.detailreceipt_date_value);
        TVdate.setOnClickListener(this);
        TVshop = findViewById(R.id.detailreceipt_shop_value);
        TVshop.setOnClickListener(this);
        TVamount = findViewById(R.id.detailreceipt_amount_value);
        TVamount.setOnClickListener(this);
        TVpaymentMethod = findViewById(R.id.detailreceipt_paymentmethod_value);
        TVpaymentMethod.setOnClickListener(this);
        TVcategory = findViewById(R.id.detailreceipt_category_value);
        TVcategory.setOnClickListener(this);
        TVcommentary = findViewById(R.id.detailreceipt_commentary_value);
        TVcommentary.setOnClickListener(this);
        IVdelete = findViewById(R.id.detailreceipt_image_delete);
        IVdelete.setOnClickListener(this);
        IVshare = findViewById(R.id.detailreceipt_image_share);
        IVshare.setOnClickListener(this);
        IVpinned = findViewById(R.id.detailreceipt_image_pinned);
        IVpinned.setOnClickListener(this);

        completeField();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();

        View dialogViewField = inflater.inflate(R.layout.dialog_edit_field, null);
        TextView dialogTitleField = dialogViewField.findViewById(R.id.dialog_edit_field_title);
        final EditText dialogEditTextField = dialogViewField.findViewById(R.id.dialog_edit_field_et);

        View dialogViewSpinner = inflater.inflate(R.layout.dialog_edit_spinner, null);
        TextView dialogTitleSpinner = dialogViewSpinner.findViewById(R.id.dialog_edit_spinner_title);
        final Spinner dialogSpinnerSpinner = dialogViewSpinner.findViewById(R.id.dialog_edit_spinner_spinner);

        View dialogViewDate = inflater.inflate(R.layout.dialog_edit_date, null);
        TextView dialogTitleDate = dialogViewDate.findViewById(R.id.dialog_edit_date_title);
        final DatePicker dialogDatepickerDate = dialogViewDate.findViewById(R.id.dialog_edit_date_dp);

        builder.setNegativeButton(R.string.cancel, null);
        switch (v.getId()) {
            case R.id.detailreceipt_name_value:
                builder.setView(dialogViewField);
                dialogTitleField.setText(getResources().getString(R.string.detailreceipt_name));
                dialogEditTextField.setText(TVname.getText());
                builder.setPositiveButton(R.string.update, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            updateField("name", dialogEditTextField.getText().toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                builder.show();
                break;
            case R.id.detailreceipt_commentary_value:
                builder.setView(dialogViewField);
                dialogTitleField.setText(getResources().getString(R.string.detailreceipt_commentary));
                dialogEditTextField.setText(TVcommentary.getText());
                builder.setPositiveButton(R.string.update, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            updateField("commentary", dialogEditTextField.getText().toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                builder.show();
                break;
            case R.id.detailreceipt_amount_value:
                builder.setView(dialogViewField);
                dialogTitleField.setText(getResources().getString(R.string.detailreceipt_amount));
                dialogEditTextField.setText(TVamount.getText().toString().substring(0, TVamount.getText().toString().length() - 1));
                dialogEditTextField.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
                builder.setPositiveButton(R.string.update, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            if (dialogEditTextField.getText().toString().trim().length() == 0 ) {
                                dialogEditTextField.setText("0");
                            }
                            updateAmount("amount", Double.parseDouble(dialogEditTextField.getText().toString()));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                builder.show();
                break;
            case R.id.detailreceipt_shop_value:
                List<String> listShop = new ArrayList<String>();
                builder.setView(dialogViewSpinner);
                dialogTitleSpinner.setText(getResources().getString(R.string.detailreceipt_shop));
                try {
                    JSONObject jsonShop = new JSONObject(readJson("shops.json"));
                    JSONArray jsonArrayShop = jsonShop.getJSONArray("shops");
                    for (int j = 0; j < jsonArrayShop.length(); j++) {
                        JSONObject jsonObjShop = jsonArrayShop.getJSONObject(j);
                        String nameShop = jsonObjShop.getString("name");
                        listShop.add(nameShop);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ArrayAdapter<String> SpinnerAdapterShop = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listShop);
                SpinnerAdapterShop.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                dialogSpinnerSpinner.setAdapter(SpinnerAdapterShop);
                builder.setPositiveButton(R.string.update, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    try {
                        String selectIdShop = null;
                        try {
                            JSONObject jsonShop = new JSONObject(readJson("shops.json"));
                            JSONArray jsonArrayShop = jsonShop.getJSONArray("shops");
                            for (int j = 0; j < jsonArrayShop.length(); j++) {
                                JSONObject jsonObjShop = jsonArrayShop.getJSONObject(j);
                                Integer idShop = jsonObjShop.getInt("id");
                                String nameShop = jsonObjShop.getString("name");
                                if (dialogSpinnerSpinner.getSelectedItem().toString().equals(nameShop)) {
                                    selectIdShop = idShop.toString();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        updateField("shopId", selectIdShop);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    }
                });
                builder.show();
                break;
            case R.id.detailreceipt_category_value:
                List<String> listCat= new ArrayList<String>();
                builder.setView(dialogViewSpinner);
                dialogTitleSpinner.setText(getResources().getString(R.string.detailreceipt_category));
                try {
                    JSONObject jsonCategory = new JSONObject(readJson("category.json"));
                    JSONArray jsonArrayCategory = jsonCategory.getJSONArray("category");
                    for (int j = 0; j < jsonArrayCategory.length(); j++) {
                        JSONObject jsonObjCategory = jsonArrayCategory.getJSONObject(j);
                        String nameCat = jsonObjCategory.getString("name");
                        listCat.add(nameCat);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ArrayAdapter<String> SpinnerAdapterCat = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listCat);
                SpinnerAdapterCat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                dialogSpinnerSpinner.setAdapter(SpinnerAdapterCat);
                builder.setPositiveButton(R.string.update, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    try {
                        String selectedIdCat = null;
                        try {
                            JSONObject jsonCategory = new JSONObject(readJson("category.json"));
                            JSONArray jsonArrayCategory = jsonCategory.getJSONArray("category");
                            for (int j = 0; j < jsonArrayCategory.length(); j++) {
                                JSONObject jsonObjCategory = jsonArrayCategory.getJSONObject(j);
                                Integer idCat = jsonObjCategory.getInt("id");
                                String nameCat = jsonObjCategory.getString("name");
                                if (dialogSpinnerSpinner.getSelectedItem().toString().equals(nameCat)) {
                                    selectedIdCat = idCat.toString();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        updateField("categoryId", selectedIdCat);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    }
                });
                builder.show();
                break;
            case R.id.detailreceipt_paymentmethod_value:
                List<String> listPM = new ArrayList<String>();
                builder.setView(dialogViewSpinner);
                dialogTitleSpinner.setText(getResources().getString(R.string.detailreceipt_paymentmethod));
                listPM.add("Credit card");
                listPM.add("Cash");
                listPM.add("Cheque");
                listPM.add("Bank transfer");
                listPM.add("Lydia");
                ArrayAdapter<String> SpinnerAdapterPM = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listPM);
                SpinnerAdapterPM.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                dialogSpinnerSpinner.setAdapter(SpinnerAdapterPM);
                builder.setPositiveButton(R.string.update, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    try {
                        updateField("paymentMethod", dialogSpinnerSpinner.getSelectedItem().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    }
                });
                builder.show();
                break;
            case R.id.detailreceipt_date_value:
                builder.setView(dialogViewDate);
                dialogTitleDate.setText(getResources().getString(R.string.detailreceipt_date));
                Date dateReceiptCurrent = null;
                try {
                    JSONObject jsonReceipts = new JSONObject(readJson("receipts.json"));
                    JSONArray jsonArray = jsonReceipts.getJSONArray("receipts");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObj = jsonArray.getJSONObject(i);
                        Integer id = jsonObj.getInt("id");
                        if (id == getIntent().getExtras().getInt("receiptId")) {
                            dateReceiptCurrent = new Date(jsonObj.getString("purchaseDate"));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Calendar cal = Calendar.getInstance();
                cal.setTime(dateReceiptCurrent);
                dialogDatepickerDate.updateDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
                builder.setPositiveButton(R.string.update, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    try {
                        updateDate("purchaseDate", dialogDatepickerDate);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    }
                });
                builder.show();
                break;
            case R.id.detailreceipt_image_pinned:
                try {
                    JSONObject jsonReceipts = new JSONObject(readJson("receipts.json"));
                    JSONArray jsonArray = jsonReceipts.getJSONArray("receipts");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonReceipt = jsonArray.getJSONObject(i);
                        Integer id = jsonReceipt.getInt("id");
                        Boolean pinned = jsonReceipt.getBoolean("pinned");
                        if (id == getIntent().getExtras().getInt("receiptId")) {
                            if (pinned) {
                                updateField("pinned", "false");
                            } else {
                                updateField("pinned", "true");
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.detailreceipt_image_delete:
                builder.setTitle(getResources().getString(R.string.delete_answer));
                builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    removeReceipt();
                    DetailReceiptActivity.super.onBackPressed();
                    }
                });
                builder.show();
                break;
        }
        //TODO share button
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void completeField(){
        try {
            JSONObject jsonReceipts = new JSONObject(readJson("receipts.json"));
            JSONArray jsonArray = jsonReceipts.getJSONArray("receipts");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObj = jsonArray.getJSONObject(i);
                Integer id = jsonObj.getInt("id");
                if (id == getIntent().getExtras().getInt("receiptId")) {
                    Date purchaseDate = new Date(jsonObj.getString("purchaseDate"));
                    Double amount = jsonObj.getDouble("amount");
                    String paymentMethod = jsonObj.getString("paymentMethod");
                    String name = jsonObj.getString("name");
                    String commentary = jsonObj.getString("commentary");
                    Boolean pinned = jsonObj.getBoolean("pinned");
                    Integer userId = jsonObj.getInt("userId");
                    Integer shopId = jsonObj.getInt("shopId");
                    Integer categoryId = jsonObj.getInt("categoryId");

                    if (pinned) {
                        IVpinned.setImageResource(R.drawable.ic_attach_on_24dp);
                    }

                    JSONObject jsonCategory = new JSONObject(readJson("category.json"));
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

                    JSONObject jsonShop = new JSONObject(readJson("shops.json"));
                    JSONArray jsonArrayShop = jsonShop.getJSONArray("shops");
                    Shop shop = null;
                    Integer shopAddressId = null;
                    for (int j = 0; j < jsonArrayShop.length(); j++) {
                        JSONObject jsonObjShop= jsonArrayShop.getJSONObject(j);
                        Integer idShop = jsonObjShop.getInt("id");
                        if (shopId == idShop) {
                            String nameShop = jsonObjShop.getString("name");
                            Integer addressIdShop = jsonObjShop.getInt("idAddress");
                            shop = new Shop(nameShop);
                            shop.setId(idShop);
                            shopAddressId = addressIdShop;
                        }
                    }

                    JSONObject jsonAddress = new JSONObject(readJson("address.json"));
                    JSONArray jsonArrayAddress= jsonAddress.getJSONArray("address");
                    for (int j = 0; j < jsonArrayAddress.length(); j++) {
                        JSONObject jsonObjAddress = jsonArrayAddress.getJSONObject(j);
                        Integer idAddress = jsonObjAddress.getInt("id");
                        if (shopAddressId == idAddress) {
                            Integer numberAddress = jsonObjAddress.getInt("number");
                            String streetAddress = jsonObjAddress.getString("street");
                            String cityAddress = jsonObjAddress.getString("city");
                            String countryAddress = jsonObjAddress.getString("country");
                            String zipcodeAddress = jsonObjAddress.getString("zipcode");
                            addressShop = new Address(numberAddress, streetAddress, cityAddress, countryAddress, zipcodeAddress);
                        }
                    }

                    DateFormat format = DateFormat.getDateInstance(DateFormat.FULL, Locale.getDefault());

                    TVname.setText(name);
                    TVdate.setText(format.format(purchaseDate));
                    TVshop.setText(shop.getName()+" ("+ addressShop.getStreet()+", "+ addressShop.getCity()+")");
                    TVamount.setText(amount.toString()+"€");
                    TVpaymentMethod.setText(paymentMethod);
                    TVcategory.setText(category.getName());
                    TVcommentary.setText(commentary);
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String readJson(String fileName){
        String content = null;
        try {
            InputStream is = this.openFileInput(fileName);
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void updateField(String fieldName, String newValue) throws JSONException {
        if (newValue.length() == 0) {
            newValue = getResources().getString(R.string.undefined);
        }
        //Update local Json
        JSONObject jsonReceipts = new JSONObject(readJson("receipts.json"));
        JSONArray jsonArray = jsonReceipts.getJSONArray("receipts");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObj = jsonArray.getJSONObject(i);
            Integer id = jsonObj.getInt("id");
            if (id == getIntent().getExtras().getInt("receiptId")) {
                if (newValue.equals("true")) {
                    jsonObj.put(fieldName, true);
                } else if (newValue.equals("false")) {
                    jsonObj.put(fieldName, false);
                } else {
                    jsonObj.put(fieldName, newValue);
                }
                String jsonFinal = jsonReceipts.toString();
                try {
                    OutputStream outputStream = this.openFileOutput("receipts.json", Context.MODE_PRIVATE);
                    outputStream.write(jsonFinal.getBytes());
                    outputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        //TODO DB UPDATE


        //Refresh activity
        completeField();
    }

    private void removeReceipt() {
        try {
            JSONObject jsonReceipts = new JSONObject(readJson("receipts.json"));
            JSONArray jsonArray = jsonReceipts.getJSONArray("receipts");
            JSONArray jsonArrayFinal = new JSONArray();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonReceipt = jsonArray.getJSONObject(i);
                Integer id = jsonReceipt.getInt("id");
                if (id != getIntent().getExtras().getInt("receiptId")) {
                    jsonArrayFinal.put(jsonReceipt);
                }
                jsonReceipts.put("receipts", jsonArrayFinal);
                String jsonFinal = jsonReceipts.toString();
                try {
                    OutputStream outputStream = this.openFileOutput("receipts.json", Context.MODE_PRIVATE);
                    outputStream.write(jsonFinal.getBytes());
                    outputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void updateAmount(String fieldName, double newValue) throws JSONException {
        //Update local Json
        JSONObject jsonReceipts = new JSONObject(readJson("receipts.json"));
        JSONArray jsonArray = jsonReceipts.getJSONArray("receipts");
        Receipt receipt = null;
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObj = jsonArray.getJSONObject(i);
            Integer id = jsonObj.getInt("id");
            if (id == getIntent().getExtras().getInt("receiptId")) {
                jsonObj.put(fieldName, newValue);
                String jsonFinal = jsonReceipts.toString();
                try {
                    OutputStream outputStream = this.openFileOutput("receipts.json", Context.MODE_PRIVATE);
                    outputStream.write(jsonFinal.getBytes());
                    outputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        //TODO DB UPDATE


        //Refresh activity
        completeField();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void updateDate(String fieldName, DatePicker newValue) throws JSONException {
        int day = newValue.getDayOfMonth();
        int month = newValue.getMonth();
        int year = newValue.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        Date newValueDate = calendar.getTime();
        //Update local Json
        JSONObject jsonReceipts = new JSONObject(readJson("receipts.json"));
        JSONArray jsonArray = jsonReceipts.getJSONArray("receipts");
        Receipt receipt = null;
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObj = jsonArray.getJSONObject(i);
            Integer id = jsonObj.getInt("id");
            if (id == getIntent().getExtras().getInt("receiptId")) {
                jsonObj.put(fieldName, newValueDate);
                String jsonFinal = jsonReceipts.toString();
                try {
                    OutputStream outputStream = this.openFileOutput("receipts.json", Context.MODE_PRIVATE);
                    outputStream.write(jsonFinal.getBytes());
                    outputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        //TODO DB UPDATE


        //Refresh activity
        completeField();
    }
}
