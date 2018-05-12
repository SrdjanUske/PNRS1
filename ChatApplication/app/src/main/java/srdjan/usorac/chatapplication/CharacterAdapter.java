package srdjan.usorac.chatapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

public class CharacterAdapter extends BaseAdapter implements View.OnClickListener {

    private Context mContext;
    private ArrayList<User> mContacts;

    public CharacterAdapter(Context mContext) {
        this.mContext = mContext;
        mContacts = new ArrayList<User>();
    }

    public void addCharacter(User character) {
        mContacts.add(character);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mContacts.size();
    }

    @Override
    public Object getItem(int i) {
        Object rv = null;

        try {
            rv = mContacts.get(i);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return rv;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.character_row, null);

            ViewHolder holder = new ViewHolder();
            holder.image = (ImageView) view.findViewById(R.id.imageView);
            holder.name = (TextView) view.findViewById(R.id.contact_name);
            holder.first_letter = (TextView) view.findViewById(R.id.first_letter);

            holder.first_letter.setBackgroundColor(Color.rgb((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255)));

            holder.image.requestLayout();
            holder.image.getLayoutParams().width = 100;
            holder.image.getLayoutParams().height = 100;
            holder.image.setOnClickListener(this);
            holder.image.setTag(i);

            view.setTag(holder);
        }

        User contact = (User) getItem(i);
        ViewHolder holder = (ViewHolder) view.getTag();

        holder.name.setText(String.valueOf(contact.getUsername()));
        holder.first_letter.setText(String.valueOf(contact.getUsername().toUpperCase().charAt(0)));

        return view;
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.imageView) {

            int k = Integer.parseInt(v.getTag().toString());
            User contact = mContacts.get(k);

            Intent intent = new Intent(mContext.getApplicationContext(), MessageActivity.class);

            Bundle bundle = new Bundle();
            bundle.putString("contact_name", contact.getUsername());
            bundle.putString("receiverID", contact.getSessionID());

            intent.putExtras(bundle);
            mContext.startActivity(intent);
        }
    }

    private class ViewHolder {
        public TextView first_letter = null;
        public TextView name = null;
        public ImageView image = null;
    }

}
