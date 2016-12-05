package cl.usm.telematica.sigamobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AllNewsActivity extends AppCompatActivity {
    RecyclerView cardView;
    List<Event> eventsToShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_news);
        Intent i = getIntent();
        List<Event> eventos = i.getParcelableArrayListExtra("eventos");
        cardView = (RecyclerView) findViewById(R.id.allnewsRecyclerView);
        if (eventos != null){
            eventsToShow = showMonthEvents(eventos);
            cardView.setHasFixedSize(false);
            LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
            cardView.setLayoutManager(llm);
            EventAdapter adapter = new EventAdapter(eventsToShow);
            cardView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }
    public List<Event> showMonthEvents(List<Event> eventos) {
        List<Event> temp = new ArrayList<>();
        try {
            Date now = new Date();
            Date past = now;
            past = DateUtil.addDays(past,-30);
            Date future = now;
            future = DateUtil.addDays(future,30);
            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
            for (Event e : eventos) {
                Date dateEvent = df.parse(e.getDate());
                dateEvent = DateUtil.addDays(dateEvent,1);
                if (future.after(dateEvent) && past.before(dateEvent)){
                    temp.add(e);
                }
            }
            return temp;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return temp;
    }
}
