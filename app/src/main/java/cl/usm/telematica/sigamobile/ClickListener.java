package cl.usm.telematica.sigamobile;

import android.view.View;

/**
 * Created by Pipos on 30-09-2016.
 */
public interface ClickListener {
    void onClick(View view, int position);

    void onLongClick(View view, int position);
}
