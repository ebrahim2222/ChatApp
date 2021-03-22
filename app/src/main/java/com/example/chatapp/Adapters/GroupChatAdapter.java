package com.example.chatapp.Adapters;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatapp.Activites.GroupChatActivity;
import com.example.chatapp.Models.GroupChat;
import com.example.chatapp.R;

import java.util.List;

public class GroupChatAdapter extends RecyclerView.Adapter<GroupChatAdapter.MyHolder> {

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.group_chat_layout, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        GroupChat groupChat = groupChatList.get(position);
        holder.groupText.setText(groupChat.getGroupName());
    }

    @Override
    public int getItemCount() {
        return groupChatList!=null?groupChatList.size():0;
    }
    Context context;
    List<GroupChat> groupChatList;
    public void setData(Context context, List<GroupChat> groupChatList) {
        this.groupChatList = groupChatList;
        this.context = context;
    }

    class MyHolder extends RecyclerView.ViewHolder {
        TextView groupText;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            groupText = itemView.findViewById(R.id.groupChatRow);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GroupChat groupChat = groupChatList.get(getAdapterPosition());
                    Intent intent = new Intent(context, GroupChatActivity.class);
                    intent.putExtra("groupChat",groupChat);
                    context.startActivity(intent);

                }
            });
        }
    }
}
