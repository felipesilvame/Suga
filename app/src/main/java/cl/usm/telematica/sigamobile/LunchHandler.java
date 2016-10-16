package cl.usm.telematica.sigamobile;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;
import static cl.usm.telematica.sigamobile.MainMenuActivity.msCookieManager;

/**
 * Created by Pipos on 15-10-2016.
 */

public class LunchHandler extends AsyncTask<LinearLayout, Void, Boolean> {
    private StringBuffer response = new StringBuffer();
    Connection con = null;
    Document doc = null;
    LinearLayout frameLayout= null;
    ViewGroup mContainer;

    public LunchHandler(ViewGroup container){
        mContainer = container;
    }
    @Override
    protected void onPreExecute() {
        //Log.i(TAG, "onPreExecute");
        Log.i(TAG, "trying to connect...");
        con = Jsoup.connect("https://siga.usm.cl/pag/sistinsc/insc_consultabecaydeuda.jsp?m=21")
                .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:21.0) Gecko/20100101 Firefox/21.0")
                .header("Cookie", TextUtils.join(";",  msCookieManager.getCookieStore().getCookies()));
        Log.i(TAG, "CONNECTED!!!");

    }

    @Override
    protected Boolean doInBackground(LinearLayout... params) {
        frameLayout = params[0];
        try {
            if (con !=null){
                doc = con.get();

            }
//            URL lunch_url = new URL("https://siga.usm.cl/pag/sistinsc/insc_consultabecaydeuda.jsp?m=21");
//            HttpURLConnection urlConnection = (HttpURLConnection) lunch_url.openConnection();
//            urlConnection.setRequestMethod("GET");
//            urlConnection.setRequestProperty("Host","siga.usm.cl");
//            //urlConnection.setRequestProperty("Referer","https://siga.usm.cl/pag/sistinsc/insc_ficha_frame2.jsp");
//            urlConnection.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/53.0.2785.143 Safari/537.36");
//            urlConnection.setDoInput(true);
//            urlConnection.setUseCaches(true);
//            urlConnection.setInstanceFollowRedirects(true);
//            if (msCookieManager.getCookieStore().getCookies().size() > 0) {
//                // While joining the Cookies, use ',' or ';' as needed. Most of the servers are using ';'
//                urlConnection.setRequestProperty("Cookie",
//                        TextUtils.join(";",  msCookieManager.getCookieStore().getCookies()));
//            }
//            urlConnection.connect();
//            InputStreamReader in = new InputStreamReader((InputStream) urlConnection.getContent());
//            BufferedReader buff = new BufferedReader(in);
//            String line;
//            do {
//                line = buff.readLine();
//                response.append(line);
//            } while (line != null);

        } catch (MalformedURLException e) {
            e.printStackTrace();
            return false;
        } catch (ProtocolException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void onPostExecute(Boolean success){
        if (success) {
            //String respuesta = response.toString();
            //Document codigo_html = Jsoup.parse(respuesta);
            Elements letra10 = doc.select(".letra10");
            Elements letra8 = doc.select(".letra8");
            Elements celda01 = doc.select(".Celda01");
            List<String> general = new ArrayList<>();
            List<String> info_alumno = new ArrayList<>();
            List<String> detalle = new ArrayList<>();

            print("\nletra10: (%d)", letra10.size());
            for (Element src : letra10) {
                if (src.tagName().equals("td")) {
                    print(" * %s: (%s)",
                            src.tagName(),
                            src.text());
                    general.add(src.text());
                } else if (src.tagName().equals("span")) {
                    //texto: BECA DE ALIMENTACION
                    TextView text_1 = new TextView(mContainer.getContext());
                    text_1.setText(src.text());
                    text_1.setGravity(Gravity.CENTER_HORIZONTAL);
                    frameLayout.addView(text_1);
                    print(" * %s: <%s>", src.tagName(), src.text());
                }
            }

            print("\nletra8: (%d)", letra8.size());
            for (Element link : letra8) {
                print(" * %s: (%s)", link.tagName(), link.text());
                info_alumno.add(link.text());
            }

            print("\ncelda01: (%d)", celda01.size());
            for (Element link : celda01) {
                print(" * a: (%s)", trim(link.text(), 35));
                detalle.add(link.text());
            }
            //mostrar la info del alumno
            //rut
            TextView rut_alumno = new TextView(mContainer.getContext());
            rut_alumno.setText(info_alumno.get(0).toString() +
                    info_alumno.get(1).toString() +
                    info_alumno.get(2).toString());
            rut_alumno.setGravity(Gravity.LEFT);
            frameLayout.addView(rut_alumno);
            //nombre del alumno
            TextView nombre_alumno = new TextView(mContainer.getContext());
            nombre_alumno.setText(info_alumno.get(3).toString() +
                    info_alumno.get(4).toString() +
                    info_alumno.get(5).toString());
            nombre_alumno.setGravity(Gravity.LEFT);
            frameLayout.addView(nombre_alumno);
            //texto: total almuerzos
            TextView total_almuerzos = new TextView(mContainer.getContext());
            total_almuerzos.setText(general.get(1).toString() +
                    general.get(2).toString() +
                    general.get(3).toString());
            total_almuerzos.setGravity(Gravity.LEFT);
            frameLayout.addView(total_almuerzos);
            //texto: utilizados
            TextView utilizados_almuerzos = new TextView(mContainer.getContext());
            utilizados_almuerzos.setText(general.get(4).toString() +
                    general.get(5).toString());
            utilizados_almuerzos.setGravity(Gravity.LEFT);
            frameLayout.addView(utilizados_almuerzos);
            //texto: disponibles
            TextView disponibles_almuerzos = new TextView(mContainer.getContext());
            disponibles_almuerzos.setText(general.get(6).toString() +
                    general.get(7).toString());
            disponibles_almuerzos.setGravity(Gravity.LEFT);
            frameLayout.addView(disponibles_almuerzos);
            //texto: total cena
            TextView total_cena = new TextView(mContainer.getContext());
            total_cena.setText(general.get(8).toString() +
                    general.get(9).toString() +
                    general.get(10).toString());
            total_cena.setGravity(Gravity.LEFT);
            frameLayout.addView(total_cena);
            //texto: utilizados cena
            TextView utilizados_cena = new TextView(mContainer.getContext());
            utilizados_cena.setText(general.get(11).toString() +
                    general.get(12).toString());
            utilizados_cena.setGravity(Gravity.LEFT);
            frameLayout.addView(utilizados_cena);
            //texto: disponibles cena
            TextView disponibles_cena = new TextView(mContainer.getContext());
            disponibles_cena.setText(general.get(13).toString() +
                    general.get(14).toString());
            disponibles_cena.setGravity(Gravity.LEFT);
            frameLayout.addView(disponibles_cena);
            //detalle de cada almuerzo
            TextView detalle_almuerzo = new TextView(mContainer.getContext());
            detalle_almuerzo.setText("Detalle Almuerzos");
            detalle_almuerzo.setGravity(Gravity.CENTER_HORIZONTAL);
            frameLayout.addView(detalle_almuerzo);
            frameLayout.setOrientation(LinearLayout.VERTICAL);
            TableLayout tableLayout = new TableLayout(mContainer.getContext());

            TableLayout.LayoutParams tableParams =
                    new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
                            TableLayout.LayoutParams.MATCH_PARENT);
            TableRow.LayoutParams rowParams =
                    new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, 0, 1f);
            TableRow.LayoutParams itemParams =
                    new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                            TableRow.LayoutParams.MATCH_PARENT, 1f);
            tableLayout.setLayoutParams(tableParams);
            //Texto: fecha
            TextView texto_fecha = new TextView(mContainer.getContext());
            texto_fecha.setText("Fecha");
            texto_fecha.setLayoutParams(itemParams);
            texto_fecha.setGravity(Gravity.CENTER_HORIZONTAL);
            //Texto: Hora
            TextView texto_hora = new TextView(mContainer.getContext());
            texto_hora.setText("Hora");
            texto_hora.setLayoutParams(itemParams);
            texto_hora.setGravity(Gravity.CENTER_HORIZONTAL);

            TableRow row= new TableRow(mContainer.getContext());
            row.setLayoutParams(rowParams);

            row.addView(texto_fecha);
            row.addView(texto_hora);

            tableLayout.addView(row);
            //bonito ciclo for :D
            for (int i = 0; i < detalle.size(); i++) {
                //Texto: fecha
                texto_fecha = new TextView(mContainer.getContext());
                texto_fecha.setText(detalle.get(i));
                texto_fecha.setLayoutParams(itemParams);
                texto_fecha.setGravity(Gravity.CENTER_HORIZONTAL);
                i++;
                //Texto: Hora
                texto_hora = new TextView(mContainer.getContext());
                texto_hora.setText(detalle.get(i));
                texto_hora.setLayoutParams(itemParams);
                texto_hora.setGravity(Gravity.CENTER_HORIZONTAL);

                row= new TableRow(mContainer.getContext());
                row.setLayoutParams(rowParams);

                row.addView(texto_fecha);
                row.addView(texto_hora);
                tableLayout.addView(row);

            }


            frameLayout.addView(tableLayout);


            //layout2.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

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


