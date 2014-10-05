package io.hackerbros.invite.feed;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.text.SimpleDateFormat;

import io.hackerbros.invite.R;
import io.hackerbros.invite.activities.EventActivity;
import io.hackerbros.invite.data.Event;
import io.hackerbros.invite.fragments.TitledFragment;

import com.parse.ParseUser;
import com.parse.ParseQueryAdapter;
import com.parse.ParseQuery;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class NewsFeedFragment extends Fragment implements TitledFragment {
    private static SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a - dd MMM yy");
    private ParseQueryAdapter<Event> eventAdapter;

    public NewsFeedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_news_feed, container, false);

        ParseQueryAdapter.QueryFactory<Event> factory = new ParseQueryAdapter.QueryFactory<Event>() {
            @Override
            public ParseQuery<Event> create() {
                ParseQuery<Event> query = Event.getQuery();
                query.include(Event.EVENT_TITLE_KEY);
//                query.include(Event.EVENT_DESCRIPTION_KEY);
                /*
                // ENUM SWITCH
                switch (filterOptions) {
                    case EVENT_DATE:
                        break;
                    case LOCATION:
                        break;
                    case CREATE_TIME:
                        break;
                    case IS_PUBLIC:
                        break;
                }
                */
//                query.orderByDescending(Event.DATE_TIME_KEY);
                return query;
            }
        };

        ListView lv = (ListView) v.findViewById(R.id.news_feed);
        // Add factory when date time is valid
        eventAdapter = new ParseQueryAdapter<Event>(getActivity(), Event.class) {
            @Override
            public View getItemView(Event event, View view, ViewGroup parent) {
                if (view == null) {
                    view = View.inflate(getActivity(), R.layout.event_row, null);
                }

                TextView title = (TextView) view.findViewById(R.id.event_title);
                TextView description = (TextView) view.findViewById(R.id.event_description);

                TextView location = (TextView) view.findViewById(R.id.event_location);
                TextView time = (TextView) view.findViewById(R.id.event_start_time);

                title.setText(event.getEventTitle());
                description.setText(event.getEventDescription());
                location.setVisibility(View.GONE);
                time.setText(sdf.format(event.getUpdatedAt()));


                return view;
            }
        };
        lv.setAdapter(eventAdapter);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (eventAdapter != null) {
            eventAdapter.loadObjects();
        } 
    }

    @Override
    public String getTitle() {
        return "News Feed";
    }

}
