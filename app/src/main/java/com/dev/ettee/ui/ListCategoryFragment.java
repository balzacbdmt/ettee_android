package com.dev.ettee.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dev.ettee.Category;
import com.dev.ettee.CategoryAdapter;
import com.dev.ettee.R;
import com.dev.ettee.VerticalSpaceItemDecoration;
import com.flask.colorpicker.ColorPickerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class ListCategoryFragment extends Fragment implements View.OnClickListener {
    private RecyclerView recyclerView;
    private static RecyclerView.Adapter rvAdapter;
    private RecyclerView.LayoutManager rvLayoutManager;
    private static ArrayList<Category> listCategory;
    private static final int VERTICAL_ITEM_SPACE = 25;

    ImageView btn_add;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_list_category, container, false);

        listCategory = new ArrayList<>();

        recyclerView = root.findViewById(R.id.listcategory_rc);
        rvLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(rvLayoutManager);
        recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(VERTICAL_ITEM_SPACE));

        parseJsonCategory(readJsonCategory());

        btn_add = root.findViewById(R.id.listcategory_image_add);
        btn_add.setOnClickListener(this);

        return root;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.listcategory_image_add:
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                LayoutInflater inflater = requireActivity().getLayoutInflater();
                View dialogViewEditCat = inflater.inflate(R.layout.dialog_edit_category, null);
                final TextView titleDialogNewCat = dialogViewEditCat.findViewById(R.id.dialog_edit_category_title);
                final ColorPickerView colorPickerView = dialogViewEditCat.findViewById(R.id.dialog_edit_category_color_picker);
                final EditText etNameCat = dialogViewEditCat.findViewById(R.id.dialog_edit_category_name_et);
                titleDialogNewCat.setText(getResources().getString(R.string.new_category));
                etNameCat.setHint(getResources().getString(R.string.category_name));
                builder.setView(dialogViewEditCat);
                builder.setPositiveButton(R.string.update, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            JSONObject jsonCat = new JSONObject(readJsonCategory());
                            JSONArray jsonArray = jsonCat.getJSONArray("category");

                            JSONObject jsonObjCategoryLast = jsonArray.getJSONObject(jsonArray.length()-1);
                            Integer idCatLast = jsonObjCategoryLast.getInt("id");

                            JSONObject newCat = new JSONObject();
                            newCat.put("id", idCatLast+1);
                            newCat.put("name", etNameCat.getText().toString());
                            newCat.put("color", "#"+Integer.toHexString(colorPickerView.getSelectedColor()));

                            jsonArray.put(newCat);
                            jsonCat.put("category", jsonArray);

                            String jsonFinal = jsonCat.toString();
                            try {
                                OutputStream outputStream = getActivity().openFileOutput("category.json", Context.MODE_PRIVATE);
                                outputStream.write(jsonFinal.getBytes());
                                outputStream.close();
                                parseJsonCategory(jsonFinal);
                            } catch (Exception e) {
                                e.printStackTrace();
                                    }
                        } catch (JSONException ex) {
                            ex.printStackTrace();
                        }
                    }
                });
                builder.setNegativeButton(R.string.cancel, null);
                builder.show();
                break;
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

    private void parseJsonCategory(String leJson) {
        try {
            listCategory = new ArrayList<>();
            JSONObject jsonReceipts = new JSONObject(leJson);
            JSONArray jsonArray = jsonReceipts.getJSONArray("category");
            Category category = null;
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObj = jsonArray.getJSONObject(i);

                Integer id = jsonObj.getInt("id");
                String name = jsonObj.getString("name");
                String color = jsonObj.getString("color");

                category = new Category(id, name);
                category.setColor(color);

                listCategory.add(category);
                rvAdapter = new CategoryAdapter(listCategory);
                recyclerView.setAdapter(rvAdapter);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
