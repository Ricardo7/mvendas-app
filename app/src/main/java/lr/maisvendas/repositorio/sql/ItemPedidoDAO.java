package lr.maisvendas.repositorio.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import lr.maisvendas.adaptadorModelo.ItemPedidoAdap;
import lr.maisvendas.modelo.ItemPedido;
import lr.maisvendas.repositorio.DatabaseHelper;

public class ItemPedidoDAO {
	
    private Context context;
	
    //private ItemAdap itemAdap;
    private static final String ITEM_PEDIDO_TABLE_NAME = "titens_pedido";

    public static final String ITEM_PEDIDO_TABLE_CREATE = "CREATE TABLE if not exists " + ITEM_PEDIDO_TABLE_NAME + " (" +
            "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "PEDIDO_ID INTEGER NOT NULL, " +
            "PRODUTO_ID INTEGER NOT NULL, " +
            "QUANTIDADE INTEGER," +
            "VLR_UNITARIO REAL," +
            "VLR_DESCONTO REAL," +
            "VLR_TOTAL REAL," +
            "DT_CRIACAO TEXT, " +
            "DT_ATUALIZACAO TEXT);";

    public static final String SCRIPT_DELECAO_TABELA =  "DROP TABLE IF EXISTS " + ITEM_PEDIDO_TABLE_NAME;

    private SQLiteDatabase dataBase = null;

    private static ItemPedidoDAO instance;

    public static ItemPedidoDAO getInstance(Context context) {
        if(instance == null)
            instance = new ItemPedidoDAO(context);
        return instance;
    }

    private ItemPedidoDAO(Context context) {
        this.context = context;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        dataBase = databaseHelper.getWritableDatabase();
        //itemAdap = new ItemAdap();
    }

    public List<ItemPedido> buscaItemPedido(Integer pedidoId){
        ItemPedido itemPedido = null;
        List<ItemPedido> itensPedido = new ArrayList<>();

        //Busca o grupo
        String sql = "SELECT * FROM titens_pedido WHERE pedido_id = "+ pedidoId ;
        Cursor cursor = dataBase.rawQuery(sql, null);

        if (cursor != null && cursor.getCount() > 0 ){
            ProdutoDAO produtoDAO = ProdutoDAO.getInstance(context);

            ItemPedidoAdap itemPedidoAdap = new ItemPedidoAdap();
            while(cursor.moveToNext()) {
                //Converte o cursor em um objeto
                itemPedido = itemPedidoAdap.sqlToItemPedido(cursor);
                itemPedido.setProduto(produtoDAO.buscaProdutoId(cursor.getInt(cursor.getColumnIndex("PRODUTO_ID"))));

                itensPedido.add(itemPedido);
            }
            cursor.close();
        }

        return itensPedido;
    }

    public ItemPedido insereItemPedido(ItemPedido itemPedido, Integer pedidoId) {

        ItemPedidoAdap itemPedidoAdap = new ItemPedidoAdap();
        //Converte o objeto em um contetValue para inserir no banco
        ContentValues content = itemPedidoAdap.itemPedidoToContentValue(itemPedido,pedidoId);
        //Insere o itemPedido no banco
        Integer itemPedidoId = (int) dataBase.insert(ITEM_PEDIDO_TABLE_NAME, null, content);
        /*
        if(loginId <= 0){
            throw new Exceptions("Não foi possível inserir o usuário");
        }
        */
        itemPedido.setId(itemPedidoId);

        return itemPedido;

    }

    public void deletaItemPedidoProduto(Integer pedidoId, Integer produtoId){
        String sqlWhere = "PEDIDO_ID = " + pedidoId +
                          " AND PRODUTO_ID = " + produtoId;

        dataBase.delete(ITEM_PEDIDO_TABLE_NAME,sqlWhere,null);
    }
    
    public void truncateItems(){
        dataBase.delete(ITEM_PEDIDO_TABLE_NAME,null,null);
    }

    public void fecharConexao() {
        if(dataBase != null && dataBase.isOpen()) {
            dataBase.close();
        }
    }

}
