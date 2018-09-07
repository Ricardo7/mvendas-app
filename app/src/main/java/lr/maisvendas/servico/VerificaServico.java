package lr.maisvendas.servico;

import android.os.AsyncTask;

import java.net.HttpURLConnection;
import java.net.URL;

import lr.maisvendas.utilitarios.Ferramentas;

/**
 * Created by Ronaldo on 03/04/2018.
 */

public class VerificaServico extends AsyncTask<String, Void , Boolean> {

    public static final String TAG = "VerificaServico";

    @Override
    protected Boolean doInBackground(String... params) {
        Ferramentas ferramentas =new Ferramentas();
        Boolean teste=false;
        try {
            HttpURLConnection urlc = (HttpURLConnection) new URL(params[0]).openConnection();
            urlc.setRequestProperty("User-Agent", "Android");
            urlc.setRequestProperty("Connection", "close");
            urlc.setConnectTimeout(5000);
            urlc.connect();
            int codigo = urlc.getResponseCode();
            int largura = urlc.getContentLength();

            if ( codigo == 200 && largura != 0) {
                teste = true;
            }else{
                ferramentas.customLog(TAG,"Servidor não encontrado, código ("+codigo+").");
                teste = false;
            }

        } catch (Exception ex) {
            ferramentas.customLog(TAG,"Erro ao verificar servidor("+ex.getMessage()+")");
            teste = false;
        }
        return teste;
    }
}
