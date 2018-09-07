package lr.maisvendas.repositorio.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import lr.maisvendas.adaptadorModelo.EstadoAdap;
import lr.maisvendas.modelo.Estado;
import lr.maisvendas.repositorio.DatabaseHelper;

public class EstadoDAO {
	
    private Context context;
    //private EstadoAdap estadoAdap;
    private static final String ESTADO_TABLE_NAME = "testados";

    public static final String ESTADO_TABLE_CREATE = "CREATE TABLE if not exists " + ESTADO_TABLE_NAME + " ("
            + "ID INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "DESCRICAO TEXT NOT NULL, "
            + "SIGLA TEXT NOT NULL, "
            + "PAIS_ID INTEGER NOT NULL);";

    public static final String SCRIPT_DELECAO_TABELA =  "DROP TABLE IF EXISTS " + ESTADO_TABLE_NAME;

    private SQLiteDatabase dataBase = null;

    private static EstadoDAO instance;

    public static EstadoDAO getInstance(Context context) {
        if(instance == null)
            instance = new EstadoDAO(context);
        return instance;
    }

    private EstadoDAO(Context context) {
        this.context = context;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        dataBase = databaseHelper.getWritableDatabase();
        //estadoAdap = new EstadoAdap();
    }

    public Estado buscaEstadoId(Integer estadoId){
        Estado estado = null;

        //Busca o grupo
        String sql = "SELECT * FROM testados WHERE id = "+ estadoId ;
        Cursor cursor = dataBase.rawQuery(sql, null);

        if (cursor != null && cursor.getCount() > 0 ){
            EstadoAdap estadoAdap = new EstadoAdap();
            PaisDAO paisDAO = PaisDAO.getInstance(context);

            while(cursor.moveToNext()) {
                //Converte o cursor em um objeto
                estado = estadoAdap.sqlToEstado(cursor);
                estado.setPais(paisDAO.buscaPaisId(cursor.getInt(cursor.getColumnIndex("PAIS_ID"))));
            }
            cursor.close();
        }

        return estado;
    }

    public Estado insereEstado(Estado estado) {

        EstadoAdap estadoAdap = new EstadoAdap();
        //Converte o objeto em um contetValue para inserir no banco
        ContentValues content = estadoAdap.estadoToContentValue(estado);
        //Insere o estado no banco
        Integer estadoId = (int) dataBase.insert(ESTADO_TABLE_NAME, null, content);
        /*
        if(loginId <= 0){
            throw new Exceptions("Não foi possível inserir o usuário");
        }
        */
        estado.setId(estadoId);

        return estado;

    }

    public void truncateEstados(){
        dataBase.delete(ESTADO_TABLE_NAME,null,null);
    }

    public void fecharConexao() {
        if(dataBase != null && dataBase.isOpen()) {
            dataBase.close();
        }
    }

}
