package rtrk.pnrs.databaseexample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class StudentAdapter extends BaseAdapter {

    private ArrayList<Student> mStudents;
    private Context mContext;

    public StudentAdapter(Context context) {
        mContext = context;
        mStudents = new ArrayList<Student>();
    }

    @Override
    public int getCount() {
        return mStudents.size();
    }

    @Override
    public Object getItem(int position) {
        return mStudents.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void update(Student[] students) {
        mStudents.clear();
        if(students != null) {
            for(Student student : students) {
                mStudents.add(student);
            }
        }

        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.student_row, null);

            ViewHolder holder = new ViewHolder();
            holder.mFirstName = (TextView)  convertView.findViewById(
                    R.id.text_first_name);
            holder.mLastName = (TextView) convertView.findViewById(
                    R.id.text_last_name);
            holder.mIndex = (TextView) convertView.findViewById(
                    R.id.text_index);
            convertView.setTag(holder);
        }

        Student student = (Student) getItem(position);
        ViewHolder holder = (ViewHolder) convertView.getTag();

        holder.mFirstName.setText(student.getFirstName());
        holder.mLastName.setText(student.getLastName());
        holder.mIndex.setText(student.getIndex());

        return convertView;
    }

    private class ViewHolder {
        public TextView mFirstName = null;
        public TextView mLastName = null;
        public TextView mIndex = null;
    }
}
