package lr.maisvendas.utilitarios;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import lr.maisvendas.R;

/**
 * Created by Ronaldo on 01/03/2018.
 */

public class Notify {

    private Ferramentas ferramentas;
    private static final String TAG = "Notify";
    private static Context context;
    private static NotificationManager mNotifyManager;
    private static NotificationCompat.Builder mBuilder;
    private static final int notificacaoId = 1;

    public Notify(Context context) {
        this.ferramentas = new Ferramentas();
        this.context = context;

    }

    public void iniciaNotificacao(String titulo, String textoInicio){
        mNotifyManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(context);

        mBuilder.setContentTitle(titulo)
                .setContentText(textoInicio)
                .setSmallIcon(R.drawable.ic_maisvendas)
        ;

        mBuilder.setProgress(100, 1, false);
        // Displays the progress bar for the first time.
        mNotifyManager.notify(notificacaoId, mBuilder.build());
    }

    public void setProgress(int maximo, int progresso, boolean indeterminado){
        mBuilder.setProgress(maximo,progresso,indeterminado);
        mNotifyManager.notify(notificacaoId, mBuilder.build());
    }

    public void setContentText(String texto){
        mBuilder.setContentText(texto);
        mNotifyManager.notify(notificacaoId, mBuilder.build());
    }

    public void finalizaNotificacao(String texto){
        mBuilder.setContentText(texto)
                .setProgress(0, 0, false);

        mNotifyManager.notify(notificacaoId, mBuilder.build());
    }
}
