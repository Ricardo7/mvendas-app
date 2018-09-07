package lr.maisvendas.adaptadorModelo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import lr.maisvendas.modelo.Notificacao;

public class NotificacaoAdap {

    private Context context;

    public void NotificacaoAdap(Context context){
        this.context = context;
    }

    public Notificacao sqlToNotificacao(Cursor cursor){
        Notificacao notificacao = new Notificacao();

        /*private Integer id;
    private String titulo;
    private String mensagem;
    private String dtSinc;*/

        return notificacao;
    }

    public ContentValues notificacaoToContentValue(Notificacao notificacao){
        ContentValues content = new ContentValues();

        /*"ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "TITULO TEXT, " +
            "MENSAGEM TEXT)";*/

        return content;
    }
}
