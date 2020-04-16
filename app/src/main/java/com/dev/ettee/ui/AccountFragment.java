package com.dev.ettee.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

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
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AccountFragment extends Fragment implements View.OnClickListener {
    TextView TVusername;
    TextView TVpassword;
    TextView TVfirstname;
    TextView TVlastname;
    TextView TVmail;
    TextView TVaddress;
    TextView TVcurrency;
    Integer idAddress;
    ImageView signOutBtn;

    Address address = null;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_account, container, false);

        TVusername = root.findViewById(R.id.account_username_value);
        TVusername.setOnClickListener(this);
        TVpassword = root.findViewById(R.id.account_password_value);
        TVpassword.setOnClickListener(this);
        TVfirstname = root.findViewById(R.id.account_firstname_value);
        TVfirstname.setOnClickListener(this);
        TVlastname = root.findViewById(R.id.account_lastname_value);
        TVlastname.setOnClickListener(this);
        TVmail = root.findViewById(R.id.account_email_value);
        TVmail.setOnClickListener(this);
        TVaddress = root.findViewById(R.id.account_address_value);
        TVaddress.setOnClickListener(this);
        TVcurrency = root.findViewById(R.id.account_currency_value);
        TVcurrency.setOnClickListener(this);
        signOutBtn = root.findViewById(R.id.account_sign_out_btn);
        signOutBtn.setOnClickListener(this);

        parseJSON();

        return root;
    }

    @Override
    public void onClick(final View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View dialogViewField = inflater.inflate(R.layout.dialog_edit_field, null);
        TextView dialogTitleField = dialogViewField.findViewById(R.id.dialog_edit_field_title);
        final EditText dialogEditTextField = dialogViewField.findViewById(R.id.dialog_edit_field_et);

        View dialogViewSpinner = inflater.inflate(R.layout.dialog_edit_spinner, null);
        TextView dialogTitleSpinner = dialogViewSpinner.findViewById(R.id.dialog_edit_spinner_title);
        final Spinner dialogSpinnerSpinner = dialogViewSpinner.findViewById(R.id.dialog_edit_spinner_spinner);

        View dialogViewAddress = inflater.inflate(R.layout.dialog_edit_address, null);
        final EditText dialogEditTextAddressNumber = dialogViewAddress.findViewById(R.id.dialog_address_number_et);
        final EditText dialogEditTextAddressStreet = dialogViewAddress.findViewById(R.id.dialog_address_street_et);
        final EditText dialogEditTextAddressCity = dialogViewAddress.findViewById(R.id.dialog_address_city_et);
        final EditText dialogEditTextAddressCountry = dialogViewAddress.findViewById(R.id.dialog_address_country_et);
        final EditText dialogEditTextAddressZipcode = dialogViewAddress.findViewById(R.id.dialog_address_zipcode_et);

        View dialogViewPassword = inflater.inflate(R.layout.dialog_edit_password, null);
        final EditText dialogEditTextPasswordCurrent = dialogViewPassword.findViewById(R.id.dialog_edit_password_et_current);
        final EditText dialogEditTextPasswordNew = dialogViewPassword.findViewById(R.id.dialog_edit_password_et_new);
        final EditText dialogEditTextPasswordConf = dialogViewPassword.findViewById(R.id.dialog_edit_password_et_conf);

        builder.setNegativeButton(R.string.cancel, null);
        switch (v.getId()) {
            case R.id.account_username_value:
                builder.setView(dialogViewField);
                dialogTitleField.setText(getResources().getString(R.string.account_username));
                dialogEditTextField.setText(TVusername.getText());
                builder.setPositiveButton(R.string.update, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            updateField("username", dialogEditTextField.getText().toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                builder.show();
                break;
            case R.id.account_email_value:
                builder.setView(dialogViewField);
                dialogTitleField.setText(getResources().getString(R.string.account_email));
                dialogEditTextField.setText(TVmail.getText());
                builder.setPositiveButton(R.string.update, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            updateField("email", dialogEditTextField.getText().toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                builder.show();
                break;
            case R.id.account_firstname_value:
                builder.setView(dialogViewField);
                dialogTitleField.setText(getResources().getString(R.string.account_firstname));
                dialogEditTextField.setText(TVfirstname.getText());
                builder.setPositiveButton(R.string.update, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            updateField("firstname", dialogEditTextField.getText().toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                builder.show();
                break;
            case R.id.account_lastname_value:
                builder.setView(dialogViewField);
                dialogTitleField.setText(getResources().getString(R.string.account_lastname));
                dialogEditTextField.setText(TVlastname.getText());
                builder.setPositiveButton(R.string.update, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            updateField("lastname", dialogEditTextField.getText().toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                builder.show();
                break;
            case R.id.account_address_value:
                builder.setView(dialogViewAddress);
                dialogEditTextAddressNumber.setText(address.getNumber()+"");
                dialogEditTextAddressStreet.setText(address.getStreet());
                dialogEditTextAddressCity.setText(address.getCity());
                dialogEditTextAddressCountry.setText(address.getCountry());
                dialogEditTextAddressZipcode.setText(address.getZipCode());
                final Address finalAddress = address;
                builder.setPositiveButton(R.string.update, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            finalAddress.setNumber(Integer.parseInt(dialogEditTextAddressNumber.getText().toString()));
                            finalAddress.setStreet(dialogEditTextAddressStreet.getText().toString());
                            finalAddress.setCity(dialogEditTextAddressCity.getText().toString());
                            finalAddress.setCountry(dialogEditTextAddressCountry.getText().toString());
                            finalAddress.setZipCode(dialogEditTextAddressZipcode.getText().toString());
                            updateAddress("username", finalAddress);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                builder.show();
                break;
            case R.id.account_currency_value:
                List<String> listCurrency = new ArrayList<String>();
                builder.setView(dialogViewSpinner);
                dialogTitleSpinner.setText(getResources().getString(R.string.account_currency));
                listCurrency.add(getResources().getString(R.string.euro));
                listCurrency.add(getResources().getString(R.string.dollars));
                listCurrency.add(getResources().getString(R.string.pound_sterling));
                ArrayAdapter<String> SpinnerAdapterCurrency = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, listCurrency);
                SpinnerAdapterCurrency.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                dialogSpinnerSpinner.setAdapter(SpinnerAdapterCurrency);
                builder.setPositiveButton(R.string.update, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            updateField("currency", dialogSpinnerSpinner.getSelectedItem().toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                builder.show();
                break;
            case R.id.account_password_value:
                builder.setView(dialogViewPassword);
                builder.setPositiveButton(R.string.update, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        JSONObject jsonUser = null;
                        String passwordCurrent = "";
                        try {
                            jsonUser = new JSONObject(readJson("user.json"));
                            passwordCurrent = jsonUser.getString("password");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (passwordCurrent.equals(dialogEditTextPasswordCurrent.getText().toString())) {
                            //TODO new can't be same like current and avoid
                            //TODO avoid if conf is not equals
                            //TODO if length > X ?
                            if ( dialogEditTextPasswordNew.getText().toString().equals(dialogEditTextPasswordConf.getText().toString())) {
                                try {
                                    updateField("password", dialogEditTextPasswordNew.getText().toString());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                });
                builder.show();
                break;
            case R.id.account_sign_out_btn:
                //TODO clear user.json
                Intent myIntent = new Intent(v.getContext(), LoginActivity.class);
                v.getContext().startActivity(myIntent);
                break;
        }
    }

    private void updateField(String fieldName, String newValue) throws JSONException {
        if (newValue.length() == 0) {
            newValue = getResources().getString(R.string.undefined);
        }
        //Update local Json
        JSONObject userJson = new JSONObject(readJson("user.json"));
        userJson.put(fieldName, newValue);
        String userJsonFinal = userJson.toString();
        try {
            OutputStream outputStream = getActivity().openFileOutput("user.json", Context.MODE_PRIVATE);
            outputStream.write(userJsonFinal.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //TODO DB UPDATE


        //Refresh fragment

        parseJSON();
    }

    private void updateAddress(String fieldName, Address address) throws JSONException {
        //Update local Json
        JSONObject jsonAddress = new JSONObject(readJson("address.json"));
        JSONArray jsonArrayAddress= jsonAddress.getJSONArray("address");
        for (int j = 0; j < jsonArrayAddress.length(); j++) {
            JSONObject jsonObjAddress = jsonArrayAddress.getJSONObject(j);
            Integer idAddressJson = jsonObjAddress.getInt("id");
            if (address.getId() == idAddressJson) {
                jsonObjAddress.put("number", address.getNumber());
                jsonObjAddress.put("street", address.getStreet());
                jsonObjAddress.put("city", address.getCity());
                jsonObjAddress.put("country", address.getCountry());
                jsonObjAddress.put("zipcode", address.getZipCode());
            }
        }
        String addressJsonFinal = jsonAddress.toString();
        try {
            OutputStream outputStream = getActivity().openFileOutput("address.json", Context.MODE_PRIVATE);
            outputStream.write(addressJsonFinal.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //TODO DB UPDATE

        parseJSON();
    }

    public String readJson(String fileName){
        String content = null;
        try {
            InputStream is = getActivity().openFileInput(fileName);
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

    private void parseJSON(){
        try {
            JSONObject jsonUser = new JSONObject(readJson("user.json"));
            String username = jsonUser.getString("username");
            String firstName = jsonUser.getString("firstname");
            String lastName = jsonUser.getString("lastname");
            String email = jsonUser.getString("email");
            idAddress = jsonUser.getInt("addressId");
            String currency =  jsonUser.getString("currency");

            JSONObject jsonAddress = new JSONObject(readJson("address.json"));
            JSONArray jsonArrayAddress= jsonAddress.getJSONArray("address");
            for (int j = 0; j < jsonArrayAddress.length(); j++) {
                JSONObject jsonObjAddress = jsonArrayAddress.getJSONObject(j);
                Integer idAddressJson = jsonObjAddress.getInt("id");
                if (idAddress == idAddressJson) {
                    int numberCurrent = jsonObjAddress.getInt("number");
                    String streetCurrent = jsonObjAddress.getString("street");
                    String cityCurrent = jsonObjAddress.getString("city");
                    String countryCurrent = jsonObjAddress.getString("country");
                    String zipcodeCurrent = jsonObjAddress.getString("zipcode");

                    address = new Address(numberCurrent, streetCurrent, cityCurrent, countryCurrent, zipcodeCurrent);
                }
            }

            if (firstName.length() == 0) { firstName = getResources().getString(R.string.undefined); }
            if (lastName.length() == 0) { lastName = getResources().getString(R.string.undefined); }
            if (email.length() == 0) { email = getResources().getString(R.string.undefined); }
            if (currency.length() == 0) { currency = getResources().getString(R.string.undefined); }

            TVusername.setText(username);
            TVfirstname.setText(firstName);
            TVlastname.setText(lastName);
            TVmail.setText(email);
            TVcurrency.setText(currency);
            TVaddress.setText(address.getNumber()+", "+address.getStreet()+", "+address.getCity());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
