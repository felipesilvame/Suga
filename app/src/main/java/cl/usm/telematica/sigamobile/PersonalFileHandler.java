package cl.usm.telematica.sigamobile;


import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.util.RangeValueIterator;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;
import static cl.usm.telematica.sigamobile.MainMenuActivity.msCookieManager;

/**
 * Created by Pipos on 16-10-2016.
 */

public class PersonalFileHandler extends AsyncTask<LinearLayout, Void, Boolean> {
    Connection con = null;
    Document doc = null;
    LinearLayout frameLayout= null;
    ViewGroup mContainer;
    Elements encabezados;
    Elements datos_dentro;

    public PersonalFileHandler(ViewGroup container){
        mContainer = container;
    }

    @Override
    protected void onPreExecute(){
        Log.i(TAG, "trying to connect...");
        con = Jsoup.connect("https://siga.usm.cl/pag/sistinsc/insc_ficha_frameset.jsp")
                .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:21.0) Gecko/20100101 Firefox/21.0")
                .header("Cookie", TextUtils.join(";",  msCookieManager.getCookieStore().getCookies()))
                .data("v", "0")
                .data("menu", "0")
                .data("opcion", "0")
                .data("m", "0")
                .data("listado", "");
        Log.i(TAG, "CONNECTED!!!");
    }

    @Override
    protected Boolean doInBackground(LinearLayout... params) {
        frameLayout = params[0];
        try {
            if (con !=null){
                doc = con.post();
                String titulo = doc.select("title").first().text();
                if (titulo.equals("Ficha del Alumno")){
                    //traer la info del alumno
                    try {
                        con = Jsoup.connect("https://siga.usm.cl/pag/sistinsc/insc_ficha_frame2.jsp")
                                .header("Cookie", TextUtils.join(";",  msCookieManager.getCookieStore().getCookies()));
                        doc = con.get();
                        Element formulario = doc.select("form").first();
                        encabezados = formulario.select("table.Encabezado03");
                        datos_dentro = formulario.select("table.Celda02");
                        if (encabezados.size() == datos_dentro.size())
                            return true;
                        else
                            return false;
                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }
                }else
                    return false;
            }else return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void onPostExecute(Boolean success){

        TableLayout.LayoutParams tableParams =
                new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                        TableLayout.LayoutParams.WRAP_CONTENT);
        TableRow.LayoutParams rowParams =
                new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 0, 1f);
        TableRow.LayoutParams itemParams =
                new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT, 1f);
        if (success) {
            try{
                //detalle de cada encabezado
                //TAG: Table
                for (int i = 0; i < encabezados.size(); i++) {

                    TextView nombre_encabezado = new TextView(mContainer.getContext());
                    nombre_encabezado.setText(encabezados.get(i).text());
                    nombre_encabezado.setGravity(Gravity.CENTER_HORIZONTAL);
                    nombre_encabezado.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                            mContainer.getResources().getDimensionPixelSize(R.dimen.header_2 ));
                    frameLayout.addView(nombre_encabezado);
                    frameLayout.setOrientation(LinearLayout.VERTICAL);

                    TableLayout tableLayout = new TableLayout(mContainer.getContext());
                    tableLayout.setLayoutParams(tableParams);


                    //elementos dentro de cada encabezado
                    //Tag: tr
                    Elements datos = datos_dentro.get(i).select("tr");
                    for (int j = 0; j < datos.size(); j++) {
                        Element single_tr = datos.get(j);
                        //TODO: realizar la logica para separar los elementos de un tr
                        List<String> elementos = parseElements(single_tr);

                        //verificar que esto funcione
                        for (String e:elementos) {
                            TableRow row = new TableRow(mContainer.getContext());
                            row.setLayoutParams(rowParams);
                            TextView texto = new TextView(mContainer.getContext());
                            texto.setText(e);
                            texto.setLayoutParams(itemParams);
                            row.addView(texto);
                            tableLayout.addView(row);
                        }
                        //TAG: td
//                        for (int k = 0; k < td.size(); k++) {
//                            String imprimir_texto;
//                            Element inputt = td.get(k).select("input").first();
//                            if (inputt != null && inputt.attr("type").equals("text")){
//                                //print("elemento con input");
//                                imprimir_texto = inputt.attr("value");
//                            }else {
//                                imprimir_texto = td.get(k).text();
//                            }
//                            TextView texto = new TextView(mContainer.getContext());
//                            texto.setText(imprimir_texto);
//                            texto.setLayoutParams(itemParams);
//                            row.addView(texto);
//
//                        }


                    }
                    frameLayout.addView(tableLayout);
                }
            }
            catch (Exception e){
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
    private List<String> parseElements(Element elements){
        List<String> elementos_finales = new ArrayList<>();
        Elements single_td = elements.select("td");
        if (single_td.size()==2){
            //2 elementos, verificar la casilla del segundo
            Element nodo0 = single_td.get(0);
            Element nodo1 = single_td.get(1);
            //<input type=text value=...
            if (nodo1.select("input").first() != null &&
                    nodo1.select("input").first().attr("type").equals("text")){
                elementos_finales.add(nodo0.text()+": "+
                        nodo1.select("input").first().attr("value"));
            }else{
                elementos_finales.add(nodo0.text()+nodo1.text());
            }
        }
        else if (single_td.size()==3){
            //3 elementos, mostrar como un string
            elementos_finales.add(single_td.text());
        }else if(single_td.size()==4){
            //4 elementos, mostrar de 2 string
            Element nodo0 = single_td.get(0);
            Element nodo1 = single_td.get(1);
            Element nodo2 = single_td.get(2);
            Element nodo3 = single_td.get(3);
            elementos_finales.add(nodo0.text()+nodo1.text());
            elementos_finales.add(nodo2.text()+nodo3.text());
        }
        else if(single_td.size()==5){
            //5 elementos, mostrar 2 string
            Element nodo0 = single_td.get(0);
            Element nodo1 = single_td.get(1);
            Element nodo2 = single_td.get(2);
            Element nodo3 = single_td.get(3);
            Element nodo4 = single_td.get(4);
            //<input type='text'
            if (nodo1.select("input").first() !=null &&
                    nodo1.select("input").first().attr("type").equals("text")){
                elementos_finales.add(nodo0.text()+": "+nodo1.select("input").first().attr("value"));
            }else if (nodo1.select("select").size()==1){
                //<select option...
                elementos_finales.add(nodo0.text()+": "+
                        nodo1.select("select").first().select("option[selected]").text());
            }

            else{
                elementos_finales.add(nodo0.text()+nodo1.text());
            }
            //<select name=....
            if(nodo4.select("select").size()==1){
                elementos_finales.add(nodo2.text()+
                        nodo3.text()+" "+
                        nodo4.select("select").first().select("option[selected]").text());
            }//<input type=text value=...
            else if (nodo4.select("input").first() != null &&
                    nodo4.select("input").first().attr("type").equals("text")){
                elementos_finales.add(nodo2.text()+
                        nodo3.text()+
                        nodo4.select("input").first().attr("value"));
            }else{
                elementos_finales.add(nodo2.text()+
                        nodo3.text()+
                        nodo4.text());
            }
        }else if (single_td.size()==6){

        }
        return elementos_finales;
    }
}
