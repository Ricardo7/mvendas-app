package lr.maisvendas.repositorio.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import lr.maisvendas.adaptadorModelo.CondicaoPgtoAdap;
import lr.maisvendas.modelo.CondicaoPagamento;
import lr.maisvendas.repositorio.DatabaseHelper;
import lr.maisvendas.utilitarios.Exceptions;

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

    public CondicaoPagamento buscaCondicaoPgtoId(Integer condicaoPgtoId){
        CondicaoPagamento condicaoPagamento = null;

        //Busca o grupo
        String sql = "SELECT * FROM tcondicoes_pgto WHERE id = "+ condicaoPgtoId ;
        Cursor cursor = dataBase.rawQuery(sql, null);

        if (cursor != null && cursor.getCount() > 0 ){
            CondicaoPgtoAdap condicaoPgtoAdap = new CondicaoPgtoAdap();
            while(cursor.moveToNext()) {
                //Converte o cursor em um objeto
                condicaoPagamento = condicaoPgtoAdap.sqlToCondicaoPgto(cursor);
            }
            cursor.close();
        }

        return condicaoPagamento;
    }

    public CondicaoPagamento buscaCondicaoPgtoIdWs(String condicaoPgtoIdWs){
        CondicaoPagamento condicaoPagamento = null;

        //Busca o grupo
        String sql = "SELECT * FROM tcondicoes_pgto WHERE id_ws = '"+ condicaoPgtoIdWs +"'";
        Cursor cursor = dataBase.rawQuery(sql, null);

        if (cursor != null && cursor.getCount() > 0 ){
            CondicaoPgtoAdap condicaoPgtoAdap = new CondicaoPgtoAdap();
            while(cursor.moveToNext()) {
                //Converte o cursor em um objeto
                condicaoPagamento = condicaoPgtoAdap.sqlToCondicaoPgto(cursor);
            }
            cursor.close();
        }

        return condicaoPagamento;
    }

    public List<CondicaoPagamento> buscaCondicaoPgto(){
        List<CondicaoPagamento> condicoesPgto = new ArrayList<>();
        CondicaoPagamento condicaoPagamento = null;

        //Busca o grupo
        String sql = "SELECT * FROM tcondicoes_pgto";
        Cursor cursor = dataBase.rawQuery(sql, null);

        if (cursor != null && cursor.getCount() > 0 ) {
            CondicaoPgtoAdap condicaoPgtoAdap = new CondicaoPgtoAdap();
            while (cursor.moveToNext()) {
                //Converte o cursor em um objeto
                condicaoPagamento = condicaoPgtoAdap.sqlToCondicaoPgto(cursor);

                condicoesPgto.add(condicaoPagamento);
            }
            cursor.close();

        }
        return condicoesPgto;
    }

    public CondicaoPagamento insereCondicaoPgto(CondicaoPagamento condicaoPagamento) {

        CondicaoPgtoAdap condicaoPgtoAdap = new CondicaoPgtoAdap();
        //Converte o objeto em um contetValue para inserir no banco
        ContentValues content = condicaoPgtoAdap.condicaoPgtoToContentValue(condicaoPagamento);
        //Insere o condicaoPagamento no banco
        Integer condicaoPgtoId = (int) dataBase.insert(CONDICAO_PGTO_TABLE_NAME, null, content);
        /*
        if(loginId <= 0){
            throw new Exceptions("Não foi possível inserir o usuário");
        }
        */
        condicaoPagamento.setId(condicaoPgtoId);

        return condicaoPagamento;

    }

    public CondicaoPagamento atualizaCondicaoPgto(CondicaoPagamento condicaoPagamento) throws Exceptions{

        CondicaoPgtoAdap condicaoPgtoAdap = new CondicaoPgtoAdap();
        //Converte o objeto em um contetValue para inserir no banco
        ContentValues content = condicaoPgtoAdap.condicaoPgtoToContentValue(condicaoPagamento);
        String sqlWhere = "id = "+ condicaoPagamento.getId();
        //Insere o condicaoPagamento no banco
        Integer executou = (int) dataBase.update(CONDICAO_PGTO_TABLE_NAME,content,sqlWhere,null);

        if(executou <= 0){
            throw new Exceptions("Não foi possível atualizar a condição de pagamento (ID="+ condicaoPagamento.getId()+")");
        }

        return condicaoPagamento;

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
