package lr.maisvendas.servico;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Ronaldo on 10/08/2017.
 */
public class VerificaConexao {

    public boolean isNetworkAvailable(Context context) {

        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        boolean teste = activeNetworkInfo != null;

        return activeNetworkInfo != null;
    }
}
