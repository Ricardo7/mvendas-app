package lr.maisvendas.broadcast;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

import lr.maisvendas.modelo.Usuario;
import lr.maisvendas.repositorio.sql.UsuarioDAO;
import lr.maisvendas.utilitarios.Ferramentas;

/**
 * Created by Ronaldo on 15/02/2018.
 */

public class Alarme extends BroadcastReceiver {

    private Calendar calendario;
    private static final String TAG = "Alarme";
    private PendingIntent pendingIntent;
    private Ferramentas ferramentas;

    @Override
    public void onReceive(Context context, Intent it) {
        ferramentas = new Ferramentas();
        boolean alarmeAtivo = (PendingIntent.getBroadcast(context, 0, new Intent("CHECAR_AGENDAMENTOS"), PendingIntent.FLAG_NO_CREATE))==null;

        UsuarioDAO usuarioDAO = UsuarioDAO.getInstance(context);
        Usuario usuario = usuarioDAO.buscaUsuarioLoginToken();

        if (alarmeAtivo){
            if (usuario != null) {
                ferramentas.customLog(TAG, "Agenda ja esta ativa... Será cancelada e reiniciada");
                cancel(context);
                start(context);
            }else{
                ferramentas.customLog(TAG, "Agenda ja esta ativa... Será cancelada pois o dispositivo não está logado");
                cancel(context);
            }
        }else if (alarmeAtivo == false && usuario != null){
            start(context);
        }
    }
    public void start(Context context){
        int horas = 1;
        int intervalo = horas*60*60*1000;
        // Getting current time
        calendario = Calendar.getInstance();
        //Calculo para definir a hora de inicio do alarm, o inicio deve ser multiplo de 5
        int minuto=calendario.get(Calendar.MINUTE);
        int esperar = 5 - (minuto % 5);
        calendario.set(calendario.get(Calendar.YEAR),calendario.get(Calendar.MONTH),calendario.get(Calendar.DAY_OF_MONTH),calendario.get(Calendar.HOUR_OF_DAY),calendario.get(Calendar.MINUTE),00);

        ferramentas.customLog(TAG,"Calendario original: "+calendario.getTime());

        // Creating Alarm Manager
        Intent intent = new Intent("CHECAR_AGENDAMENTOS");

        pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        //Seta o tempo de espera calculado anteriormente
        calendario.add(Calendar.MINUTE,esperar);
        long inicio = calendario.getTimeInMillis();
        ferramentas.customLog(TAG,"Iniciando Agenda em: "+calendario.getTime());
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, inicio, intervalo, pendingIntent);
        ferramentas.customLog(TAG,"Criou alarme!");

    }

    public void cancel(Context context) {
        try {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(pendingIntent);
            ferramentas.customLog(TAG, "Cancelou alarme!");
        }catch (Exception ex){
            ferramentas.customLog(TAG,ex.getMessage());
        }
    }
}
