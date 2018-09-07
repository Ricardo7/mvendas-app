package lr.maisvendas.repositorio.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import lr.maisvendas.adaptadorModelo.MetaAdap;
import lr.maisvendas.modelo.Meta;
import lr.maisvendas.repositorio.DatabaseHelper;

public class MetaDAO {
	
    private Context context;
    //private MetaAdap metaAdap;
    private static final String META_TABLE_NAME = "tmetas";

    public static final String META_TABLE_CREATE = "CREATE TABLE if not exists " + META_TABLE_NAME + " (" +
            "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "ESTIMADO REAL, " +
            "REALIZADO REAL)";

    public static final String SCRIPT_DELECAO_TABELA =  "DROP TABLE IF EXISTS " + META_TABLE_NAME;

    private SQLiteDatabase dataBase = null;

    private static MetaDAO instance;

    public static MetaDAO getInstance(Context context) {
        if(instance == null)
            instance = new MetaDAO(context);
        return instance;
    }

    private MetaDAO(Context context) {
        this.context = context;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        dataBase = databaseHelper.getWritableDatabase();
        //metaAdap = new MetaAdap();
    }

    public Meta buscaMetaId(Integer metaId){
        Meta meta = null;

        //Busca o grupo
        String sql = "SELECT * FROM tmetas WHERE id = "+ metaId ;
        Cursor cursor = dataBase.rawQuery(sql, null);

        if (cursor != null && cursor.getCount() > 0 ){
            MetaAdap metaAdap = new MetaAdap();
            while(cursor.moveToNext()) {
                //Converte o cursor em um objeto
                meta = metaAdap.sqlToMeta(cursor);
            }
            cursor.close();
        }

        return meta;
    }

    public List<Meta> buscaMetas(){
        Meta meta = null;
        List<Meta> metas = new ArrayList<>();

        //Busca o grupo
        String sql = "SELECT * FROM tmetas" ;
        Cursor cursor = dataBase.rawQuery(sql, null);

        if (cursor != null && cursor.getCount() > 0 ){
            MetaAdap metaAdap = new MetaAdap();
            while(cursor.moveToNext()) {
                //Converte o cursor em um objeto
                meta = metaAdap.sqlToMeta(cursor);
                metas.add(meta);
            }
            cursor.close();
        }

        return metas;
    }

    public Meta insereMeta(Meta meta) {

        MetaAdap metaAdap = new MetaAdap();
        //Converte o objeto em um contetValue para inserir no banco
        ContentValues content = metaAdap.metaToContentValue(meta);
        //Insere o meta no banco
        Integer metaId = (int) dataBase.insert(META_TABLE_NAME, null, content);
        /*
        if(loginId <= 0){
            throw new Exceptions("Não foi possível inserir o usuário");
        }
        */
        meta.setId(metaId);

        return meta;

    }
    
    public void truncateMetas(){
        dataBase.delete(META_TABLE_NAME,null,null);
    }

    public void fecharConexao() {
        if(dataBase != null && dataBase.isOpen()) {
            dataBase.close();
        }
    }

}
