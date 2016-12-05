package cl.usm.telematica.sigamobile;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Pipos on 04-12-2016.
 */

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.MyViewHolder> {

    private List<Course> coursesList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView sigla, asignatura, profesor, paral, cred, nota, evaluacion, estado;


        public MyViewHolder(View view) {
            super(view);
            sigla = (TextView) view.findViewById(R.id.siglaTextView);
            asignatura = (TextView) view.findViewById(R.id.asignaturaTextView);
            profesor = (TextView) view.findViewById(R.id.profesorTextView);
            paral = (TextView) view.findViewById(R.id.paralTextView);
            cred = (TextView) view.findViewById(R.id.creditTextView);
            nota = (TextView) view.findViewById(R.id.notaTextView);
            estado = (TextView) view.findViewById(R.id.estadoTextView);
            evaluacion = (TextView) view.findViewById(R.id.evalTextView);

        }

    }


    public CourseAdapter(List<Course> coursesList) {
        this.coursesList = coursesList;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.courses_recyclerview_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final Course course = coursesList.get(position);
        holder.sigla.setText("Sigla: "+course.getInitial());
        holder.asignatura.setText("Asignatura: "+ course.getName());
        holder.profesor.setText("Profesor: "+course.getProfessor());
        holder.paral.setText("Paralelo: "+course.getParallel());
        holder.cred.setText("Créditos: "+String.valueOf(course.getCredits()));
        holder.nota.setText("Nota: "+course.getScore());
        if (course.getScore().equals("")){}
        else if(course.getScore().compareTo("55") >=0 ){
            holder.nota.setTextColor(Color.parseColor("#304FFE"));
        } else if (course.getScore().equals("100")){
            holder.nota.setTextColor(Color.parseColor("#388E3C"));
        }
        else{
            holder.nota.setTextColor(Color.parseColor("#D50000"));
        }
        holder.estado.setText("Estado: "+course.getStatus());
        holder.evaluacion.setText("Evaluación: "+course.getEvaluation());
        if (course.getEvaluation().equals("Aprobada")){
            holder.evaluacion.setTextColor(Color.parseColor("#304FFE"));
        } else if (course.getEvaluation().equals("Reprobada")){
            holder.evaluacion.setTextColor(Color.parseColor("#D50000"));
        }

    }

    @Override
    public int getItemCount() {
        return coursesList.size();
    }
}