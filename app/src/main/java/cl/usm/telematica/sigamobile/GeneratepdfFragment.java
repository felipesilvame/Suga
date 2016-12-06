package cl.usm.telematica.sigamobile;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class GeneratepdfFragment extends Fragment {
    List<PdfRequest> pdfRequestList;
    List<String> spinnerOptions;
    String rutAlumno;
    String tipo;
    View vista;
    Spinner spinner;
    ArrayAdapter<String> dataAdapter;
    Button generatePdfButton;
    LinearLayout linearLayout;
    int selectedPosition;

    public GeneratepdfFragment() {} //required empty constructor
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            rutAlumno = getArguments().getString("rutAlumno");
            tipo = getArguments().getString("tipo");
        }
    }
    public static GeneratepdfFragment newInstance(String rutAlumno, String tipo) {
        GeneratepdfFragment myFragment = new GeneratepdfFragment();

        Bundle args = new Bundle();
        args.putString("rutAlumno", rutAlumno);
        args.putString("tipo", tipo);
        myFragment.setArguments(args);
        return myFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista = inflater.inflate(R.layout.fragment_generatepdf, container, false);
        linearLayout = (LinearLayout) vista.findViewById(R.id.linearLayoutPDF);
        spinner = (Spinner) vista.findViewById(R.id.pdfSpinner);
        final GeneratepdfHandler mHandler = new GeneratepdfHandler(this);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedPosition = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        pdfRequestList = new ArrayList<>();
        generatePdfButton = (Button) vista.findViewById(R.id.generatePdfButton);
        disableButton();
        generatePdfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (generatePdfButton.isEnabled()){
                    mHandler.generatePDF(selectedPosition);
                }
            }
        });

        mHandler.getData();


        return vista;
    }

    public void populateData(){
        if (pdfRequestList != null){
            spinnerOptions = new ArrayList<>();
            for (PdfRequest value :pdfRequestList){
                spinnerOptions.add(value.getDescription());
            }
            dataAdapter = new ArrayAdapter<String>(getContext() , android.R.layout.simple_spinner_item, spinnerOptions);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(dataAdapter);
            dataAdapter.notifyDataSetChanged();
            enableButton();
        }
    }
    public void enableButton(){
        generatePdfButton.setEnabled(true);
    }

    public void disableButton(){
        generatePdfButton.setEnabled(false);

    }

    public String getRutAlumno(){
        return rutAlumno;
    }

    public String getTipo(){
        return tipo;
    }

    public void setPdfRequestList(List<PdfRequest> pdfRequestList){
        this.pdfRequestList = pdfRequestList;
    }
    public void setRutAlumno(String rutAlumno){
        this.rutAlumno = rutAlumno;
    }
    public void setTipo(String tipo){
        this.tipo = tipo;
    }
}
