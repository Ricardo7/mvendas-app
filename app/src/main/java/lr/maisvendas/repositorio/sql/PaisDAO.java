package lr.maisvendas.repositorio.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import lr.maisvendas.adaptadorModelo.PaisAdap;
import lr.maisvendas.modelo.Pais;
import lr.maisvendas.repositorio.DatabaseHelper;

public class PaisDAO {
	
    private Context context;
    //private PaisAdap paisAdap;
    private static final String PAIS_TABLE_NAME = "tpaises";

    public static final String PAIS_TABLE_CREATE = "CREATE TABLE if not exists " + PAIS_TABLE_NAME + " ("
            + "ID INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "DESCRICAO TEXT NOT NULL, "
            + "SIGLA TEXT NOT NULL);";

    public static final String SCRIPT_DELECAO_TABELA =  "DROP TABLE IF EXISTS " + PAIS_TABLE_NAME;

    private SQLiteDatabase dataBase = null;

    private static PaisDAO instance;

    public static PaisDAO getInstance(Context context) {
        if(instance == null)
            instance = new PaisDAO(context);
        return instance;
    }

    private PaisDAO(Context context) {
        this.context = context;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        dataBase = databaseHelper.getWritableDatabase();
        //paisAdap = new PaisAdap();
    }

    public Pais buscaPaisId(Integer paisId){
        Pais pais = null;

        //Busca o grupo
        String sql = "SELECT * FROM tpaises WHERE id = "+ paisId ;
        Cursor cursor = dataBase.rawQuery(sql, null);

        if (cursor != null && cursor.getCount() > 0 ){
            PaisAdap paisAdap = new PaisAdap();
            while(cursor.moveToNext()) {
                //Converte o cursor em um objeto
                pais = paisAdap.sqlToPais(cursor);
            }
            cursor.close();
        }

        return pais;
    }

    public Pais inserePais(Pais pais) {

        PaisAdap paisAdap = new PaisAdap();
        //Converte o objeto em um contetValue para inserir no banco
        ContentValues content = paisAdap.paisToContentValue(pais);
        //Insere o pais no banco
        Integer paisId = (int) dataBase.insert(PAIS_TABLE_NAME, null, content);
        /*
        if(loginId <= 0){
            throw new Exceptions("Não foi possível inserir o usuário");
        }
        */
        pais.setId(paisId);

        return pais;

    }

    public void truncatePais(){
        dataBase.delete(PAIS_TABLE_NAME,null,null);
    }

    public void fecharConexao() {
        if(dataBase != null && dataBase.isOpen()) {
            dataBase.close();
        }
    }

}
