package co.il.liam.booktobook.ADAPTERS;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import co.il.liam.booktobook.R;
import co.il.liam.model.Chat;
import co.il.liam.model.Chats;
import co.il.liam.model.User;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatHolder> {
    private Context context;
    private int chatLayout;
    private Chats chats;
    private User loggedUser;

    private OnItemClickListener listener;
    private OnItemLongClickListener longListener;

    public ChatAdapter(Context context, int chatLayout, Chats chats, User loggedUser, OnItemClickListener listener, OnItemLongClickListener longListener) {
        this.context = context;
        this.chatLayout = chatLayout;
        this.chats = chats;
        this.loggedUser = loggedUser;
        this.listener = listener;
        this.longListener = longListener;
    }

    @NonNull
    @Override
    public ChatAdapter.ChatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChatHolder(LayoutInflater.from(context).inflate(chatLayout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.ChatHolder holder, int position) {
        Chat chat = chats.get(position);

        if (chat != null) {
            holder.bind(chat, findRecepientUser(chat), listener, longListener);
        }
    }

    private User findRecepientUser(Chat chat) {
        User user1 = chat.getUserOne();
        User user2 = chat.getUserTwo();

        if (loggedUser.equals(user1)) {
            return user2;
        }
        else if (loggedUser.equals(user2)) {
            return user1;
        }
        else {
            return null;
        }
    }

    @Override
    public int getItemCount() {
        return (chats != null) ? chats.size() : 0;
    }



    public static class ChatHolder extends RecyclerView.ViewHolder {
        private TextView tvChatUsername;
        private TextView tvChatLastMessage;
        private TextView tvChatLastTime;
        private TextView tvChatLastDate;

        public ChatHolder(@NonNull View itemView) {
            super(itemView);

            tvChatUsername = itemView.findViewById(R.id.tvChatUsername);
            tvChatLastMessage = itemView.findViewById(R.id.tvChatLastMessage);
            tvChatLastTime = itemView.findViewById(R.id.tvChatLastTime);
            tvChatLastDate = itemView.findViewById(R.id.tvChatLastDate);
        }


        public void bind(Chat chat, User recepientUser, ChatAdapter.OnItemClickListener listener, ChatAdapter.OnItemLongClickListener longListener) {
            tvChatUsername.setText(recepientUser.getUsername());
            tvChatLastMessage.setText(chat.getLastMessage());
            tvChatLastTime.setText(chat.getLastTime());
            tvChatLastDate.setText(chat.getLastDate());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClicked(chat, getAdapterPosition());
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    longListener.onItemLongClicked(chat);
                    return true;
                }
            });
        }

    }


    public void setChats(Chats chats) {
        this.chats = chats;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClicked(Chat chat, int position);
    }

    public interface OnItemLongClickListener {
        boolean onItemLongClicked(Chat chat);
    }
}
