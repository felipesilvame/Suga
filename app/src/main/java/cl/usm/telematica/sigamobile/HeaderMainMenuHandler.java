package cl.usm.telematica.sigamobile;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;
import static cl.usm.telematica.sigamobile.MainMenuActivity.msCookieManager;

/**
 * Created by Pipos on 16-10-2016.
 */

public class HeaderMainMenuHandler extends AsyncTask<Void, Void, Boolean> {
    private MainMenuActivity parent;
    private StringBuffer response = new StringBuffer();
    private Connection con = null;
    private Document doc = null;
    private Connection menu = null;
    private Document doc2 = null;

    public HeaderMainMenuHandler(MainMenuActivity _navHeader){
        parent = _navHeader;
    }
    @Override
    protected void onPreExecute() {
        //Log.i(TAG, "onPreExecute");
        Log.i(TAG, "trying to connect...");
        con = Jsoup.connect("https://siga.usm.cl/pag/cabecera.jsp")
                .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:21.0) Gecko/20100101 Firefox/21.0")
                .header("Cookie", TextUtils.join(";",  msCookieManager.getCookieStore().getCookies()));
        menu = Jsoup.connect("https://siga.usm.cl/pag/menu.jsp")
                .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:21.0) Gecko/20100101 Firefox/21.0")
                .header("Cookie", TextUtils.join(";",  msCookieManager.getCookieStore().getCookies()));
        Log.i(TAG, "CONNECTED!!!");

    }
    @Override
    protected Boolean doInBackground(Void... params) {
        try {
            if (con !=null){
                doc = con.get();
                doc2 = menu.get();
                return true;
            }else return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    public void onPostExecute(Boolean success){
        if (success) {
            try{
                Elements ee = doc.select(".Encabezado01");
                List<String> listado = new ArrayList<>();
                for (Element e: ee){
                    if (e.tagName().equals("table")){
                        Element nombre = e.select(".Arial").first();
                        Element campus = e.select(".letra8").first();
                        Element alumno = e.select(".blanca10").first();

                        print(nombre.text());
                        print(campus.text());
                        print(alumno.text());

                        listado.add(nombre.text());
                        listado.add(campus.text());
                        listado.add(alumno.text());
                    }
                }
            //print(listado.toString());
                TextView nombre_alumno = (TextView) parent.findViewById(R.id.nombre_usuario);
                nombre_alumno.setText(listado.get(0).trim());

                TextView nombre_campus = (TextView) parent.findViewById(R.id.campus);
                nombre_campus.setText(listado.get(1).trim());

                TextView tipo_alumno = (TextView) parent.findViewById(R.id.tipo_alumno);
                tipo_alumno.setText(listado.get(2).trim());

                Element form_resumen = doc2.select("form[name=form_resumen]").first();
                Elements inputs = form_resumen.select("input");
                for (Element e:inputs){
                    if (e.attr("name").equals("tipo")){
                        parent.setTipo(e.attr("value"));
                    }else if (e.attr("name").equals("rutAlumno")){
                        parent.setRutAlumno(e.attr("value"));
                    }
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    private static void print(String msg, Object... args) {
        System.out.println(String.format(msg, args));
    }

    private static String trim(String s, int width) {
        if (s.length() > width)
            return s.substring(0, width-1) + ".";
        else
            return s;
    }
}
