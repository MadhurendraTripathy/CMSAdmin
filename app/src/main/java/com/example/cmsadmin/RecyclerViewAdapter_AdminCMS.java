package com.example.cmsadmin;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class RecyclerViewAdapter_AdminCMS extends RecyclerView.Adapter<RecyclerViewAdapter_AdminCMS.innerClassViewHolder> {

    ArrayList<ComplaintParameters> mArrayList,mArrayListFull;

    RecyclerViewAdapter_AdminCMS(ArrayList<ComplaintParameters> mArrayList){
        this.mArrayList = mArrayList;
        mArrayListFull = new ArrayList<>(mArrayList);
    }

    public interface clickOnCardViewHandler{
        void CardClicked(int pos);
    }
    clickOnCardViewHandler pointer_to_clickOnCardViewHandler;
    void openCardView(clickOnCardViewHandler pointer_to_clickOnCardViewHandler){
        this.pointer_to_clickOnCardViewHandler = pointer_to_clickOnCardViewHandler;
    }


    @NonNull
    @Override
    public innerClassViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view_for_recycler_view,viewGroup,false);
        innerClassViewHolder vh = new innerClassViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull innerClassViewHolder vh, int pos) {
        ComplaintParameters cp_current_item = mArrayList.get(pos);
        vh.unitIdToBeShownInCardView.setText(cp_current_item.getUnitid());
        vh.complaintIdToBeShownInCardView.setText(cp_current_item.getComplainId());
        vh.categoeryToBeShownInCardView.setText(cp_current_item.getCategory());

        vh.assignedTo_ToBeShownInCardView.setText(cp_current_item.getAssignedTo());

        if(cp_current_item.getSeverity().equals("High")){
            vh.urgencyFlagInCardView.setImageResource(R.drawable.important_flag);
        }
        else{
            vh.urgencyFlagInCardView.setImageResource(R.drawable.plussign);
        }
    }


    @Override
    public int getItemCount() {
        if(mArrayList!=null){
            return mArrayList.size();
        }
        return 0;
    }

    class innerClassViewHolder extends RecyclerView.ViewHolder{
        TextView unitIdToBeShownInCardView;
        TextView complaintIdToBeShownInCardView;
        TextView categoeryToBeShownInCardView;
        TextView assignedTo_ToBeShownInCardView;
        ImageView urgencyFlagInCardView;

        public innerClassViewHolder(@NonNull View itemView) {
            super(itemView);
            unitIdToBeShownInCardView = itemView.findViewById(R.id.unitIdToBeShownInCardView);
            complaintIdToBeShownInCardView = itemView.findViewById(R.id.cardViewComplaintId);
            categoeryToBeShownInCardView = itemView.findViewById(R.id.cardViewCategoery);
            assignedTo_ToBeShownInCardView = itemView.findViewById(R.id.cardViewAssignedTo);
            urgencyFlagInCardView = itemView.findViewById(R.id.cardViewUrgencyFlag);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(pointer_to_clickOnCardViewHandler != null && getAdapterPosition()!=RecyclerView.NO_POSITION){
                        pointer_to_clickOnCardViewHandler.CardClicked(getAdapterPosition());
                    }
                }
            });

        }
    }


    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<ComplaintParameters> filteredlist = new ArrayList<>();
            if(constraint == null || constraint.length()==0){
                filteredlist.addAll(mArrayListFull);
            }
            else{
                String filterPattern = constraint.toString().toLowerCase().trim();
                for(ComplaintParameters myitem : mArrayListFull){
                    if(myitem.getUnitid().toLowerCase().contains(filterPattern) || myitem.getCategory().toLowerCase().contains(filterPattern) || myitem.getSeverity().toLowerCase().contains(filterPattern)){
                        filteredlist.add(myitem);
                    }
                }
            }

            FilterResults  results = new FilterResults();
            results.values = filteredlist;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mArrayList.clear();
            mArrayList.addAll((ArrayList) results.values);
            notifyDataSetChanged();
        }

    };

}
