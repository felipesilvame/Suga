package cl.usm.telematica.sigamobile;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.TextUtils;
import android.widget.Toast;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static cl.usm.telematica.sigamobile.MainMenuActivity.msCookieManager;

/**
 * Created by Pipos on 05-12-2016.
 */

public class GeneratepdfHandler {
    GeneratepdfFragment mFragment;
    List<PdfRequest> pdfRequestList;

    public GeneratepdfHandler(GeneratepdfFragment mFragment){
        pdfRequestList = new ArrayList<>();
        this.mFragment = mFragment;
    }

    public void getData(){
        try {
            if(new GeneratepdfHandler.GetListOfCarrers(mFragment.getTipo(), mFragment.getRutAlumno()).execute().get()) {
                mFragment.setPdfRequestList(pdfRequestList);
                mFragment.populateData();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    public void generatePDF(int index){
        Toast.makeText(mFragment.getContext(), "Descargando...", Toast.LENGTH_SHORT).show();
        try {
            if(new GeneratepdfHandler.GetPdf(index).execute().get()) {
                File pdfFile = new File(Environment.getExternalStorageDirectory() + "/Download/"
                        + "resumen_academico.pdf");
                Uri path = Uri.fromFile(pdfFile);
                Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
                pdfIntent.setDataAndType(path, "application/pdf");
                pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                try{
                    mFragment.startActivity(pdfIntent);
                }catch(ActivityNotFoundException e){
                    Toast.makeText(mFragment.getContext(), "No Application available to view PDF", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    public void addToList(PdfRequest element){
        pdfRequestList.add(element);
    }
    public List<PdfRequest> getPdfRequestList(){
        return pdfRequestList;
    }

    public void setPdfRequestList(List<PdfRequest> pdfRequestList){
        this.pdfRequestList = pdfRequestList;
    }

    private class GetListOfCarrers extends AsyncTask<Void, Void, Boolean>{
        private String plan;
        private String dv;
        private String mant;
        private String carrera;
        private String mencion;
        private String sedea;
        private String sedec;
        private String plan_alumno;
        private String description;
        private Connection con;
        private String tipo;
        private String rutAlumno;
        Document doc = null;

        private GetListOfCarrers(String tipo, String rutAlumno){
            this.tipo = tipo;
            this.rutAlumno = rutAlumno;
        }

        @Override
        protected void onPreExecute() {
            con = Jsoup.connect("https://siga.usm.cl/pag/sistmantalu/sma_resumenXAlumno.jsp")
                    .userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:21.0) Gecko/20100101 Firefox/21.0")
                    .header("Cookie", TextUtils.join(";",  msCookieManager.getCookieStore().getCookies()))
                    .data("tipo", tipo)
                    .data("rutAlumno", rutAlumno);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            if (con != null){
                try {
                    doc = con.post();
                    Elements info = doc.select("form[name=form1] input[type=hidden]");
                    for (Element element:info){
                        if (element.attr("name").equals("rut"))
                            rutAlumno = element.attr("value");
                        else if (element.attr("name").equals("dv"))
                            dv = element.attr("value");
                        else if (element.attr("name").equals("mant"))
                            mant = element.attr("value");
                    }
                    Elements carrer_info= doc.select("select[name=plan_alumno].Select option");
                    for (Element element: carrer_info){
                        description = element.text();
                        plan_alumno = element.attr("value");
                        String token[] = plan_alumno.split("/");
                        if (token.length == 5) {
                            plan = token[0];
                            carrera = token[1];
                            mencion = token[2];
                            sedea = token[3];
                            sedec = token[4];
                        }
                        PdfRequest temp = new PdfRequest(plan,rutAlumno,dv,mant,carrera,mencion,sedea,sedec,plan_alumno,"pdf",description);
                        addToList(temp);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            }
            return false;
        }

    }
    private class GetPdf extends AsyncTask<Void, Void, Boolean>{
        HttpURLConnection pdfConnection;
        int index;

        public GetPdf(int index){
            this.index = index;
        }
        @Override
        protected Boolean doInBackground(Void... voids) {
            PdfRequest temp = pdfRequestList.get(index);
            try{
                String extStorageDirectory = Environment.getExternalStorageDirectory()
                        .toString();
                File folder = new File(extStorageDirectory, "sigamobile");
                folder.mkdir();
                URL urlConnection = new URL("https://siga.usm.cl/pag/resumen");
                pdfConnection = (HttpURLConnection) urlConnection.openConnection();
                pdfConnection.setUseCaches( true );
                pdfConnection.setInstanceFollowRedirects(false);
                String urlParamameters = "";
                urlParamameters += "plan="+temp.getPlan();
                urlParamameters += "&rut="+temp.getRut();
                urlParamameters += "&dv="+temp.getDv();
                urlParamameters += "&mant="+temp.getMant();
                urlParamameters += "&carrera="+temp.getCarrera();
                urlParamameters += "&mencion="+temp.getMencion();
                urlParamameters += "&sedea="+temp.getSedea();
                urlParamameters += "&sedec="+temp.getSedec();
                urlParamameters += "&plan_alumno="+temp.getPlan_alumno();
                urlParamameters += "&tipo="+temp.getTipo();

                byte[] postData = urlParamameters.getBytes(StandardCharsets.UTF_8);
                int postDataLength = postData.length;
                pdfConnection.setDoOutput( true );
                pdfConnection.setRequestMethod( "POST" );
                pdfConnection.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded");
                pdfConnection.setRequestProperty( "charset", "utf-8");
                pdfConnection.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));
                pdfConnection.addRequestProperty("Cookie", TextUtils.join(";",  msCookieManager.getCookieStore().getCookies()));
                try( DataOutputStream wr = new DataOutputStream( pdfConnection.getOutputStream())) {
                    wr.write( postData );
                }
                FileOutputStream f = new FileOutputStream(new File(extStorageDirectory, "Download/resumen_academico.pdf"));


                InputStream in = pdfConnection.getInputStream();
                int totalSize = pdfConnection.getContentLength();

                byte[] buffer = new byte[1024];
                int len1 = 0;
                while ( (len1 = in.read(buffer)) > 0 ) {
                    f.write(buffer, 0, len1);
                }
                f.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return false;
            } catch (ProtocolException e) {
                e.printStackTrace();
                return false;
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return false;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }

    }
}
