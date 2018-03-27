package srdjan.usorac.listadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by student on 27.3.2018.
 */

public class CharacterAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Custom> mCharacters;

    public CharacterAdapter(Context mContext) {
        this.mContext = mContext;
        this.mCharacters = new ArrayList<Custom>();
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
            view = inflater.inflate(R.layout.raw_item, null);
            ViewHolder holder = new ViewHolder();
            holder.image = (ImageView) view.findViewById(R.id.kenny);
            holder.name = (TextView) view.findViewById(R.id.southpark);
            view.setTag(holder);
        }

        Custom character = (Custom) getItem(i);
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.image.setImageDrawable(character.getImage());
        holder.name.setText(character.getName());
        return view;
    }

    private class ViewHolder {
        public ImageView image = null;
        public TextView name = null;
    }
}
