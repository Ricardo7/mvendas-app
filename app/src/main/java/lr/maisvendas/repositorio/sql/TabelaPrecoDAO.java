package lr.maisvendas.repositorio.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import lr.maisvendas.adaptadorModelo.TabelaPrecoAdap;
import lr.maisvendas.modelo.TabelaPreco;
import lr.maisvendas.repositorio.DatabaseHelper;

public class TabelaPrecoDAO {
	
    private Context context;
	
    //private TabelaAdap tabelaAdap;
    private static final String TABELA_PRECOS_TABLE_NAME = "ttabela_precos";

    public static final String TABELA_PRECOS_TABLE_CREATE = "CREATE TABLE if not exists " + TABELA_PRECOS_TABLE_NAME + " (" +
            "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "CODIGO TEXT, " +
            "DESCRICAO TEXT);";

    public static final String SCRIPT_DELECAO_TABELA =  "DROP TABLE IF EXISTS " + TABELA_PRECOS_TABLE_NAME;

    private SQLiteDatabase dataBase = null;

    private static TabelaPrecoDAO instance;

    public static TabelaPrecoDAO getInstance(Context context) {
        if(instance == null)
            instance = new TabelaPrecoDAO(context);
        return instance;
    }

    private TabelaPrecoDAO(Context context) {
        this.context = context;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        dataBase = databaseHelper.getWritableDatabase();
        //tabelaAdap = new TabelaAdap();
    }

    public TabelaPreco buscaTabelaPrecoId(Integer tabelaPrecoId){
        TabelaPreco tabelaPreco = null;

        //Busca o grupo
        String sql = "SELECT * FROM ttabela_precos WHERE id = "+ tabelaPrecoId ;
        Cursor cursor = dataBase.rawQuery(sql, null);

        if (cursor != null && cursor.getCount() > 0 ){
            TabelaPrecoAdap tabelaPrecoAdap = new TabelaPrecoAdap();
            ItemTabelaPrecoDAO itemTabelaPrecoDAO = ItemTabelaPrecoDAO.getInstance(context);

            while(cursor.moveToNext()) {
                //Converte o cursor em um objeto
                tabelaPreco = tabelaPrecoAdap.sqlToTabelaPreco(cursor);
                tabelaPreco.setItensTabelaPreco(itemTabelaPrecoDAO.buscaItensTabelaPreco(cursor.getInt(cursor.getColumnIndex("ID"))));
            }
            cursor.close();
        }

        return tabelaPreco;
    }

    public List<TabelaPreco> buscaTabelasPreco(){
        List<TabelaPreco> tabelasPreco = new ArrayList<>();
        TabelaPreco tabelaPreco = null;

        //Busca o grupo
        String sql = "SELECT * FROM ttabela_precos ";
        Cursor cursor = dataBase.rawQuery(sql, null);

        if (cursor != null && cursor.getCount() > 0 ){
            TabelaPrecoAdap tabelaPrecoAdap = new TabelaPrecoAdap();
            ItemTabelaPrecoDAO itemTabelaPrecoDAO = ItemTabelaPrecoDAO.getInstance(context);

            while(cursor.moveToNext()) {
                //Converte o cursor em um objeto
                tabelaPreco = tabelaPrecoAdap.sqlToTabelaPreco(cursor);
                tabelaPreco.setItensTabelaPreco(itemTabelaPrecoDAO.buscaItensTabelaPreco(cursor.getInt(cursor.getColumnIndex("ID"))));

                tabelasPreco.add(tabelaPreco);
            }
            cursor.close();
        }

        return tabelasPreco;
    }

    public TabelaPreco insereTabelaPreco(TabelaPreco tabelaPreco) {

        TabelaPrecoAdap tabelaPrecoAdap = new TabelaPrecoAdap();
        //Converte o objeto em um contetValue para inserir no banco
        ContentValues content = tabelaPrecoAdap.tabelaPrecoToContentValue(tabelaPreco);
        //Insere o tabelaPreco no banco
        Integer tabelaPrecoId = (int) dataBase.insert(TABELA_PRECOS_TABLE_NAME, null, content);
        /*
        if(loginId <= 0){
            throw new Exceptions("Não foi possível inserir o usuário");
        }
        */
        tabelaPreco.setId(tabelaPrecoId);

        return tabelaPreco;

    }

    public void truncateTabelas(){
        dataBase.delete(TABELA_PRECOS_TABLE_NAME,null,null);
    }

    public void fecharConexao() {
        if(dataBase != null && dataBase.isOpen()) {
            dataBase.close();
        }
    }

}
