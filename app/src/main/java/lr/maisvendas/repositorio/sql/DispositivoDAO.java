package lr.maisvendas.repositorio.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import lr.maisvendas.adaptadorModelo.DispositivoAdap;
import lr.maisvendas.modelo.Dispositivo;
import lr.maisvendas.repositorio.DatabaseHelper;
import lr.maisvendas.utilitarios.Exceptions;

public class DispositivoDAO {
	
    private Context context;
    //private DispositivoAdap dispositivoAdap;
    private static final String DISPOSITIVO_TABLE_NAME = "tdispositivos";

    public static final String DISPOSITIVO_TABLE_CREATE = "CREATE TABLE if not exists " + DISPOSITIVO_TABLE_NAME + " (" +
             "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
             "DATA_SINC_IMAGENS TEXT, " +
             "DATA_SINC_PEDIDOS TEXT, " +
             "DATA_SINC_CLIENTES TEXT, " +
             "DATA_SINC_PRODUTOS TEXT, " +
             "DATA_SINC_ATIVIDADES)";

    public static final String SCRIPT_DELECAO_TABELA =  "DROP TABLE IF EXISTS " + DISPOSITIVO_TABLE_NAME;

    private SQLiteDatabase dataBase = null;

    private static DispositivoDAO instance;

    public static DispositivoDAO getInstance(Context context) {
        if(instance == null)
            instance = new DispositivoDAO(context);
        return instance;
    }

    private DispositivoDAO(Context context) {
        this.context = context;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        dataBase = databaseHelper.getWritableDatabase();
        //dispositivoAdap = new DispositivoAdap();
    }

    public Dispositivo buscaDispositivo(){
        Dispositivo dispositivo = null;

        //Busca o grupo
        String sql = "SELECT * FROM tdispositivos";
        Cursor cursor = dataBase.rawQuery(sql, null);

        if (cursor != null && cursor.getCount() > 0 ){
            DispositivoAdap dispositivoAdap = new DispositivoAdap();
            while(cursor.moveToNext()) {
                //Converte o cursor em um objeto
                dispositivo = dispositivoAdap.sqlToDispositivo(cursor);
            }
            cursor.close();
        }

        return dispositivo;
    }

    public Dispositivo insereDispositivo(Dispositivo dispositivo) {

        DispositivoAdap dispositivoAdap = new DispositivoAdap();
        //Converte o objeto em um contetValue para inserir no banco
        ContentValues content = dispositivoAdap.dispositivoToContentValue(dispositivo);
        //Insere o dispositivo no banco
        Integer dispositivoId = (int) dataBase.insert(DISPOSITIVO_TABLE_NAME, null, content);
        /*
        if(loginId <= 0){
            throw new Exceptions("Não foi possível inserir o usuário");
        }
        */
        dispositivo.setId(dispositivoId);

        return dispositivo;

    }

    public Dispositivo atualizaDispositivo(Dispositivo dispositivo) throws Exceptions{

        DispositivoAdap dispositivoAdap = new DispositivoAdap();
        //Converte o objeto em um contetValue para inserir no banco
        ContentValues content = dispositivoAdap.dispositivoToContentValue(dispositivo);
        //Monta WHERE
        String sqlWhere = "id = "+dispositivo.getId();
        //Insere o dispositivo no banco
        Integer executou = (int) dataBase.update(DISPOSITIVO_TABLE_NAME, content,sqlWhere,null);

        if (executou <= 0) {
            throw new Exceptions("Não foi possível atualizar o Dispositovo ID: " + dispositivo.getId());
        }

        return dispositivo;

    }

    public void truncateDispositivo(){
        dataBase.delete(DISPOSITIVO_TABLE_NAME,null,null);
    }

    public void fecharConexao() {
        if(dataBase != null && dataBase.isOpen()) {
            dataBase.close();
        }
    }

}
