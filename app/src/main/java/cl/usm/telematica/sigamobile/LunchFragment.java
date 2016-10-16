package cl.usm.telematica.sigamobile;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;


/**
 * A simple {@link Fragment} subclass.
 */
public class LunchFragment extends Fragment {

    public LunchFragment() {
    }

    @Override
    public void onCreate(Bundle savedbundlestate){
        super.onCreate(savedbundlestate);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragment_lunch, container, false);
        LinearLayout linearLayout = (LinearLayout) vista.findViewById(R.id.frameLayoutLunch);
        new LunchHandler(container).execute(linearLayout);
        return vista;

    }

}
