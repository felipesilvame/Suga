package cl.usm.telematica.sigamobile;


import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.Scriptable;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.ContentValues.TAG;
import static cl.usm.telematica.sigamobile.MainMenuActivity.msCookieManager;

/**
 * Created by Pipos on 03-12-2016.
 */

public class ProgressHandler extends AsyncTask<RelativeLayout, Void, Boolean> {
    Connection con = null;
    Document doc = null;
    RelativeLayout frameLayout= null;
    ViewGroup mContainer;
    ProgressFragment mFragment;


    public ProgressHandler(ViewGroup container, ProgressFragment mFragment){
        this.mFragment = mFragment;
        mContainer = container;
    }

    @Override
    protected void onPreExecute() {
        Log.i(TAG, "trying to connect...");
        con = Jsoup.connect("https://siga.usm.cl/pag/sistinsc/insc_consultainscalum_frameset.jsp")
                .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:21.0) Gecko/20100101 Firefox/21.0")
                .header("Cookie", TextUtils.join(";",  msCookieManager.getCookieStore().getCookies()))
                .data("v", "0")
                .data("menu", "0")
                .data("opcion", "0")
                .data("m", "16")
                .data("listado", "");
        Log.i(TAG, "CONNECTED!!!");
    }

    @Override
    protected Boolean doInBackground(RelativeLayout... params) {
        try {
            if (con !=null){
                doc = con.post();
                String titulo = doc.select("title").first().text();
                if (titulo.equals("Consulta de asignaturas")){
                    //traer la fotito
                    try {
                        con = Jsoup.connect("https://siga.usm.cl/pag/sistinsc/insc_consultainscalum_frame2.jsp?m=16")
                                .header("Cookie", TextUtils.join(";",  msCookieManager.getCookieStore().getCookies()));
                        doc = con.get();
                        Elements javascript = doc.select("script");
                        Element asdf = javascript.first();
                        Elements carreras = doc.select("select option");
                        for (Element e: carreras){
                            mFragment.addcarrera(e.text());
                        }
                        String javaScriptCode = asdf.html();
                        Context rhino = Context.enter();

                        // Turn off optimization to make Rhino Android compatible
                        rhino.setOptimizationLevel(-1);
                        try {
                            int i;
                            Scriptable scope = rhino.initStandardObjects();

                            // Note the forth argument is 1, which means the JavaScript source has
                            // been compressed to only one line using something like YUI
                            rhino.evaluateString(scope, javaScriptCode, "JavaScript", 1, null);

                            // Get the functionName defined in JavaScriptCode
                             NativeArray obj = (NativeArray) scope.get("combo_planText", scope);
                            for (Object e : obj){
                                mFragment.addcombo_planText(e.toString());
                            }
                            obj = (NativeArray) scope.get("combo_planValue", scope);
                            for (Object e : obj){
                                mFragment.addcombo_planValue(e.toString());
                            }
                            obj = (NativeArray) scope.get("combo_rolText", scope);
                            for (Object e:obj){
                                mFragment.addcombo_rolText(e.toString());
                            }
                            obj = (NativeArray) scope.get("combo_cursoText", scope);
                            for (Object e:obj){
                                mFragment.addcombo_cursoText(e.toString());
                            }
                            obj = (NativeArray) scope.get("combo_carreraText", scope);
                            for (Object e:obj){
                                mFragment.addcombo_carreraText(e.toString());
                            }
                            obj = (NativeArray) scope.get("combo_sedeText", scope);
                            for (Object e:obj){
                                mFragment.addcombo_sedeText(e.toString());
                            }
                            obj = (NativeArray) scope.get("combo_codcarrValue",scope);
                            for (Object e:obj){
                                mFragment.addcombo_codcarrValue((int)Double.parseDouble(e.toString()));
                            }
                            obj = (NativeArray) scope.get("combo_codmencValue",scope);
                            for (Object e:obj){
                                mFragment.addcombo_codmencValue((int)Double.parseDouble(e.toString()));
                            }
                            obj = (NativeArray) scope.get("combo_codsedeValue",scope);
                            for (Object e:obj){
                                mFragment.addcombo_codsedeValue((int)Double.parseDouble(e.toString()));
                            }
                            obj = (NativeArray) scope.get("combo_codscarValue",scope);
                            for (Object e:obj){
                                mFragment.addcombo_codscarValue((int)Double.parseDouble(e.toString()));
                            }
                            obj = (NativeArray) scope.get("combo_paralValue",scope);
                            for (Object e:obj){
                                mFragment.addcombo_paralValue(e);
                            }
                            obj = (NativeArray) scope.get("combo_codClasifValue",scope);
                            for (Object e:obj){
                                mFragment.addcombo_codClasifValue((int)Double.parseDouble(e.toString()));
                            }
                            obj = (NativeArray) scope.get("combo_fechExamValue", scope);
                            for (Object e:obj){
                                mFragment.addcombo_fechExam(e.toString());
                            }
                            obj = (NativeArray) scope.get("combo_fechResoValue", scope);
                            for (Object e:obj){
                                mFragment.addcombo_fechReso(e.toString());
                            }
                            obj = (NativeArray) scope.get("combo_añoIngresoValue",scope);
                            for (Object e:obj){
                                mFragment.addcombo_anoIngresoValue((int)Double.parseDouble(e.toString()));
                            }
                        } finally {
                            Context.exit();
                        }
                        Pattern pattern = Pattern.compile("insc_avancecurricular\\.jsp\\?(.*?)\"");
                        Matcher matcher = pattern.matcher(javaScriptCode);
                        while (matcher.find()) {
                            mFragment.setGet_parameters(matcher.group(1));
                        }
                        return true;

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
        if (success) {
            mFragment.changeDataSetCarreras();
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
