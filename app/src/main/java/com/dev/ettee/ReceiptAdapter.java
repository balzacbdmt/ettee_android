package com.dev.ettee;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.dev.ettee.ui.DetailReceiptActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ReceiptAdapter extends RecyclerView.Adapter<ReceiptAdapter.MyViewHolder> {

    ArrayList<Receipt> listReceipt;

    public ReceiptAdapter(ArrayList<Receipt> listItems){
        this.listReceipt = listItems;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_receipt_list, null);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        holder.name.setText(listReceipt.get(position).getName());
        if (listReceipt.get(position).getShop() != null) {
            holder.shop.setText(listReceipt.get(position).getShop().getName());
        }
        if (listReceipt.get(position).getAmount() != null) {
            holder.amount.setText(listReceipt.get(position).getAmount().toString()+"€");
        }
        if (listReceipt.get(position).getPurcharseDate() != null) {
            holder.date.setText(dateFormat.format(listReceipt.get(position).getPurcharseDate()));
        }
        if(listReceipt.get(position).getCategory().getColor() != null) {
            holder.v.setBackgroundResource(R.drawable.background_receipt_row);
            GradientDrawable gd = (GradientDrawable) holder.v.getBackground().getCurrent();
            gd.setColor(Color.parseColor(listReceipt.get(position).getCategory().getColor()));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent myIntent = new Intent(v.getContext(), DetailReceiptActivity.class);
            myIntent.putExtra("receiptId", listReceipt.get(position).getId());
            v.getContext().startActivity(myIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listReceipt.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView shop;
        TextView amount;
        TextView date;
        View v;
        public MyViewHolder(View itemView){
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.row_receipt_name);
            shop = (TextView) itemView.findViewById(R.id.row_receipt_shop);
            amount = (TextView) itemView.findViewById(R.id.row_receipt_amount);
            date = (TextView) itemView.findViewById(R.id.row_receipt_date);
            v = itemView;
        }

    }
}
