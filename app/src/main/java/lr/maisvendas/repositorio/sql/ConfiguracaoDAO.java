package lr.maisvendas.repositorio.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import lr.maisvendas.repositorio.DatabaseHelper;

public class ConfiguracaoDAO {
	
    private Context context;
    //private ConfiguracaoAdap configuracaoAdap;
    private static final String CONFIGURACAO_TABLE_NAME = "tconfiguracaos";

    public static final String CONFIGURACAO_TABLE_CREATE = "CREATE TABLE if not exists " + CONFIGURACAO_TABLE_NAME + " (" +
            "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "DOWNLOAD_IMG_WIFI INTEGER, " +
            "RASTREA_GPS INTEGER," +
            "SINC_IMG INTEGER," +
            "SINC_PEDIDOS INTEGER," +
            "SINC_CLIENTES INTEGER," +
            "SINC_PRODUTOS INTEGER)";

    public static final String SCRIPT_DELECAO_TABELA =  "DROP TABLE IF EXISTS " + CONFIGURACAO_TABLE_NAME;

    private SQLiteDatabase dataBase = null;

    private static ConfiguracaoDAO instance;

    public static ConfiguracaoDAO getInstance(Context context) {
        if(instance == null)
            instance = new ConfiguracaoDAO(context);
        return instance;
    }

    private ConfiguracaoDAO(Context context) {
        this.context = context;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        dataBase = databaseHelper.getWritableDatabase();
        //configuracaoAdap = new ConfiguracaoAdap();
    }

    //CRUD

    public void truncateConfiguracaos(){
        dataBase.delete(CONFIGURACAO_TABLE_NAME,null,null);
    }

    public void fecharConexao() {
        if(dataBase != null && dataBase.isOpen()) {
            dataBase.close();
        }
    }

}
