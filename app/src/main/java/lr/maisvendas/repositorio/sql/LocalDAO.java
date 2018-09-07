package lr.maisvendas.repositorio.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import lr.maisvendas.repositorio.DatabaseHelper;

public class LocalDAO {
	
    private Context context;
	
    //private LocalAdap localAdap;
    private static final String LOCAL_TABLE_NAME = "tlocals";

    public static final String LOCAL_TABLE_CREATE = "CREATE TABLE if not exists " + LOCAL_TABLE_NAME + " (" +
            "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "LATITUDE BLOB NOT NULL, " +
            "LONGITUDE BLOB NOT NULL, " +
            "CLIENTE_ID INTEGER," +
            "DT_CRIACAO TEXT, " +
            "DT_ATUALIZACAO TEXT);";

    public static final String SCRIPT_DELECAO_TABELA =  "DROP TABLE IF EXISTS " + LOCAL_TABLE_NAME;

    private SQLiteDatabase dataBase = null;

    private static LocalDAO instance;

    public static LocalDAO getInstance(Context context) {
        if(instance == null)
            instance = new LocalDAO(context);
        return instance;
    }

    private LocalDAO(Context context) {
        this.context = context;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        dataBase = databaseHelper.getWritableDatabase();
        //localAdap = new LocalAdap();
    }

    //CRUD

    public void truncateLocals(){
        dataBase.delete(LOCAL_TABLE_NAME,null,null);
    }

    public void fecharConexao() {
        if(dataBase != null && dataBase.isOpen()) {
            dataBase.close();
        }
    }

}
