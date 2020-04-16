package com.dev.ettee;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.dev.ettee.ui.DetailReceiptActivity;
import com.dev.ettee.ui.HomeActivity;
import com.dev.ettee.ui.ListCategoryFragment;
import com.dev.ettee.ui.LoginActivity;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {

    ArrayList<Category> listCategory;
    LayoutInflater vg;

    public CategoryAdapter(ArrayList<Category> listCategory){
        this.listCategory = listCategory;
    }

    @Override
    public CategoryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_category_list, null);
        vg = LayoutInflater.from(parent.getContext());

        return new CategoryAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CategoryAdapter.MyViewHolder holder, final int position) {

        holder.name.setText(listCategory.get(position).getName());

        holder.v.setBackgroundResource(R.drawable.background_category_row);
        GradientDrawable gd = (GradientDrawable) holder.v.getBackground().getCurrent();
        gd.setColor(Color.parseColor(listCategory.get(position).getColor()));

        holder.btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                View dialogViewEditCat = vg.inflate(R.layout.dialog_edit_category, null);
                final ColorPickerView colorPickerView = dialogViewEditCat.findViewById(R.id.dialog_edit_category_color_picker);
                final EditText etNameCat = dialogViewEditCat.findViewById(R.id.dialog_edit_category_name_et);
                etNameCat.setText(listCategory.get(position).getName());
                builder.setView(dialogViewEditCat);
                builder.setPositiveButton(R.string.update, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            InputStream is = v.getContext().openFileInput("category.json");
                            int size = is.available();
                            byte[] buffer = new byte[size];
                            is.read(buffer);
                            is.close();
                            String content = new String(buffer);
                            JSONObject jsonCat = new JSONObject(content);
                            JSONArray jsonArray = jsonCat.getJSONArray("category");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObj = jsonArray.getJSONObject(i);
                                Integer idCat = jsonObj.getInt("id");
                                if (idCat == listCategory.get(position).getId()) {
                                    jsonObj.put("name", etNameCat.getText().toString());
                                    jsonObj.put("color", "#"+Integer.toHexString(colorPickerView.getSelectedColor()));
                                    String jsonFinal = jsonCat.toString();

                                    try {
                                        OutputStream outputStream = v.getContext().openFileOutput("category.json", Context.MODE_PRIVATE);
                                        outputStream.write(jsonFinal.getBytes());
                                        outputStream.close();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        } catch (IOException | JSONException ex) {
                            ex.printStackTrace();
                        }
                    }
                });
                builder.setNegativeButton(R.string.cancel, null);
                builder.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return listCategory.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        View v;
        ImageView btn_edit;



        public MyViewHolder(View itemView){
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.row_category_name);
            btn_edit = itemView.findViewById(R.id.row_category_edit_btn);
            v = itemView;
        }

    }
}