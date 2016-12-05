package cl.usm.telematica.sigamobile;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFragment extends Fragment {
    List<Event> eventos;
    List<Event> eventos_samples;
    View vista;
    RecyclerView cardView;
    TextView linkToAllEvents;


    public NewsFragment() {
        // Required empty public constructor
        eventos = new ArrayList<>();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        vista = inflater.inflate(R.layout.fragment_news, container, false);
        if (eventos.size()==0)
            new NewsHandler(this).execute();
        cardView = (RecyclerView) vista.findViewById(R.id.newsRecyclerView);
        linkToAllEvents = (TextView)vista.findViewById(R.id.seeMoreTextView);
        return vista;
    }

    public void changeDataSetNews(List<Event> eventos) {
        this.eventos = eventos;
        eventos_samples = showMaximumEvents(3, eventos);
        cardView.setHasFixedSize(false);
        LinearLayoutManager llm = new LinearLayoutManager(vista.getContext());
        cardView.setLayoutManager(llm);
        EventAdapter adapter = new EventAdapter(eventos_samples);
        cardView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        linkToAllEvents.setText("Ver más...");
        linkToAllEvents.setClickable(true);
        linkToAllEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("eventos",(ArrayList<Event>) getEventos());
                Intent news = new Intent(getActivity(), AllNewsActivity.class);
                news.putExtras(bundle);
                startActivity(news);
            }
        });

    }

    public List<Event> showMaximumEvents(int i, List<Event> eventos) {
        List<Event> temp = new ArrayList<>();
        try {
            Calendar now = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
            for (Event e : eventos) {
                Date dateEvent = df.parse(e.getDate());
                dateEvent = DateUtil.addDays(dateEvent,1);
                if (new Date().before(dateEvent) && temp.size() < i){
                    temp.add(e);
                }
            }
            return temp;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return temp;
    }
    public List<Event> getEventos(){
        return eventos;
    }
}
