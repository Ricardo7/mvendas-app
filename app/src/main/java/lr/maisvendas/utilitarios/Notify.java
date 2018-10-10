package lr.maisvendas.utilitarios;

import android.app.NotificationChannel;
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
    private static Integer progress;

    public Notify(Context context) {
        this.ferramentas = new Ferramentas();
        this.context = context;
        progress = 0;
    }

    public void iniciaNotificacao(String titulo, String textoInicio){
        mNotifyManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        //mBuilder = new NotificationCompat.Builder(context);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel notificationChannel = new NotificationChannel("ID", "Name", importance);
            notificationChannel.enableVibration(false);
            notificationChannel.setSound(null, null);
            mNotifyManager.createNotificationChannel(notificationChannel);
            mBuilder = new NotificationCompat.Builder(context, notificationChannel.getId());
        } else {
            mBuilder = new NotificationCompat.Builder(context);
        }

        mBuilder.setContentTitle(titulo)
                .setContentText(textoInicio)
                .setSound(null)
                .setSmallIcon(R.drawable.ic_maisvendas)
        ;

        mBuilder.setProgress(100, progress, false);
        // Displays the progress bar for the first time.
        mNotifyManager.notify(notificacaoId, mBuilder.build());
    }

    public void setProgress(int maximo, int progresso, boolean indeterminado){
        progress = progress + progresso;

        if(progress >= 100){
            //setProgress(100, 100, false);
            finalizaNotificacao("Sincronização concluída");
        }
        mBuilder.setSound(null);
        mBuilder.setProgress(maximo,progress,indeterminado);
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
