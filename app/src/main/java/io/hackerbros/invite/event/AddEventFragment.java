package io.hackerbros.invite.event;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ArrayAdapter;
import android.content.Context;
import android.widget.AutoCompleteTextView;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.lang.StringBuilder;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.io.IOException;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import io.hackerbros.invite.R;
import io.hackerbros.invite.data.Event;
import io.hackerbros.invite.network.InviteNetworkObject;

import com.parse.ParseException;
import com.parse.SaveCallback;

public class AddEventFragment extends Fragment implements View.OnClickListener{
    private static final String TAG = AddEventFragment.class.getSimpleName();

    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";

    private static final String API_KEY = "AIzaSyBy5wH84ZUND8BZma_EhZg0nfTPofWPgz4";

    private Button submitButton;
    private Activity parentActivity;

    public AddEventFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_add_event, container, false);

        AutoCompleteTextView tv = (AutoCompleteTextView) v.findViewById(R.id.add_event_edit_location);
        tv.setAdapter(new PlacesAutoCompleteAdapter(getActivity(), android.R.layout.simple_list_item_1));

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        parentActivity = getActivity();

        submitButton = (Button) parentActivity.findViewById(R.id.add_event_submit_button);
        submitButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.add_event_submit_button) {
            constructEventsFromUserFields();
        }
    }

    private void constructEventsFromUserFields() {
        Event newEvent = new Event();

        EditText titleField = (EditText) parentActivity.findViewById(R.id.add_event_edit_title);
        EditText descriptionField = (EditText) parentActivity.findViewById(R.id.add_event_edit_description);
        RadioGroup selector = (RadioGroup) parentActivity.findViewById(R.id.add_event_type_selector);

        newEvent.setEventTitle(titleField.getText().toString());
        newEvent.setEventDescription(descriptionField.getText().toString());
        newEvent.setPublicEvent(selector.getCheckedRadioButtonId()
                == R.id.add_event_type_selector_public ? true : false);

        newEvent.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Error:", e);
                }
                getActivity().finish();
            }
        });
    }

    private ArrayList<String> autocomplete(String input) {
        ArrayList<String> resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?key=" + API_KEY);
            sb.append("&input=" + URLEncoder.encode(input, "utf-8"));

            URL url = new URL(sb.toString());
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        }
        catch (MalformedURLException e) {
            Log.e(TAG, "Error processing Places API URL", e);
            return resultList;
        }
        catch (IOException e) {
            Log.e(TAG, "Error connecting to Places API", e);
            return resultList;
        }
        finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

            resultList = new ArrayList<String>(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); ++i) {
                resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
            }
        }
        catch (JSONException e) {
            Log.e(TAG, "Cannot process JSON results", e);
        }

        return resultList;
    }

    private class PlacesAutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {
        private ArrayList<String> resultList;
        
        public PlacesAutoCompleteAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }
    
        @Override
        public int getCount() {
            return resultList.size();
        }
    
        @Override
        public String getItem(int index) {
            return resultList.get(index);
        }
    
        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        resultList = autocomplete(constraint.toString());
    
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                    }
                    return filterResults;
                }
    
                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    }
                    else {
                        notifyDataSetInvalidated();
                    }
                }};
            return filter;
        }
    }
}
