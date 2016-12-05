package cl.usm.telematica.sigamobile;

import android.os.AsyncTask;
import android.text.TextUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static cl.usm.telematica.sigamobile.MainMenuActivity.msCookieManager;

/**
 * Created by Pipos on 04-12-2016.
 */

public class AvanceHandler {
    private HttpURLConnection urlConnection;
    List<CurricularPeriod> periodos;
    ProgressFragment mFragment;
    int index;

    public AvanceHandler(ProgressFragment mFragment){
        this.mFragment = mFragment;
        periodos = new ArrayList<>();
    }
    public boolean getPeriodSelected(int index){
        this.index = index;
        try {
            if(new GetPeriodsTask().execute().get())
                return true;
            else return false;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return false;
    }

    public class GetPeriodsTask extends AsyncTask<Void, Void, Boolean> {

        private String carrera;
        private String cod_plan;
        private String num_plan;
        private String rol;
        private String curso;
        private String carralu;
        private String sedealu;
        private String ano_ingreso;
        private String cod_carrera;
        private String cod_mencion;
        private String cod_sede_alu;
        private String cod_sede_car;
        private String paralelo_car;
        private String mCookie;
        private String params;
        private String location;

        GetPeriodsTask() {
            carrera = mFragment.getCombo_codcarrValue(index);
            cod_plan = mFragment.getCombo_planText(index);
            num_plan = mFragment.getCombo_planValue(index);
            rol = mFragment.getCombo_rolText(index);
            curso = mFragment.getCombo_cursoText(index);
            carralu = mFragment.getCombo_carreraText(index);
            sedealu = mFragment.getCombo_sedeText(index);
            ano_ingreso = mFragment.getCombo_anoIngresoValue(index);
            cod_carrera = mFragment.getCombo_codcarrValue(index);
            cod_mencion = mFragment.getCombo_codmencValue(index);
            cod_sede_alu = mFragment.getCombo_codsedeValue(index);
            cod_sede_car = mFragment.getCombo_codscarValue(index);
            paralelo_car = mFragment.getCombo_paralValue(index);
            params = mFragment.getParameters();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            String code="";
            try {
                URL login_url = new URL("https://siga.usm.cl/pag/sistinsc/insc_avancecurricular.jsp?"+this.params);
                urlConnection = (HttpURLConnection) login_url.openConnection();
                urlConnection.setUseCaches( true );
                urlConnection.setInstanceFollowRedirects(false);
                String urlParamameters = "";
                urlParamameters += "carrera="+carrera;
                urlParamameters += "&cod_plan="+cod_plan;
                urlParamameters += "&num_plan="+num_plan;
                urlParamameters += "&rol="+rol;
                urlParamameters += "&curso="+curso;
                urlParamameters += "&sedealu="+sedealu;
                urlParamameters += "&año_ingreso="+ano_ingreso;
                urlParamameters += "&cod_carrera="+cod_carrera;
                urlParamameters += "&cod_mencion="+cod_mencion;
                urlParamameters += "&cod_sede_alu="+cod_sede_alu;
                urlParamameters += "&cod_sede_car="+cod_sede_car;
                urlParamameters += "&paralelo_car="+paralelo_car;

                byte[] postData = urlParamameters.getBytes(StandardCharsets.UTF_8);
                int postDataLength = postData.length;
                urlConnection.setDoOutput( true );
                urlConnection.setRequestMethod( "POST" );
                urlConnection.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded");
                urlConnection.setRequestProperty( "charset", "utf-8");
                urlConnection.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));
                urlConnection.addRequestProperty("Cookie", TextUtils.join(";",  msCookieManager.getCookieStore().getCookies()));
                try( DataOutputStream wr = new DataOutputStream( urlConnection.getOutputStream())) {
                    wr.write( postData );
                }
                int response = urlConnection.getResponseCode();
                code = readStream(urlConnection.getInputStream());
                //TODO: make the html parsing
                List<CurricularPeriod> periodos = getPeriodos(code);
                mFragment.setPeriodos(periodos);
                urlConnection.disconnect();
                return true;
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            //urlConnection.disconnect();
            // TODO: register the new account here.
            return false;
        }

        @Override
        protected void onCancelled() {
            urlConnection.disconnect();
        }
        private String readStream(InputStream in) {
            BufferedReader reader = null;
            StringBuffer response = new StringBuffer();
            try {
                reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.ISO_8859_1));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return response.toString();
        }
    }
    public List<CurricularPeriod> getPeriodos(String code){
        List<CurricularPeriod> periodos = new ArrayList<>();
        List<Course> courses = new ArrayList<>();
        Document doc = Jsoup.parse(code, "ISO-8859-1");
        Elements celda6_letraazul = doc.select("table td.Celda06 td.letra8 .letraAzul");
        Elements allCourses = doc.select("table tbody tr td.Celda02");
        if (allCourses.size() % 8 == 0){
            int quantity = allCourses.size()/8;
            int i = 0;
            while(i < allCourses.size()){
                Course temp = new Course();
                temp.setInitial(allCourses.get(i).text().replaceAll("\\u00A0", "").trim());
                temp.setName(allCourses.get(i+1).text().trim());
                temp.setProfessor(allCourses.get(i+2).text().trim());
                temp.setParallel(allCourses.get(i+3).text().replaceAll("\\u00A0", "").trim());
                temp.setCredits(Integer.parseInt(allCourses.get(i+4).text().trim()));
                temp.setScore(allCourses.get(i+5).text().replaceAll("\\u00A0", "").trim());
                temp.setEvaluation(allCourses.get(i+6).text().replaceAll("\\u00A0", "").trim());
                temp.setStatus(allCourses.get(i+7).text().trim());
                courses.add(temp);
                i+=8;
            }
        }
        Elements info_cursos = doc.select("table tbody td.Celda06 td.letra8");
        if (info_cursos.size()%3 == 0){
            int quantity = info_cursos.size()/3;
            int i = 0;
            int offset = 0;
            while (i < info_cursos.size()){
                CurricularPeriod newPeriod = new CurricularPeriod();
                String nombrePeriodo = info_cursos.get(i).text();
                nombrePeriodo = nombrePeriodo.split(":")[1].replaceAll("\\u00A0", "").trim();
                String totalCreditos = info_cursos.get(i+1).text();
                totalCreditos = totalCreditos.split(":")[1].replaceAll("\\u00A0", "").trim();
                String asignaturas = info_cursos.get(i+2).text();
                asignaturas = asignaturas.split(":")[1].replaceAll("\\u00A0", "").trim();
                int cantAsignaturas = Integer.parseInt(asignaturas);
                newPeriod.setPeriod(nombrePeriodo);
                for (int k=0;k<cantAsignaturas;k++){
                    Course cursoTemp = courses.get(k+offset);
                    cursoTemp.setPeriod(nombrePeriodo);
                    newPeriod.addCourse(cursoTemp);
                }
                offset+=cantAsignaturas;
                i+=3;
                periodos.add(newPeriod);
            }
        }
        return periodos;
    }
}
