package co.il.liam.booktobook.ADAPTERS;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;

import co.il.liam.booktobook.R;
import co.il.liam.model.Chat;
import co.il.liam.model.Message;
import co.il.liam.model.Messages;
import co.il.liam.model.User;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MessageHolder> {
    private Context context;
    private int messageLayout;
    private Messages messages;
    private User loggedUser;

    private OnItemClickListener listener;
    private OnItemLongClickListener longListener;

    public MessagesAdapter(Context context, int chatLayout, Messages messages, User loggedUser, OnItemClickListener listener, OnItemLongClickListener longListener) {
        this.context = context;
        this.messageLayout = chatLayout;
        this.messages = messages;
        this.loggedUser = loggedUser;
        this.listener = listener;
        this.longListener = longListener;
    }


    public String findMessageType(Message message) {
        User userSender = message.getSender();

        if (loggedUser.equals(userSender)) {
            return "sender";
        }
        return "recipient";
    }

    @NonNull
    @Override
    public MessagesAdapter.MessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MessagesAdapter.MessageHolder(LayoutInflater.from(context).inflate(messageLayout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MessagesAdapter.MessageHolder holder, int position) {
        Message message = messages.get(position);

        if (message != null) {
            holder.bind(message, findMessageType(message), listener, longListener);
        }
    }

    @Override
    public int getItemCount() {
        return messages != null ? messages.size() : 0;
    }


    public static class MessageHolder extends RecyclerView.ViewHolder {
        private LinearLayout llMessageRecipient;
        private TextView tvMessageRecipientContent;
        private TextView tvMessageRecipientTime;
        private TextView tvMessageRecipientDate;


        private LinearLayout llMessageSender;
        private TextView tvMessageSenderContent;
        private TextView tvMessageSenderTime;
        private TextView tvMessageSenderDate;


        public MessageHolder(@NonNull View itemView) {
            super(itemView);

            llMessageRecipient = itemView.findViewById(R.id.llMessageRecipient);
            tvMessageRecipientContent = itemView.findViewById(R.id.tvMessageRecipientContent);
            tvMessageRecipientTime = itemView.findViewById(R.id.tvMessageRecipientTime);
            tvMessageRecipientDate = itemView.findViewById(R.id.tvMessageRecipientDate);

            llMessageSender = itemView.findViewById(R.id.llMessageSender);
            tvMessageSenderContent = itemView.findViewById(R.id.tvMessageSenderContent);
            tvMessageSenderTime = itemView.findViewById(R.id.tvMessageSenderTime);
            tvMessageSenderDate = itemView.findViewById(R.id.tvMessageSenderDate);
        }

        public void bind(Message message, String messageType, OnItemClickListener listener, OnItemLongClickListener longListener) {
            if (Objects.equals(messageType, "sender")) {
                llMessageRecipient.setVisibility(View.GONE);
                llMessageSender.setVisibility(View.VISIBLE);

                tvMessageSenderContent.setText(message.getContent());
                tvMessageSenderTime.setText(message.getTime());
                tvMessageSenderDate.setText(message.getDate());
            }

            else {
                llMessageRecipient.setVisibility(View.VISIBLE);
                llMessageSender.setVisibility(View.GONE);

                tvMessageRecipientContent.setText(message.getContent());
                tvMessageRecipientTime.setText(message.getTime());
                tvMessageRecipientDate.setText(message.getDate());
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClicked(message, getAdapterPosition());
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    longListener.onItemLongClicked(message);
                    return true;
                }
            });

        }
    }

    public void setMessages(Messages messages) {
        this.messages = messages;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClicked(Message message, int position);
    }

    public interface OnItemLongClickListener {
        boolean onItemLongClicked(Message message);
    }
}
