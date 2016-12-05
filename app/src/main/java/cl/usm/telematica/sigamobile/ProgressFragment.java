package cl.usm.telematica.sigamobile;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProgressFragment extends Fragment {
    String get_parameters = "";
    List<String> carreras;
    ArrayList<String> combo_planText;
    ArrayList<String> combo_planValue;
    ArrayList<String> combo_rolText;
    ArrayList<String> combo_cursoText;
    ArrayList<String> combo_carreraText;
    ArrayList<String> combo_sedeText;
    ArrayList<Integer> combo_codcarrValue;
    ArrayList<Integer> combo_codmencValue;
    ArrayList<Integer> combo_codsedeValue;
    ArrayList<Integer> combo_codscarValue;
    ArrayList<Object> combo_paralValue;
    ArrayList<Integer> combo_codClasifValue;
    ArrayList<String> combo_fechExam;
    ArrayList<String> combo_fechReso;
    ArrayList<Integer> combo_anoIngresoValue;
    View vista;
    ArrayAdapter<String> dataAdapter;
    AvanceHandler mHandler;
    List<CurricularPeriod> periodos;
    RecyclerView cardView;

    public ProgressFragment() {
        // Required empty public constructor
        combo_planText = new ArrayList<>();
        combo_planValue= new ArrayList<>();
        combo_rolText= new ArrayList<>();
        combo_cursoText= new ArrayList<>();
        combo_carreraText= new ArrayList<>();
        combo_sedeText= new ArrayList<>();
        combo_codcarrValue= new ArrayList<>();
        combo_codmencValue= new ArrayList<>();
        combo_codsedeValue= new ArrayList<>();
        combo_codscarValue= new ArrayList<>();
        combo_paralValue= new ArrayList<>();
        combo_codClasifValue= new ArrayList<>();
        combo_fechExam= new ArrayList<>();
        combo_fechReso= new ArrayList<>();
        combo_anoIngresoValue = new ArrayList<>();
        mHandler = new AvanceHandler(this);
    }

    @Override
    public void onCreate(Bundle savedbundlestate){

        super.onCreate(savedbundlestate);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        carreras = new ArrayList<String>();
        // Inflate the layout for this fragment
        vista = inflater.inflate(R.layout.fragment_progress, container, false);
        RelativeLayout relativeLayout = (RelativeLayout) vista.findViewById(R.id.fragmentrelativelayout);
        new ProgressHandler(container,this).execute(relativeLayout);
        cardView = (RecyclerView) vista.findViewById(R.id.cardView);

        return vista;
    }

    public void changeDataSetCarreras(){
        Spinner spinnerCarreras = (Spinner) vista.findViewById(R.id.spinner);
        dataAdapter = new ArrayAdapter<String>(this.getContext(),
                android.R.layout.simple_spinner_item, carreras);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCarreras.setAdapter(dataAdapter);
        spinnerCarreras.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                if(mHandler.getPeriodSelected(position+1)){
                    cardView.setHasFixedSize(false);
                    LinearLayoutManager llm = new LinearLayoutManager(vista.getContext());
                    cardView.setLayoutManager(llm);
                    PeriodAdapter adapter = new PeriodAdapter(periodos);
                    cardView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }

        });
        dataAdapter.notifyDataSetChanged();
        if (mHandler.getPeriodSelected(1)){
            cardView.setHasFixedSize(false);
            LinearLayoutManager llm = new LinearLayoutManager(vista.getContext());
            cardView.setLayoutManager(llm);
            PeriodAdapter adapter = new PeriodAdapter(periodos);
            cardView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }



    }

    public void addcarrera(String value){carreras.add(value);}
    public void addcombo_planText(String value){combo_planText.add(value);}
    public void addcombo_planValue(String value){combo_planValue.add(value);}
    public void addcombo_rolText(String value){combo_rolText.add(value);}
    public void addcombo_cursoText(String value){combo_cursoText.add(value);}
    public void addcombo_carreraText(String value){combo_carreraText.add(value);}
    public void addcombo_sedeText(String value){combo_sedeText.add(value);}
    public void addcombo_codcarrValue(Integer value){combo_codcarrValue.add(value);}
    public void addcombo_codmencValue(Integer value){combo_codmencValue.add(value);}
    public void addcombo_codsedeValue(Integer value){combo_codsedeValue.add(value);}
    public void addcombo_codscarValue(Integer value){combo_codscarValue.add(value);}
    public void addcombo_paralValue(Object value){combo_paralValue.add(value);}
    public void addcombo_codClasifValue(Integer value){combo_codClasifValue.add(value);}
    public void addcombo_fechExam(String value){combo_fechExam.add(value);}
    public void addcombo_fechReso(String value){combo_fechReso.add(value);}
    public void addcombo_anoIngresoValue(Integer value){combo_anoIngresoValue.add(value);}
    public void setGet_parameters(String value){get_parameters = value;}

    public void setPeriodos(List<CurricularPeriod> lista){
        periodos = lista;
    }
    public String getCarrera(int index){
        if (carreras.size() < index){
            return "undefined";
        }else
            return carreras.get(index);

    }
    public String getCombo_planText(int index){
        if (combo_planText.size() < index){
            return "undefined";
        }else{
            return combo_planText.get(index);
        }
    }
    public String getCombo_planValue(int index){
        if (combo_planValue.size() < index){
            return "undefined";
        }else{
            return combo_planValue.get(index);
        }
    }
    public String getCombo_rolText(int index){
        if (combo_rolText.size() < index){
            return "undefined";
        }else{
            return combo_rolText.get(index);
        }
    }
    public String getCombo_cursoText(int index){
        if (combo_cursoText.size() < index+1){
            return "undefined";
        }else{
            return combo_cursoText.get(index);
        }
    }
    public String getCombo_carreraText(int index){
        if (combo_carreraText.size() < index){
            return "undefined";
        }else{
            return combo_carreraText.get(index);
        }
    }
    public String getCombo_sedeText(int index){
        if (combo_sedeText.size() < index){
            return "undefined";
        }else{
            return combo_sedeText.get(index);
        }
    }
    public String getCombo_codcarrValue(int index){
        if (combo_codcarrValue.size() < index){
            return "undefined";
        }else{
            return combo_codcarrValue.get(index).toString();
        }
    }
    public String getCombo_codmencValue(int index){
        if (combo_codmencValue.size() < index){
            return "undefined";
        }else{
            return combo_codmencValue.get(index).toString();
        }
    }
    public String getCombo_codsedeValue(int index){
        if (combo_codsedeValue.size() < index){
            return "undefined";
        }else{
            return combo_codsedeValue.get(index).toString();
        }
    }
    public String getCombo_codscarValue(int index){
        if (combo_codscarValue.size() < index){
            return "undefined";
        }else{
            return combo_codscarValue.get(index).toString();
        }
    }
    public String getCombo_paralValue(int index){
        if (combo_paralValue.size() < index){
            return "undefined";
        }else{
            return combo_paralValue.get(index).toString();
        }
    }
    public String getCombo_codClasifValue(int index){
        if (combo_codClasifValue.size() < index){
            return "undefined";
        }else{
            return combo_codClasifValue.get(index).toString();
        }
    }
    public String getCombo_fechExam(int index){
        if (combo_fechExam.size() < index){
            return "undefined";
        }else{
            return combo_fechExam.get(index);
        }
    }
    public String getCombo_fechReso(int index){
        if (combo_fechReso.size() < index){
            return "undefined";
        }else{
            return combo_fechReso.get(index);
        }
    }
    public String getCombo_anoIngresoValue(int index){
        if (combo_anoIngresoValue.size() < index){
            return "undefined";
        }else{
            return combo_anoIngresoValue.get(index).toString();
        }
    }
    public String getParameters(){
        return get_parameters;
    }
}
