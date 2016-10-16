package cl.usm.telematica.sigamobile;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

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

import static android.content.ContentValues.TAG;
import static cl.usm.telematica.sigamobile.MainMenuActivity.msCookieManager;

/**
 * Created by Pipos on 16-10-2016.
 */

public class PhotoMainHandler extends AsyncTask<Void, Void, Boolean> {
    private Activity parent;
    private StringBuffer response = new StringBuffer();
    private Connection con = null;
    private Document doc = null;
    Bitmap foto;

    public PhotoMainHandler(Activity _parent){
        parent = _parent;
    }

    @Override
    protected void onPreExecute() {
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
    protected Boolean doInBackground(Void... params) {
        try {
            if (con !=null){
                doc = con.post();
                String titulo = doc.select("title").first().text();
                if (titulo.equals("Ficha del Alumno")){
                    //traer la fotito
                    try {
                        con = Jsoup.connect("https://siga.usm.cl/pag/sistinsc/insc_ficha_frame2.jsp")
                                .header("Cookie", TextUtils.join(";",  msCookieManager.getCookieStore().getCookies()));
                        doc = con.get();

                        URL url = new URL("https://siga.usm.cl/pag/foto");
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setDoInput(true);
                        connection.addRequestProperty("Cookie", TextUtils.join(";",  msCookieManager.getCookieStore().getCookies()));
                        connection.connect();
                        InputStream input = connection.getInputStream();
                        foto = BitmapFactory.decodeStream(input);
                        if (foto == null){
                            foto = BitmapFactory.decodeStream(new BufferedInputStream(new URL("https://siga.usm.cl/pag/imagen/sinfoto.jpg").openStream()));
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
            try{
                ImageView imagen = (ImageView)parent.findViewById(R.id.imageView);
                foto = Bitmap.createScaledBitmap(foto,100,100,true);
                imagen.setImageBitmap(foto);

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
}
