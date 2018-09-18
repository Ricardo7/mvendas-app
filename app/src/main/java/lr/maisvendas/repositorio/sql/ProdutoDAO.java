package lr.maisvendas.repositorio.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import lr.maisvendas.adaptadorModelo.ProdutoAdap;
import lr.maisvendas.modelo.Produto;
import lr.maisvendas.repositorio.DatabaseHelper;
import lr.maisvendas.utilitarios.Exceptions;

public class ProdutoDAO {
	
    private Context context;
	
    //private TabelaAdap tabelaAdap;
    private static final String PRODUTO_TABLE_NAME = "tprodutos";

    public static final String PRODUTO_TABLE_CREATE = "CREATE TABLE if not exists " + PRODUTO_TABLE_NAME + " (" +
            "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "ID_WS TEXT, " +
            "CODIGO TEXT, " +
            "DESCRICAO TEXT," +
            "OBSERVACAO TEXT);";

    public static final String SCRIPT_DELECAO_TABELA =  "DROP TABLE IF EXISTS " + PRODUTO_TABLE_NAME;

    private SQLiteDatabase dataBase = null;

    private static ProdutoDAO instance;

    public static ProdutoDAO getInstance(Context context) {
        if(instance == null)
            instance = new ProdutoDAO(context);
        return instance;
    }

    private ProdutoDAO(Context context) {
        this.context = context;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        dataBase = databaseHelper.getWritableDatabase();
        //tabelaAdap = new TabelaAdap();
    }

    public Produto buscaProdutoId(Integer produtoId){
        Produto produto = null;

        //Busca o grupo
        String sql = "SELECT * FROM tprodutos WHERE id = "+ produtoId ;
        Cursor cursor = dataBase.rawQuery(sql, null);

        if (cursor != null && cursor.getCount() > 0 ){
            ProdutoAdap produtoAdap = new ProdutoAdap();
            while(cursor.moveToNext()) {
                //Converte o cursor em um objeto
                produto = produtoAdap.sqlToProduto(cursor);
            }
            cursor.close();
        }

        return produto;
    }

    public Produto buscaProdutoIdWs(String produtoIdWs){
        Produto produto = null;

        //Busca o grupo
        String sql = "SELECT * FROM tprodutos WHERE id_ws = '"+ produtoIdWs +"'" ;
        Cursor cursor = dataBase.rawQuery(sql, null);

        if (cursor != null && cursor.getCount() > 0 ){
            ProdutoAdap produtoAdap = new ProdutoAdap();
            while(cursor.moveToNext()) {
                //Converte o cursor em um objeto
                produto = produtoAdap.sqlToProduto(cursor);
            }
            cursor.close();
        }

        return produto;
    }
    
    public List<Produto> buscaProdutos(){
        List<Produto> produtos = new ArrayList<>();
        Produto produto = null;

        //Busca o grupo
        String sql = "SELECT * FROM tprodutos";
        Cursor cursor = dataBase.rawQuery(sql, null);

        if (cursor != null && cursor.getCount() > 0 ){
            ProdutoAdap produtoAdap = new ProdutoAdap();
            ImagemDAO imagemDAO = ImagemDAO.getInstance(context);
            while(cursor.moveToNext()) {
                //Converte o cursor em um objeto
                produto = produtoAdap.sqlToProduto(cursor);

                produto.setImagens(imagemDAO.buscaImagensProduto(cursor.getInt(cursor.getColumnIndex("ID"))));

                produtos.add(produto);
            }
            cursor.close();
        }

        return produtos;
    }

    public Produto insereProduto(Produto produto) {

        ProdutoAdap produtoAdap = new ProdutoAdap();
        //Converte o objeto em um contetValue para inserir no banco
        ContentValues content = produtoAdap.produtoToContentValue(produto);
        //Insere o produto no banco
        Integer produtoId = (int) dataBase.insert(PRODUTO_TABLE_NAME, null, content);
        /*
        if(loginId <= 0){
            throw new Exceptions("Não foi possível inserir o usuário");
        }
        */
        produto.setId(produtoId);

        return produto;

    }

    public Produto atualizaProduto(Produto produto) throws Exceptions {

        ProdutoAdap produtoAdap = new ProdutoAdap();
        //Converte o objeto em um contetValue para inserir no banco
        ContentValues content = produtoAdap.produtoToContentValue(produto);
        String sqlWhere = "id = "+produto.getId();
        //Insere o produto no banco
        Integer executou = (int) dataBase.update(PRODUTO_TABLE_NAME, content, sqlWhere,null);

        if(executou <= 0){
            throw new Exceptions("Não foi possível atualizar o Produto (ID: "+produto.getId()+")");
        }

        return produto;

    }
    
    public void truncateTabelas(){
        dataBase.delete(PRODUTO_TABLE_NAME,null,null);
    }

    public void fecharConexao() {
        if(dataBase != null && dataBase.isOpen()) {
            dataBase.close();
        }
    }

}
