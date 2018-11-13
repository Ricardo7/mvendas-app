package lr.maisvendas.utilitarios;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

public class PermissoesAndroid {

    public static final int STORAGE_PERMISSION = 0;


    /**
     * Verifica se a versão do Android é acima do 5.1 (Lollipop API level 22), se for é necessário
     * solicitar a permissão em tempo de execução.
     *
     * @return
     */
    private static boolean needToAskPermission() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    public static boolean hasStoragePermission(Context context) {
        if (needToAskPermission()) {
            return ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }

    public static void asksStoragePermission(final Activity activity) {
        if(ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CAMERA)) {
            new AlertDialog.Builder(activity).setMessage( "Precisa acessar o Storage")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            ActivityCompat.requestPermissions(activity,
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PermissoesAndroid.STORAGE_PERMISSION);
                        }
                    }).show();
        } else {
            ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},PermissoesAndroid.STORAGE_PERMISSION);
        }
    }

}
