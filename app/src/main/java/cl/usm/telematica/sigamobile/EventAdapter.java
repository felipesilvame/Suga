package cl.usm.telematica.sigamobile;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Pipos on 05-12-2016.
 */

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.MyViewHolder> {

    private List<Event> eventList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView date, tipo, descripcion;

        public MyViewHolder(View view) {
            super(view);
            date = (TextView) view.findViewById(R.id.dateTextView);
            tipo = (TextView) view.findViewById(R.id.typeTextView);
            descripcion = (TextView) view.findViewById(R.id.descriptionTextView);
        }

    }


    public EventAdapter(List<Event> eventList) {
        this.eventList = eventList;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_single_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Event event = eventList.get(position);
        holder.date.setText(event.getDate());
        holder.tipo.setText(event.getType());
        holder.descripcion.setText(event.getDescription());

    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }
}