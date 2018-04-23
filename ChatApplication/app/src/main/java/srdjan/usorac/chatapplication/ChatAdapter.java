package srdjan.usorac.chatapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

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
            holder.box.setTag(position);

            convertView.setTag(holder);

        }

        SharedPreferences preferences = mContext.getApplicationContext().getSharedPreferences("MyPreferences", 0);
        int senderID = preferences.getInt("senderID", -1);

        Chat chat = (Chat) getItem(position);
        ViewHolder holder = (ViewHolder) convertView.getTag();

        holder.box.setText(chat.getmMessage());

        if (chat.getmSender().getmID() == senderID) {
            convertView.setBackgroundColor(convertView.getResources().getColor(R.color.chat0));
            holder.box.setPadding(400, 0, 20, 0);
        }
        else {
            convertView.setBackgroundColor(convertView.getResources().getColor(R.color.chat1));
            holder.box.setPadding(20, 0, 400, 0);
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
            Chat chat = mDbHelper.getMessage(i);

            mDbHelper.deleteMessage(chat.getId());
            chatList.remove(i);

            notifyDataSetChanged();
            return true;
        }
        else {
            return false;
        }
    }

    private class ViewHolder {
        public TextView box;
    }
}
