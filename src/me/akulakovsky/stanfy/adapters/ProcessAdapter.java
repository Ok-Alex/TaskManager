package me.akulakovsky.stanfy.adapters;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import me.akulakovsky.stanfy.R;

public class ProcessAdapter extends ArrayAdapter<ApplicationInfo> {

    private Context context;
    private LayoutInflater inflater;
    private PackageManager pm;

    public ProcessAdapter(Context context) {
        super(context, R.layout.adapter_item);
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.pm = context.getPackageManager();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        InfoWrapper wrapper;
        ApplicationInfo info;

        if (row == null || row.getTag() == null){
            row = inflater.inflate(R.layout.adapter_item, parent, false);
            wrapper = new InfoWrapper(row);
            row.setTag(wrapper);
        } else {
            wrapper = (InfoWrapper) row.getTag();
        }

        info = getItem(position);

        wrapper.populate(info);

        return row;
    }

    private class InfoWrapper{

        View row;

        ImageView ivIcon;
        TextView tvName;

        private InfoWrapper(View row) {
            this.row = row;
        }

        public void populate(ApplicationInfo applicationInfo){
            getIvIcon().setImageDrawable(applicationInfo.loadIcon(pm));
            getTvName().setText(pm.getApplicationLabel(applicationInfo));
        }

        public ImageView getIvIcon() {
            if (ivIcon == null){
                ivIcon = (ImageView) row.findViewById(R.id.app_icon);
            }
            return ivIcon;
        }

        public TextView getTvName() {
            if (tvName == null){
                tvName = (TextView) row.findViewById(R.id.app_title);
            }
            return tvName;
        }
    }
}
