package com.coewithgolap.mlhub.Adapters;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.coewithgolap.mlhub.R;
import com.coewithgolap.mlhub.interfaces.CropsItemClickListener;

public class CropsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView name, description;
    public ImageView image;
    public CropsItemClickListener listener;
    private final Context context;

    public CropsViewHolder(@NonNull View itemView) {
        super(itemView);
        context = itemView.getContext();
        name = (TextView)itemView.findViewById(R.id.cropName);
        description = (TextView)itemView.findViewById(R.id.cropDescription);
        image = (ImageView)itemView.findViewById(R.id.cropImage);
    }

    public void setItemClickListener(CropsItemClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        listener.onClick(v, getAdapterPosition(),false);
    }
}
