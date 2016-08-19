package ivanrudyk.com.open_weather_api.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ivanrudyk.com.open_weather_api.R;

/**
 * Created by Ivan on 19.08.2016.
 */
public class FavoritesLocationAdapter extends BaseAdapter{
    private Context context;
    private ArrayList<String> arrayListLocation = new ArrayList<>();

    public FavoritesLocationAdapter(Context context, List<String> arrayListLocation) {
        this.context = context;
        this.arrayListLocation = (ArrayList<String>) arrayListLocation;
    }



    @Override
    public int getCount() {
        if(arrayListLocation != null)
        return arrayListLocation.size();
        else return 0;
    }

    @Override
    public Object getItem(int position) {
        return arrayListLocation.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.list_location_item, null);
            holder = new ViewHolder();
            holder.tvLocation = (TextView) convertView.findViewById(R.id.tvListLocation);

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        String location = arrayListLocation.get(position);
        holder.tvLocation.setText(location);

        return convertView;
    }

    private static class ViewHolder {
        TextView tvLocation;
    }
}
