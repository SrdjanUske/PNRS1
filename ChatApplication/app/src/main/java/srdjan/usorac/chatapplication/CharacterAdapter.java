package srdjan.usorac.chatapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

public class CharacterAdapter extends BaseAdapter implements View.OnClickListener {

    private Context mContext;
    private ArrayList<Contact> mContacts;

    public CharacterAdapter(Context mContext) {
        this.mContext = mContext;
        mContacts = new ArrayList<Contact>();
    }

    public void addCharacter(Contact contact) {
        mContacts.add(contact);
    }

    public void clear() {
        mContacts.clear();
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
            holder.image = view.findViewById(R.id.imageView);
            holder.name = view.findViewById(R.id.contact_name);
            holder.first_letter = view.findViewById(R.id.first_letter);

            holder.first_letter.setBackgroundColor(Color.rgb((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255)));

            holder.image.requestLayout();
            holder.image.getLayoutParams().width = 100;
            holder.image.getLayoutParams().height = 100;
            holder.image.setOnClickListener(this);

            view.setTag(holder);
        }

        Contact contact = (Contact) getItem(i);

        ViewHolder holder = (ViewHolder) view.getTag();

        holder.name.setText(String.valueOf(contact.getUserName()));
        holder.first_letter.setText(String.valueOf(contact.getUserName().toUpperCase().charAt(0)));
        holder.image.setTag(i);

        return view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.imageView:

                int k = Integer.parseInt(v.getTag().toString());
                Contact contact = mContacts.get(k);

                if (v.getId() == R.id.imageView) {

                    Intent intent = new Intent(mContext.getApplicationContext(), MessageActivity.class);
                    intent.putExtra(Contact.name, contact.getUserName());
                    mContext.startActivity(intent);
                }
                break;
        }
    }

    private class ViewHolder {
        public TextView first_letter = null;
        public TextView name = null;
        public ImageView image = null;
    }

}
