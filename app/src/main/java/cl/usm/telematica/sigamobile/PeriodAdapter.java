package cl.usm.telematica.sigamobile;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Pipos on 04-12-2016.
 */

public class PeriodAdapter extends RecyclerView.Adapter<PeriodAdapter.MyViewHolder> {

    private List<CurricularPeriod> periodList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public CourseAdapter courseAdapter;
        public TextView periodName, totalCredits,totalCourses;
        public RecyclerView reciclerView;

        public MyViewHolder(View view) {
            super(view);
            periodName = (TextView) view.findViewById(R.id.periodTextView);
            totalCourses = (TextView) view.findViewById(R.id.coursesTextView);
            totalCredits = (TextView) view.findViewById(R.id.creditsTextView);
            reciclerView = (RecyclerView) view.findViewById(R.id.item_mode);
        }

    }


    public PeriodAdapter(List<CurricularPeriod> periodList) {
        this.periodList = periodList;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.period_cardview_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final CurricularPeriod period = periodList.get(position);
        holder.periodName.setText("Período: "+period.getPeriod());
        holder.totalCredits.setText("Créditos: "+String.valueOf(period.getCredits()));
        holder.totalCourses.setText("Asignaturas: "+String.valueOf(period.getCoursesSize()));

        holder.courseAdapter = new CourseAdapter(period.getCourses());
        holder.reciclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager repoLayoutManager = new LinearLayoutManager(holder.itemView.getContext(), LinearLayoutManager.VERTICAL,false);
        holder.reciclerView.setLayoutManager(repoLayoutManager);
        holder.reciclerView.setItemAnimator(new DefaultItemAnimator());
        holder.reciclerView.setAdapter(holder.courseAdapter);


        holder.courseAdapter.notifyDataSetChanged();



    }

    @Override
    public int getItemCount() {
        return periodList.size();
    }
}