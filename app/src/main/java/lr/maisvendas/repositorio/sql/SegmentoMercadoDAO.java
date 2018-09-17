package lr.maisvendas.repositorio.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import lr.maisvendas.adaptadorModelo.SegmentoMercadoAdap;
import lr.maisvendas.modelo.SegmentoMercado;
import lr.maisvendas.repositorio.DatabaseHelper;
import lr.maisvendas.utilitarios.Exceptions;

public class SegmentoMercadoDAO {
	
    private Context context;
	
    //private SegmentoMercadoAdap segmentoMercadoAdap;
    private static final String SEGMENTO_MERCADO_TABLE_NAME = "tsegmentos_mercado";

    public static final String SEGMENTO_MERCADO_TABLE_CREATE = "CREATE TABLE if not exists " + SEGMENTO_MERCADO_TABLE_NAME + " ("
            + "ID INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "ID_WS TEXT, "
            + "DESCRICAO TEXT NOT NULL);";

    public static final String SCRIPT_DELECAO_TABELA =  "DROP TABLE IF EXISTS " + SEGMENTO_MERCADO_TABLE_NAME;

    private SQLiteDatabase dataBase = null;

    private static SegmentoMercadoDAO instance;

    public static SegmentoMercadoDAO getInstance(Context context) {
        if(instance == null)
            instance = new SegmentoMercadoDAO(context);
        return instance;
    }

    private SegmentoMercadoDAO(Context context) {
        this.context = context;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        dataBase = databaseHelper.getWritableDatabase();
        //segmentoMercadoAdap = new SegmentoMercadoAdap();
    }

    public SegmentoMercado buscaSegmentoMercadoId(Integer segmentoMercadoId){
        SegmentoMercado segmentoMercado = null;

        //Busca o grupo
        String sql = "SELECT * FROM tsegmentos_mercado WHERE id = "+ segmentoMercadoId ;
        Cursor cursor = dataBase.rawQuery(sql, null);

        if (cursor != null && cursor.getCount() > 0 ){
            SegmentoMercadoAdap segmentoMercadoAdap = new SegmentoMercadoAdap();

            while(cursor.moveToNext()) {
                //Converte o cursor em um objeto
                segmentoMercado = segmentoMercadoAdap.sqlToSegmentoMercado(cursor);
            }
            cursor.close();
        }

        return segmentoMercado;

    }

    public SegmentoMercado buscaSegmentoMercadoIdWs(String segmentoMercadoIdWs){
        SegmentoMercado segmentoMercado = null;

        //Busca o grupo
        String sql = "SELECT * FROM tsegmentos_mercado WHERE id_ws = '"+ segmentoMercadoIdWs +"'";
        Cursor cursor = dataBase.rawQuery(sql, null);

        if (cursor != null && cursor.getCount() > 0 ){
            SegmentoMercadoAdap segmentoMercadoAdap = new SegmentoMercadoAdap();

            while(cursor.moveToNext()) {
                //Converte o cursor em um objeto
                segmentoMercado = segmentoMercadoAdap.sqlToSegmentoMercado(cursor);
            }
            cursor.close();
        }

        return segmentoMercado;

    }

    public SegmentoMercado insereSegmentoMercado(SegmentoMercado segmentoMercado) {

        SegmentoMercadoAdap segmentoMercadoAdap = new SegmentoMercadoAdap();
        //Converte o objeto em um contetValue para inserir no banco
        ContentValues content = segmentoMercadoAdap.segmentoMercadoToContentValue(segmentoMercado);
        //Insere o segmentoMercado no banco
        Integer segmentoMercadoId = (int) dataBase.insert(SEGMENTO_MERCADO_TABLE_NAME, null, content);
        /*
        if(loginId <= 0){
            throw new Exceptions("Não foi possível inserir o usuário");
        }
        */
        segmentoMercado.setId(segmentoMercadoId);

        return segmentoMercado;

    }

    public SegmentoMercado atualizaSegmentoMercado(SegmentoMercado segmentoMercado) throws Exceptions {

        SegmentoMercadoAdap segmentoMercadoAdap = new SegmentoMercadoAdap();
        //Converte o objeto em um contetValue para inserir no banco
        ContentValues content = segmentoMercadoAdap.segmentoMercadoToContentValue(segmentoMercado);
        String sqlWhere = "id = "+segmentoMercado.getId();

        //Insere o segmentoMercado no banco
        Integer executou = (int) dataBase.update(SEGMENTO_MERCADO_TABLE_NAME, content, sqlWhere,null);

        if(executou <= 0){
            throw new Exceptions("Não foi possível inserir o Segmento de Mercado (ID: "+segmentoMercado.getId()+")");
        }

        return segmentoMercado;

    }

    public void truncateSegmentoMercados(){
        dataBase.delete(SEGMENTO_MERCADO_TABLE_NAME,null,null);
    }

    public void fecharConexao() {
        if(dataBase != null && dataBase.isOpen()) {
            dataBase.close();
        }
    }

}
