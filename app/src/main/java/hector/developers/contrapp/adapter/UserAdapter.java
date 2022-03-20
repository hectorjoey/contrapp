package hector.developers.contrapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import hector.developers.contrapp.R;
import hector.developers.contrapp.model.Users;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {
    Context context;
    List<Users> userList;

    public UserAdapter(List<Users> userList, Context context) {
        this.userList = userList;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.userlist, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Users users = userList.get(position);
        holder.tvFirstName.setText(users.getFirstname());
        holder.tvEmail.setText(users.getEmail());
        holder.tvPhone.setText(users.getPhone());
        holder.tvGender.setText(users.getGender());

        holder.cardView.setAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_scale));
        if (position % 2 == 1) {
            holder.itemView.setBackgroundColor(Color.parseColor("#E8EAF6"));
        } else {
            holder.itemView.setBackgroundColor(Color.parseColor("#BBDEFB"));
        }
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvFirstName;
        private TextView tvEmail;
        private TextView tvPhone;
        private TextView tvGender;
        CardView cardView;


        public ViewHolder(View itemView) {
            super(itemView);
            tvFirstName = itemView.findViewById(R.id.tvFirstname);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            tvPhone = itemView.findViewById(R.id.tvPhone);
            tvGender = itemView.findViewById(R.id.tvGender);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}