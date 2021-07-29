package com.createch.species.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.speech.SpeechRecognizer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.createch.species.Model.Species;
import com.createch.species.R;

import java.util.List;

public class SpeciesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {


    public List<Species> mItemList;
    public  Context cc;

    public SpeciesAdapter(List<Species> itemList, Context cc) {

        mItemList = itemList;
        this.cc =cc;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.species_list_item, parent, false);
        view.setOnClickListener(this::onClick);
        return new ItemViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
            populateItemRows((ItemViewHolder) viewHolder, position);


    }

    @Override
    public int getItemCount() {
        return mItemList == null ? 0 : mItemList.size();
    }

    @Override
    public void onClick(View v) {
     /*   View cd = view.findViewById(R.id.mcd);

        if (((Species) get(position)).isSelected()) {
            cd.setBackgroundColor(Color.WHITE);
            ((Species)parent.getItemAtPosition(position)).setSelected(false);
        }
        else {
            cd.setBackgroundColor(getResources().getColor(R.color.green));
            ((Species)parent.getItemAtPosition(position)).setSelected(true);

        }
        Toast.makeText(getApplicationContext()," selected ",Toast.LENGTH_SHORT).show();
*/

    }


    private class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView classification;
        TextView designation;
        View mcd;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            classification = itemView.findViewById(R.id.classification);
            designation = itemView.findViewById(R.id.designantion);
            mcd = itemView.findViewById(R.id.mcd);
        }
    }


    private void populateItemRows(ItemViewHolder viewHolder, int position) {

        Species item = mItemList.get(position);
        viewHolder.name.setText(item.getName());
        viewHolder.designation.setText(item.getDesignation());
        viewHolder.classification.setText(item.getClassification());
  

    }


}
