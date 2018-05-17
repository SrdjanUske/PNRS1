package srdjan.usorac.chatapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ChatAdapter extends BaseAdapter implements View.OnLongClickListener {

    private Context mContext;
    private ArrayList<Chat> chatList;
    private DbHelper mDbHelper;

    public ChatAdapter(Context context) {
        this.mContext = context;
        this.chatList = new ArrayList<Chat>();
    }

    public void addMessage(Chat message) {
        chatList.add(message);
    }

    public void clear() {
        chatList.clear();
    }

    public void update(Chat[] chats) {

        chatList.clear();
        if(chats != null) {
            for(Chat chat : chats) {
                chatList.add(chat);
            }
        }

        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return chatList.size();
    }

    @Override
    public Object getItem(int position) {
        Object rv = null;

        try {
            rv = chatList.get(position);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return rv;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.message_box, null);

            ViewHolder holder = new ViewHolder();
            holder.box = convertView.findViewById(R.id.message_box);
            holder.send_receive = convertView.findViewById(R.id.send_receive);
            holder.box.setTag(position);
            holder.send_receive.setTag(position);

            convertView.setTag(holder);

        }

        SharedPreferences preferences = mContext.getApplicationContext().getSharedPreferences("MyPreferences", 0);
        String sender = preferences.getString("logged_in", null);

        Chat chat = (Chat) getItem(position);
        ViewHolder holder = (ViewHolder) convertView.getTag();

        holder.box.setText(chat.getmMessage());

        if (chat.getmSender().getUserName().equals(sender)) {
            convertView.setBackgroundColor(convertView.getResources().getColor(R.color.chat0));
            holder.box.setGravity(Gravity.END);
            holder.send_receive.setText("S:");
            holder.send_receive.setGravity(Gravity.START);
        }
        else {
            convertView.setBackgroundColor(convertView.getResources().getColor(R.color.chat1));
            holder.box.setGravity(Gravity.START);
            holder.send_receive.setText("R:");
            holder.send_receive.setGravity(Gravity.END);
        }

        holder.box.setLongClickable(true);
        holder.box.setOnLongClickListener(this);

        return convertView;
    }

    @Override
    public boolean onLongClick(View v) {
        if (v.getId() == R.id.message_box) {

            int i = Integer.parseInt(v.getTag().toString());

            mDbHelper = DbHelper.getInstance(mContext.getApplicationContext());
            Chat chat = chatList.get(i);

            SharedPreferences preferences = mContext.getApplicationContext().getSharedPreferences("MyPreferences", 0);
            String sender = preferences.getString("logged_in", null);

            if (!chat.getmSender().getUserName().equals(sender)) {
                Toast.makeText(mContext.getApplicationContext(), "Cannot delete received messages!", Toast.LENGTH_SHORT).show();
            }
            else {
                mDbHelper.deleteMessage(chat.getId());
                chatList.remove(i);
                notifyDataSetChanged();
            }
            return true;
        }
        else {
            return false;
        }
    }

    private class ViewHolder {
        public TextView box;
        public TextView send_receive;
    }
}
