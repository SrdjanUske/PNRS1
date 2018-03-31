package srdjan.usorac.chatapplication;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

public class CharacterAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Custom> mCharacters;

    public CharacterAdapter(Context mContext) {
        this.mContext = mContext;
        mCharacters = new ArrayList<Custom>();
    }

    public void addCharacter(Custom character) {
        mCharacters.add(character);
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return mCharacters.size();
    }

    @Override
    public Object getItem(int i) {
        Object rv = null;

        try {
            rv = mCharacters.get(i);
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

            view.setTag(holder);
        }

        Custom character = (Custom) getItem(i);
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.image.setImageDrawable(character.getImage());
        holder.name.setText(String.valueOf(character.getName()));
        holder.first_letter.setText(String.valueOf(character.getFirst_letter()));


        return view;
    }

    private class ViewHolder {
        public TextView first_letter = null;
        public TextView name = null;
        public ImageView image = null;
    }

}
