package com.omernsh.gunlukyemek;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.MyViewHolder> {

    private List<Data> dataList;

    public class MyViewHolder extends RecyclerView.ViewHolder {


        public TextView soru_no, bayii_kodu,soguk,sıcak;


        public MyViewHolder(View view) {
            super(view);

            bayii_kodu = (TextView) view.findViewById(R.id.bayi_kodu);
            soguk = (TextView) view.findViewById(R.id.soguk);
            sıcak = (TextView) view.findViewById(R.id.sıcak);


            soru_no = view.findViewById(R.id.item_sıra);




        }
    }


    public DataAdapter(List<Data> dataList) {
        this.dataList = dataList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Data data = dataList.get(position);


        holder.bayii_kodu.setText(data.getBayii_kodu());
        holder.soguk.setText("Soğuk: "+data.getSoguk());
        holder.sıcak.setText("Sıcak: "+data.getSıcak());
        holder.soru_no.setText(String.valueOf(position + 1));


    }

    @Override
    public int getItemCount() {



        return dataList.size();


    }
}