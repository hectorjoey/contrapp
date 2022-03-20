package hector.developers.contrapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import hector.developers.contrapp.R;
import hector.developers.contrapp.details.HealthDetailsActivity;
import hector.developers.contrapp.model.UserHealthData;


public class UserHealthAdapter extends RecyclerView.Adapter<UserHealthAdapter.ViewHolder> implements Filterable {
    List<UserHealthData> userHealthDataList;
    List<UserHealthData> userHealthDataList1;
    Context context;

    //getting current date and time using Date class
    @SuppressLint("SimpleDateFormat")
    DateFormat df = new SimpleDateFormat("MM/dd/yy");
    Date currentDate = new Date();


    //private UserHealthAdapterListener listener;
    public UserHealthAdapter(Context context, List<UserHealthData> userHealthDataList) {
        this.userHealthDataList = userHealthDataList;
        this.userHealthDataList1 = userHealthDataList;
        this.context = context;
    }

    public void removeItem(int position) {
        userHealthDataList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, userHealthDataList.size());
    }

    public void restoreItem(UserHealthData userHealthData, int position) {
        userHealthDataList.add(position, userHealthData);
        // notify item added by position
        notifyItemInserted(position);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.health_list, parent, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        UserHealthData userHealthData = userHealthDataList.get(position);
        holder.tvFirstname.setText(userHealthData.getFirstname());
        holder.tvDate.setText(userHealthData.getDate());
        holder.tvRisk.setText(userHealthData.getRisk());

        holder.cardView.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_scale));
        if (holder.tvRisk.getText().equals("High Risk")) {
            holder.cardView.setBackgroundColor(Color.parseColor("#F35245"));
//        } else if (holder.tvRisk.getText().equals("Low Risk")) {
//            holder.cardView.setBackgroundColor(Color.parseColor("#32CD30"));
        } else {
//            holder.cardView.setBackgroundColor(Color.parseColor("#f5e342"));
            holder.cardView.setBackgroundColor(Color.parseColor("#32CD30"));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, HealthDetailsActivity.class);
                intent.putExtra("key", userHealthData);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userHealthDataList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvFirstname;
        private TextView tvDate;
        private CardView cardView;
        private TextView tvRisk;

        public ViewHolder(View itemView) {
            super(itemView);
            tvFirstname = itemView.findViewById(R.id.tvFullname);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvRisk = itemView.findViewById(R.id.tvRisk);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    userHealthDataList1 = userHealthDataList;
                } else {
                    List<UserHealthData> filteredList = new ArrayList<>();
                    for (UserHealthData row : userHealthDataList) {
//                         name match condition. this might differ depending on your requirement
//                         here we are looking for name or phone number match
//                        if (row.getDate().contains(charString.toLowerCase()) || row.getPhone().contains(charSequence)) {
//                            filteredList.add(row);
//                        }

//                        if (row.getDate().contains(charString.toLowerCase())) {
//                            filteredList.add(row);
//                        }
                    }

                    userHealthDataList1 = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = userHealthDataList1;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                userHealthDataList1 = (ArrayList<UserHealthData>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
//
//    public interface UserHealthAdapterListener {
//        void onContactSelected(UserHealthData contact);
//    }
}