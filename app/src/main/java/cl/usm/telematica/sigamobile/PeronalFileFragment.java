package cl.usm.telematica.sigamobile;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


public class PeronalFileFragment extends Fragment {

    public PeronalFileFragment() {
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
        View vista = inflater.inflate(R.layout.fragment_peronal_file, container, false);
        LinearLayout linearLayout = (LinearLayout) vista.findViewById(R.id.LinearLayoutPersonalFile);
        new PersonalFileHandler(container).execute(linearLayout);
        return vista;
    }

}
