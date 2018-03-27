package rtrk.pnrs.adapterexample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


public class CharacterAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Character> mCharacters;

    public CharacterAdapter(Context context) {
        mContext = context;
        mCharacters = new ArrayList<Character>();
    }

    public void addCharacter(Character character) {
        mCharacters.add(character);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mCharacters.size();
    }

    @Override
    public Object getItem(int position) {
        Object rv = null;
        try {
            rv = mCharacters.get(position);
        } catch (IndexOutOfBoundsException e) {
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
        View view = convertView;

        if(view == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.character_row, null);
            ViewHolder holder = new ViewHolder();
            holder.image = (ImageView) view.findViewById(R.id.image);
            holder.name = (TextView) view.findViewById(R.id.name);
            view.setTag(holder);
        }

        Character character = (Character) getItem(position);
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.image.setImageDrawable(character.mImage);
        holder.name.setText(character.mName);

        return view;
    }

    private class ViewHolder {
        public ImageView image = null;
        public TextView name = null;
    }
}