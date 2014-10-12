package io.hackbros.invite.view_adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import io.hackbros.invite.R;

public class SpinnerListAdapter extends BaseAdapter implements SpinnerAdapter{

    public static final int DEFUALT_SELECTED_DISTANCE_VAL = 4;
    int[] distances;
    Context context;
    int curSelected = DEFUALT_SELECTED_DISTANCE_VAL; // for 25 miles

    public SpinnerListAdapter (Context mContext, int[] mDistances) {
        context = mContext;
        distances = mDistances;
    }

    @Override
    public int getCount() {
        return distances.length;
    }

    @Override
    public Object getItem(int i) {
        return distances[i];
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public void setCurSelected(int i) {
        this.curSelected = i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.spinner_display, null);

        TextView distanceText = (TextView) view.findViewById(R.id.spinner_distance_display);

        distanceText.setText("Events Within: " + distances[i] + " " + (distances[i] > 1 ? "miles" : "mile"));

        return view;
    }

    @Override
    public View getDropDownView(int i, View view, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.spinner_row, null);

        TextView distanceText = (TextView) view.findViewById(R.id.spinner_distance_display);
        ImageView distanceCheck = (ImageView) view.findViewById(R.id.spinner_distance_check);

        distanceText.setText(distances[i] + " " + (distances[i] > 1 ? "miles" : "mile"));

        if(i == curSelected) {
            distanceCheck.setVisibility(View.VISIBLE);
        }

        return view;
    }
}
