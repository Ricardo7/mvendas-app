package lr.maisvendas.repositorio.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import lr.maisvendas.repositorio.DatabaseHelper;

public class NotificacaoDAO {
	
    private Context context;
    //private NotificacaoAdap notificacaoAdap;
    private static final String NOTIFICACAO_TABLE_NAME = "tnotificacoes";

    public static final String NOTIFICACAO_TABLE_CREATE = "CREATE TABLE if not exists " + NOTIFICACAO_TABLE_NAME + " (" +
            "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "TITULO TEXT, " +
            "MENSAGEM TEXT)";

    public static final String SCRIPT_DELECAO_TABELA =  "DROP TABLE IF EXISTS " + NOTIFICACAO_TABLE_NAME;

    private SQLiteDatabase dataBase = null;

    private static NotificacaoDAO instance;

    public static NotificacaoDAO getInstance(Context context) {
        if(instance == null)
            instance = new NotificacaoDAO(context);
        return instance;
    }

    private NotificacaoDAO(Context context) {
        this.context = context;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        dataBase = databaseHelper.getWritableDatabase();
        //notificacaoAdap = new NotificacaoAdap();
    }

    //CRUD

    public void truncateNotificacaos(){
        dataBase.delete(NOTIFICACAO_TABLE_NAME,null,null);
    }

    public void fecharConexao() {
        if(dataBase != null && dataBase.isOpen()) {
            dataBase.close();
        }
    }

}
