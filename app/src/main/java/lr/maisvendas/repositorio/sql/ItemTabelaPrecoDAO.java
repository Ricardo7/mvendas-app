package lr.maisvendas.repositorio.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import lr.maisvendas.adaptadorModelo.ItemTabelaPrecoAdap;
import lr.maisvendas.modelo.ItemTabelaPreco;
import lr.maisvendas.repositorio.DatabaseHelper;
import lr.maisvendas.utilitarios.Exceptions;

public class ItemTabelaPrecoDAO {
	
    private Context context;
	
    //private ItemAdap itemAdap;
    private static final String ITEM_TABELA_PRECO_TABLE_NAME = "titens_tabela_precos";

    public static final String ITEM_TABELA_PRECO_TABLE_CREATE = "CREATE TABLE if not exists " + ITEM_TABELA_PRECO_TABLE_NAME + " (" +
            "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "PRODUTO_ID INTEGER NOT NULL, " +
            "TABELA_PRECO_ID INTEGER NOT NULL, " +
            "VLR_UNITARIO REAL, " +
            "MAX_DESC REAL);";

    public static final String SCRIPT_DELECAO_TABELA =  "DROP TABLE IF EXISTS " + ITEM_TABELA_PRECO_TABLE_NAME;

    private SQLiteDatabase dataBase = null;

    private static ItemTabelaPrecoDAO instance;

    public static ItemTabelaPrecoDAO getInstance(Context context) {
        if(instance == null)
            instance = new ItemTabelaPrecoDAO(context);
        return instance;
    }

    private ItemTabelaPrecoDAO(Context context) {
        this.context = context;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        dataBase = databaseHelper.getWritableDatabase();
        //itemAdap = new ItemAdap();
    }

    public ItemTabelaPreco buscaItemTabelaPrecoPedidoProduto(Integer pedidoId, Integer produtoId){

        ItemTabelaPreco itemTabelaPreco = null;

        String sql = "SELECT ittab.* " +
                " FROM titens_tabela_precos ittab" +
                "      INNER JOIN ttabela_precos tab ON ittab.tabela_preco_id = tab.id" +
                "      INNER JOIN tpedidos pd        ON tab.id = pd.tabela_preco_id" +
                " WHERE ittab.produto_id = " + produtoId +
                "   AND pd.id            = " + pedidoId;

        Cursor cursor = dataBase.rawQuery(sql, null);

        if (cursor != null && cursor.getCount() > 0 ){
            ProdutoDAO produtoDAO = ProdutoDAO.getInstance(context);

            ItemTabelaPrecoAdap itemTabelaPrecoAdap = new ItemTabelaPrecoAdap();
            while(cursor.moveToNext()) {
                //Converte o cursor em um objeto
                itemTabelaPreco = itemTabelaPrecoAdap.sqlToItemTabelaPreco(cursor);
                itemTabelaPreco.setProduto(produtoDAO.buscaProdutoId(cursor.getInt(cursor.getColumnIndex("PRODUTO_ID"))));

            }
            cursor.close();
        }

        return itemTabelaPreco;
    }

    public List<ItemTabelaPreco> buscaItensTabelaPreco(Integer tabelaPrecoId){
        ItemTabelaPreco itemTabelaPreco = null;
        List<ItemTabelaPreco> itensTabelaPreco = new ArrayList<>();

        //Busca o grupo
        String sql = "SELECT * FROM titens_tabela_precos WHERE tabela_preco_id = "+ tabelaPrecoId ;
        Cursor cursor = dataBase.rawQuery(sql, null);

        if (cursor != null && cursor.getCount() > 0 ){
            ProdutoDAO produtoDAO = ProdutoDAO.getInstance(context);

            ItemTabelaPrecoAdap itemTabelaPrecoAdap = new ItemTabelaPrecoAdap();
            while(cursor.moveToNext()) {
                //Converte o cursor em um objeto
                itemTabelaPreco = itemTabelaPrecoAdap.sqlToItemTabelaPreco(cursor);
                itemTabelaPreco.setProduto(produtoDAO.buscaProdutoId(cursor.getInt(cursor.getColumnIndex("PRODUTO_ID"))));

                itensTabelaPreco.add(itemTabelaPreco);
            }
            cursor.close();
        }

        return itensTabelaPreco;
    }

    public ItemTabelaPreco buscaItemTabelaPrecoProduto(Integer tabelaPrecoId, String codProduto){
        ItemTabelaPreco itemTabelaPreco = null;

        //Busca o grupo
        String sql = "SELECT * " +
                     "  FROM titens_tabela_precos itpr" +
                     "       INNER JOIN produto pd ON itpr.produto_id = pd.id" +
                     " WHERE itpr.tabela_preco_id = "+ tabelaPrecoId +
                     "   AND pd.codigo =  '"+ codProduto +"'";
        Cursor cursor = dataBase.rawQuery(sql, null);

        if (cursor != null && cursor.getCount() > 0 ){
            ProdutoDAO produtoDAO = ProdutoDAO.getInstance(context);

            ItemTabelaPrecoAdap itemTabelaPrecoAdap = new ItemTabelaPrecoAdap();
            while(cursor.moveToNext()) {
                //Converte o cursor em um objeto
                itemTabelaPreco = itemTabelaPrecoAdap.sqlToItemTabelaPreco(cursor);
                itemTabelaPreco.setProduto(produtoDAO.buscaProdutoId(cursor.getInt(cursor.getColumnIndex("PRODUTO_ID"))));

            }
            cursor.close();
        }

        return itemTabelaPreco;
    }

    public ItemTabelaPreco insereItemTabelaPreco(ItemTabelaPreco itemTabelaPreco, Integer tabelaPrecoId) {

        ItemTabelaPrecoAdap itemTabelaPrecoAdap = new ItemTabelaPrecoAdap();
        //Converte o objeto em um contetValue para inserir no banco
        ContentValues content = itemTabelaPrecoAdap.itemTabelaPrecoToContentValue(itemTabelaPreco,tabelaPrecoId);

        //Insere o itemTabelaPreco no banco
        Integer itemTabelaPrecoId = (int) dataBase.insert(ITEM_TABELA_PRECO_TABLE_NAME, null, content);
        /*
        if(loginId <= 0){
            throw new Exceptions("Não foi possível inserir o usuário");
        }
        */
        itemTabelaPreco.setId(itemTabelaPrecoId);

        return itemTabelaPreco;

    }

    public ItemTabelaPreco atualizaItemTabelaPreco(ItemTabelaPreco itemTabelaPreco, Integer tabelaPrecoId) throws Exceptions {

        ItemTabelaPrecoAdap itemTabelaPrecoAdap = new ItemTabelaPrecoAdap();
        //Converte o objeto em um contetValue para inserir no banco
        ContentValues content = itemTabelaPrecoAdap.itemTabelaPrecoToContentValue(itemTabelaPreco,tabelaPrecoId);
        String sqlWhere = "id = "+itemTabelaPreco.getId();

        //Insere o itemTabelaPreco no banco
        Integer executou = (int) dataBase.update(ITEM_TABELA_PRECO_TABLE_NAME, content,sqlWhere,null);

        if(executou <= 0){
            throw new Exceptions("Não foi possível atualizar o item da tabela de preço (ID="+itemTabelaPreco.getId()+")");
        }

        return itemTabelaPreco;

    }

    public void truncateItems(){
        dataBase.delete(ITEM_TABELA_PRECO_TABLE_NAME,null,null);
    }

    public void fecharConexao() {
        if(dataBase != null && dataBase.isOpen()) {
            dataBase.close();
        }
    }

}
