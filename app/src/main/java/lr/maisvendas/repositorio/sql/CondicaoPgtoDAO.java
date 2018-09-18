package lr.maisvendas.repositorio.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import lr.maisvendas.adaptadorModelo.CondicaoPgtoAdap;
import lr.maisvendas.modelo.CondicaoPgto;
import lr.maisvendas.repositorio.DatabaseHelper;

public class CondicaoPgtoDAO {
	
    private Context context;
	
    //private TabelaAdap tabelaAdap;
    private static final String CONDICAO_PGTO_TABLE_NAME = "tcondicoes_pgto";

    public static final String CONDICAO_PGTO_TABLE_CREATE = "CREATE TABLE if not exists " + CONDICAO_PGTO_TABLE_NAME + " (" +
            "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
            "ID_WS TEXT," +
            "CODIGO TEXT, " +
            "DESCRICAO TEXT," +
            "DESC_ACR REAL);";

    public static final String SCRIPT_DELECAO_TABELA =  "DROP TABLE IF EXISTS " + CONDICAO_PGTO_TABLE_NAME;

    private SQLiteDatabase dataBase = null;

    private static CondicaoPgtoDAO instance;

    public static CondicaoPgtoDAO getInstance(Context context) {
        if(instance == null)
            instance = new CondicaoPgtoDAO(context);
        return instance;
    }

    private CondicaoPgtoDAO(Context context) {
        this.context = context;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        dataBase = databaseHelper.getWritableDatabase();
        //tabelaAdap = new TabelaAdap();
    }

    public CondicaoPgto buscaCondicaoPgtoId(Integer condicaoPgtoId){
        CondicaoPgto condicaoPgto = null;

        //Busca o grupo
        String sql = "SELECT * FROM tcondicoes_pgto WHERE id = "+ condicaoPgtoId ;
        Cursor cursor = dataBase.rawQuery(sql, null);

        if (cursor != null && cursor.getCount() > 0 ){
            CondicaoPgtoAdap condicaoPgtoAdap = new CondicaoPgtoAdap();
            while(cursor.moveToNext()) {
                //Converte o cursor em um objeto
                condicaoPgto = condicaoPgtoAdap.sqlToCondicaoPgto(cursor);
            }
            cursor.close();
        }

        return condicaoPgto;
    }

    public List<CondicaoPgto> buscaCondicaoPgto(){
        List<CondicaoPgto> condicoesPgto = new ArrayList<>();
        CondicaoPgto condicaoPgto = null;

        //Busca o grupo
        String sql = "SELECT * FROM tcondicoes_pgto";
        Cursor cursor = dataBase.rawQuery(sql, null);

        if (cursor != null && cursor.getCount() > 0 ) {
            CondicaoPgtoAdap condicaoPgtoAdap = new CondicaoPgtoAdap();
            while (cursor.moveToNext()) {
                //Converte o cursor em um objeto
                condicaoPgto = condicaoPgtoAdap.sqlToCondicaoPgto(cursor);

                condicoesPgto.add(condicaoPgto);
            }
            cursor.close();

        }
        return condicoesPgto;
    }

    public CondicaoPgto insereCondicaoPgto(CondicaoPgto condicaoPgto) {

        CondicaoPgtoAdap condicaoPgtoAdap = new CondicaoPgtoAdap();
        //Converte o objeto em um contetValue para inserir no banco
        ContentValues content = condicaoPgtoAdap.condicaoPgtoToContentValue(condicaoPgto);
        //Insere o condicaoPgto no banco
        Integer condicaoPgtoId = (int) dataBase.insert(CONDICAO_PGTO_TABLE_NAME, null, content);
        /*
        if(loginId <= 0){
            throw new Exceptions("Não foi possível inserir o usuário");
        }
        */
        condicaoPgto.setId(condicaoPgtoId);

        return condicaoPgto;

    }
    
    public void truncateTabelas(){
        dataBase.delete(CONDICAO_PGTO_TABLE_NAME,null,null);
    }

    public void fecharConexao() {
        if(dataBase != null && dataBase.isOpen()) {
            dataBase.close();
        }
    }

}
