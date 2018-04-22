package com.example.joaovirgili.projetofirebase1.Classes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.joaovirgili.projetofirebase1.R;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private List<User> usersList;
    private Context context;

    public MyAdapter(List<User> usersList, Context context) {
        this.usersList = usersList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.content_user_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.ViewHolder holder, int position) {
        User user = usersList.get(position);
        holder.textViewFirstName.setText(user.getFirstName());
        holder.textViewEmail.setText(user.getEmail());

        byte[] decodedString = Base64.decode(user.getProfileImage(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        holder.imageViewUserProfile.setImageBitmap(decodedByte);
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewFirstName;
        public TextView textViewEmail;
        public ImageView imageViewUserProfile;

        public ViewHolder(View itemView) {
            super(itemView);

            textViewEmail = itemView.findViewById(R.id.textViewEmail);
            textViewFirstName = itemView.findViewById(R.id.textViewFirstName);
            imageViewUserProfile = itemView.findViewById(R.id.cardUserProfile);

        }
    }
}
