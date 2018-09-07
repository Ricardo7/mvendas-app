package lr.maisvendas.repositorio.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import lr.maisvendas.repositorio.DatabaseHelper;

public class AtividadeDAO {
	
    private Context context;
	
    //private AtividadeAdap atividadeAdap;
    private static final String ATIVIDADE_TABLE_NAME = "tatividades";

    public static final String ATIVIDADE_TABLE_CREATE = "CREATE TABLE if not exists " + ATIVIDADE_TABLE_NAME + " (" +
            "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "ASSUNTO TEXT NOT NULL, " +
            "CLIENTE_ID INTEGER NOT NULL, " +
            "OBSERVACAO TEXT," +
            "DATA_ATIVIDADE TEXT NOT NULL, " +
            "HORA_ATIVIDADE TEXT NOT NULL, " +
            "DT_CRIACAO TEXT, " +
            "DT_ATUALIZACAO TEXT);";

    public static final String SCRIPT_DELECAO_TABELA =  "DROP TABLE IF EXISTS " + ATIVIDADE_TABLE_NAME;

    private SQLiteDatabase dataBase = null;

    private static AtividadeDAO instance;

    public static AtividadeDAO getInstance(Context context) {
        if(instance == null)
            instance = new AtividadeDAO(context);
        return instance;
    }

    private AtividadeDAO(Context context) {
        this.context = context;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        dataBase = databaseHelper.getWritableDatabase();
        //atividadeAdap = new AtividadeAdap();
    }

    //CRUD

    public void truncateAtividades(){
        dataBase.delete(ATIVIDADE_TABLE_NAME,null,null);
    }

    public void fecharConexao() {
        if(dataBase != null && dataBase.isOpen()) {
            dataBase.close();
        }
    }

}
