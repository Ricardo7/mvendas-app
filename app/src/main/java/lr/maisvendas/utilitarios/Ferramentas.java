package lr.maisvendas.utilitarios;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import lr.maisvendas.R;

/**
 * Created by Ronaldo on 17/08/2017.
 */

public class Ferramentas {

    private static final String TAG = "Tools";

    public void customToast(Activity aplication, String message) {

        LayoutInflater li = aplication.getLayoutInflater();
        View v = li.inflate(R.layout.custom_toast, null);

        TextView txtMessage = (TextView)
                v.findViewById(R.id.custom_toast_text_message);
        txtMessage.setText(message);

        Toast toast = new Toast(aplication.getApplicationContext());
        toast.setView(v);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.show();
    }

    public void customLog(String tag, String msg) {
        //System.out.println(tag+" - "+msg);
        Log.v("APP_VALIDADES[" + getCurrentDate().toString() + "] " + tag, msg+"");
    }

    public String getCurrentDate() {
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dataAtual = new Date(System.currentTimeMillis());

        return sd.format(dataAtual);
    }

    public boolean isDateValid(String data) {
        // Configure o SimpleDateFormat no onCreate ou onCreateView
        String pattern = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);

        sdf.setLenient(false);

        try {
            Date date = sdf.parse(data);

            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    public String formatDateUser(String date){
        String pattern = "yyyy-MM-dd";
        String patternUser = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        SimpleDateFormat sdfUser = new SimpleDateFormat(patternUser);

        Date data = null;
        try {
            data = sdf.parse(date);
        } catch (ParseException e) {
            customLog(TAG,e.getMessage());
        }
        String str = "";
        if (data != null) {
            str = sdfUser.format(data);
        }

        return str;
    }

    public String formatDateBD(String date){
        if(date == null){
            return null;
        }
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        df.setLenient(false);
        Date d = null;
        try {
            d = df.parse(date);
        } catch (ParseException e) {
            customLog(TAG,e.getMessage());
        }
        String s = "";
        if (d != null) {
            df = new SimpleDateFormat("yyyy-MM-dd");
           s = df.format(d);
        }
        return s;
    }

    public String formatDatetimeBD(String date){
        if(date == null){
            return null;
        }
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        df.setLenient(false);
        Date d = null;
        try {
            d = df.parse(date);
        } catch (ParseException e) {
            customLog(TAG,e.getMessage());
        }
        df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String s = df.format(d);
        return s;
    }

    public String formatDatetimeUser(String date){
        String pattern = "yyyy-MM-dd HH:mm:ss";
        String patternUser = "dd/MM/yyyy HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        SimpleDateFormat sdfUser = new SimpleDateFormat(patternUser);

        Date data = null;
        try {
            data = sdf.parse(date);
        } catch (ParseException e) {
            customLog(TAG,e.getMessage());
        }
        String str = "";
        if (data != null) {
            str = sdfUser.format(data);
        }

        return str;
    }

    public Date stringToDate(String dateStr){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = new Date(sdf.parse(dateStr).getTime());
        } catch (ParseException ex) {
            customLog(TAG,ex.getMessage());
        }

        return date;
    }

    public String dataToString(Date indate)
    {
        String dateString = null;
        SimpleDateFormat sdfr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try{
            dateString = sdfr.format( indate );
        }catch (Exception ex ){
            customLog(TAG,ex.getMessage());
        }
        return dateString;
    }

    public String getMd5Hash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            String md5 = number.toString(16);

            while (md5.length() < 32)
                md5 = "0" + md5;

            return md5;

        } catch (Exception e) {
            Log.e("MD5", e.getMessage());
            return null;
        }
    }

    public String formatCnpjCpf(String valor, String tipoPessoa){

        if (tipoPessoa == TipoPessoa.fisica){
            String part1 = valor.substring(0, 2);
            String part2 = valor.substring(2, 5);
            String part3 = valor.substring(5, 8);
            String part4 = valor.substring(8, 12);

            /* build the masked phone number… */
            valor = part1 + "." + part2 + "." + part3 + "-" + part4;
        }else {
            String part1 = valor.substring(0, 2);
            String part2 = valor.substring(2, 5);
            String part3 = valor.substring(5, 8);
            String part4 = valor.substring(8, 12);
            String part5 = valor.substring(12, 14);

            /* build the masked phone number… */
             valor = part1 + "." + part2 + "." + part3 + "/" + part4 + "-" + part5;
        }

        return valor;
    }

}
