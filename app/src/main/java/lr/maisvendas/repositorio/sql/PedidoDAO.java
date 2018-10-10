package lr.maisvendas.repositorio.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import lr.maisvendas.adaptadorModelo.PedidoAdap;
import lr.maisvendas.modelo.Cliente;
import lr.maisvendas.modelo.CondicaoPagamento;
import lr.maisvendas.modelo.Pedido;
import lr.maisvendas.modelo.TabelaPreco;
import lr.maisvendas.repositorio.DatabaseHelper;
import lr.maisvendas.utilitarios.Exceptions;

public class PedidoDAO {
	
    private Context context;
	
    //private PedidoAdap pedidoAdap;
    private static final String PEDIDO_TABLE_NAME = "tpedidos";

    public static final String PEDIDO_TABLE_CREATE = "CREATE TABLE if not exists " + PEDIDO_TABLE_NAME + " (" +
            "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "ID_WS TEXT, " +
            "NUMERO INTEGER, " +
            "SITUACAO INTEGER, " +
            "STATUS INTEGER, " +
            "OBSERVACAO TEXT, " +
            "CLIENTE_ID INTEGER, " +
            "CONDICAO_PGTO_ID INTEGER, " +
            "TABELA_PRECO_ID INTEGER, " +
            "DT_CRIACAO TEXT, " +
            "DT_ATUALIZACAO TEXT);";

    public static final String SCRIPT_DELECAO_TABELA =  "DROP TABLE IF EXISTS " + PEDIDO_TABLE_NAME;

    private SQLiteDatabase dataBase = null;

    private static PedidoDAO instance;

    public static PedidoDAO getInstance(Context context) {
        if(instance == null)
            instance = new PedidoDAO(context);
        return instance;
    }

    private PedidoDAO(Context context) {
        this.context = context;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        dataBase = databaseHelper.getWritableDatabase();
        //pedidoAdap = new PedidoAdap();
    }

    public Pedido buscaPedidoIdWs(String pedidoIdWs){
        Pedido pedido = null;

        //Busca o grupo
        String sql = "SELECT * FROM tpedidos WHERE id_ws = '"+ pedidoIdWs +"'";
        Cursor cursor = dataBase.rawQuery(sql, null);

        if (cursor != null && cursor.getCount() > 0 ){
            
            PedidoAdap pedidoAdap = new PedidoAdap();
            ItemPedidoDAO itemPedidoDAO = ItemPedidoDAO.getInstance(context);
            ClienteDAO clienteDAO = ClienteDAO.getInstance(context);
            CondicaoPgtoDAO condicaoPgtoDAO = CondicaoPgtoDAO.getInstance(context);
            TabelaPrecoDAO tabelaPrecoDAO = TabelaPrecoDAO.getInstance(context);

            while(cursor.moveToNext()) {
                //Converte o cursor em um objeto
                pedido = pedidoAdap.sqlToPedido(cursor);
                pedido.setItensPedido(itemPedidoDAO.buscaItemPedido(cursor.getInt(cursor.getColumnIndex("ID"))));
                pedido.setCliente(clienteDAO.buscaClienteId(cursor.getInt(cursor.getColumnIndex("CLIENTE_ID"))));
                pedido.setCondicaoPagamento(condicaoPgtoDAO.buscaCondicaoPgtoId(cursor.getInt(cursor.getColumnIndex("CONDICAO_PGTO_ID"))));
                pedido.setTabelaPreco(tabelaPrecoDAO.buscaTabelaPrecoId(cursor.getInt(cursor.getColumnIndex("TABELA_PRECO_ID"))));
            }
            cursor.close();
        }

        return pedido;
    }

    public List<Pedido> buscaPedidosData(String dataAt){
        List<Pedido> pedidos = new ArrayList<>();
        Pedido pedido;

        //Busca o pedido
        String sql = "SELECT * " +
                "  FROM tpedidos " +
                " WHERE dt_atualizacao >= ifnull('" + dataAt +"','1990-01-01 00:00:00')";

        Cursor cursor = dataBase.rawQuery(sql, null);

        if (cursor != null && cursor.getCount() > 0 ){

            PedidoAdap pedidoAdap = new PedidoAdap();
            ItemPedidoDAO itemPedidoDAO = ItemPedidoDAO.getInstance(context);
            ClienteDAO clienteDAO = ClienteDAO.getInstance(context);
            CondicaoPgtoDAO condicaoPgtoDAO = CondicaoPgtoDAO.getInstance(context);
            TabelaPrecoDAO tabelaPrecoDAO = TabelaPrecoDAO.getInstance(context);

            while(cursor.moveToNext()) {
                //Converte o cursor em um objeto
                pedido = pedidoAdap.sqlToPedido(cursor);
                pedido.setItensPedido(itemPedidoDAO.buscaItemPedido(cursor.getInt(cursor.getColumnIndex("ID"))));
                pedido.setCliente(clienteDAO.buscaClienteId(cursor.getInt(cursor.getColumnIndex("CLIENTE_ID"))));
                pedido.setCondicaoPagamento(condicaoPgtoDAO.buscaCondicaoPgtoId(cursor.getInt(cursor.getColumnIndex("CONDICAO_PGTO_ID"))));
                pedido.setTabelaPreco(tabelaPrecoDAO.buscaTabelaPrecoId(cursor.getInt(cursor.getColumnIndex("TABELA_PRECO_ID"))));

                pedidos.add(pedido);
            }
            cursor.close();
        }

        return pedidos;
    }

    public Pedido buscaPedidoStatusProduto(Integer statusPedido, Integer produtoId){
        Pedido pedido = null;
        String sql = "SELECT pd.* " +
                     " FROM tpedidos pd" +
                     "         INNER JOIN titens_pedido itpd ON pd.id = itpd.pedido_id" +
                     "         INNER JOIN tprodutos     prd  ON prd.id = itpd.produto_id" +
                     " WHERE pd.status = "+statusPedido +
                     "   AND prd.id    = "+produtoId;

        Cursor cursor = dataBase.rawQuery(sql, null);

        if (cursor != null && cursor.getCount() > 0 ){
            PedidoAdap pedidoAdap = new PedidoAdap();
            ItemPedidoDAO itemPedidoDAO = ItemPedidoDAO.getInstance(context);
            ClienteDAO clienteDAO = ClienteDAO.getInstance(context);
            CondicaoPgtoDAO condicaoPgtoDAO = CondicaoPgtoDAO.getInstance(context);
            TabelaPrecoDAO tabelaPrecoDAO = TabelaPrecoDAO.getInstance(context);

            while(cursor.moveToNext()) {
                //Converte o cursor em um objeto
                pedido = pedidoAdap.sqlToPedido(cursor);
                pedido.setItensPedido(itemPedidoDAO.buscaItemPedido(cursor.getInt(cursor.getColumnIndex("ID"))));
                pedido.setCliente(clienteDAO.buscaClienteId(cursor.getInt(cursor.getColumnIndex("CLIENTE_ID"))));
                pedido.setCondicaoPagamento(condicaoPgtoDAO.buscaCondicaoPgtoId(cursor.getInt(cursor.getColumnIndex("CONDICAO_PGTO_ID"))));
                pedido.setTabelaPreco(tabelaPrecoDAO.buscaTabelaPrecoId(cursor.getInt(cursor.getColumnIndex("TABELA_PRECO_ID"))));

            }
            cursor.close();
        }

        return pedido;
    }

    public Pedido buscaPedidoStatus(Integer statusPedido){
        Pedido pedido = null;
        String sql = "SELECT pd.* " +
                     " FROM tpedidos pd" +
                     " WHERE pd.status = "+statusPedido;

        Cursor cursor = dataBase.rawQuery(sql, null);

        if (cursor != null && cursor.getCount() > 0 ){
            PedidoAdap pedidoAdap = new PedidoAdap();
            ItemPedidoDAO itemPedidoDAO = ItemPedidoDAO.getInstance(context);
            ClienteDAO clienteDAO = ClienteDAO.getInstance(context);
            CondicaoPgtoDAO condicaoPgtoDAO = CondicaoPgtoDAO.getInstance(context);
            TabelaPrecoDAO tabelaPrecoDAO = TabelaPrecoDAO.getInstance(context);

            while(cursor.moveToNext()) {
                //Converte o cursor em um objeto
                pedido = pedidoAdap.sqlToPedido(cursor);
                pedido.setItensPedido(itemPedidoDAO.buscaItemPedido(cursor.getInt(cursor.getColumnIndex("ID"))));
                pedido.setCliente(clienteDAO.buscaClienteId(cursor.getInt(cursor.getColumnIndex("CLIENTE_ID"))));
                pedido.setCondicaoPagamento(condicaoPgtoDAO.buscaCondicaoPgtoId(cursor.getInt(cursor.getColumnIndex("CONDICAO_PGTO_ID"))));
                pedido.setTabelaPreco(tabelaPrecoDAO.buscaTabelaPrecoId(cursor.getInt(cursor.getColumnIndex("TABELA_PRECO_ID"))));

            }
            cursor.close();
        }

        return pedido;
    }

    public List<Pedido> buscaPedidos(){
        List<Pedido> pedidos = new ArrayList<>();
        Pedido pedido;
        //Busca o pedido
        String sql = "SELECT * " +
                " FROM tpedidos ";

        Cursor cursor = dataBase.rawQuery(sql, null);

        if (cursor != null && cursor.getCount() > 0 ){
            PedidoAdap pedidoAdap = new PedidoAdap();
            ItemPedidoDAO itemPedidoDAO = ItemPedidoDAO.getInstance(context);
            ClienteDAO clienteDAO = ClienteDAO.getInstance(context);
            CondicaoPgtoDAO condicaoPgtoDAO = CondicaoPgtoDAO.getInstance(context);
            TabelaPrecoDAO tabelaPrecoDAO = TabelaPrecoDAO.getInstance(context);

            while(cursor.moveToNext()) {
                //Converte o cursor em um objeto
                pedido = pedidoAdap.sqlToPedido(cursor);
                pedido.setItensPedido(itemPedidoDAO.buscaItemPedido(cursor.getInt(cursor.getColumnIndex("ID"))));
                pedido.setCliente(clienteDAO.buscaClienteId(cursor.getInt(cursor.getColumnIndex("CLIENTE_ID"))));
                pedido.setCondicaoPagamento(condicaoPgtoDAO.buscaCondicaoPgtoId(cursor.getInt(cursor.getColumnIndex("CONDICAO_PGTO_ID"))));
                pedido.setTabelaPreco(tabelaPrecoDAO.buscaTabelaPrecoId(cursor.getInt(cursor.getColumnIndex("TABELA_PRECO_ID"))));

                pedidos.add(pedido);
            }
            cursor.close();
        }

        return pedidos;

    }

    public Pedido inserePedido(Pedido pedido) {

        //Na inserção o ID(IDAP) do aplicativo deve ser limpo, para que o ID seja gerado e controlado somente pelo dispositivo
        pedido.setId(null);

        pedido = trataDependentes(pedido);

        PedidoAdap pedidoAdap = new PedidoAdap();
        //Converte o objeto em um contetValue para inserir no banco
        ContentValues content = pedidoAdap.pedidoToContentValue(pedido);

        //Insere o pedido no banco
        Integer pedidoId = (int) dataBase.insert(PEDIDO_TABLE_NAME, null, content);

        /*
        if(loginId <= 0){
            throw new Exceptions("Não foi possível inserir o usuário");
        }
        */
        pedido.setId(pedidoId);

        return pedido;

    }

    public Pedido atualizaPedido(Pedido pedido) throws Exceptions {

        pedido = trataDependentes(pedido);

        PedidoAdap pedidoAdap = new PedidoAdap();
        //Converte o objeto em um contetValue para inserir no banco
        ContentValues content = pedidoAdap.pedidoToContentValue(pedido);
        String sqlWhere = "id = "+pedido.getId();

        //Insere o pedido no banco
        Integer executou = (int) dataBase.update(PEDIDO_TABLE_NAME,content,sqlWhere,null);

        if(executou <= 0){
            throw new Exceptions("Não foi possível atualizar o pedido (ID="+pedido.getId()+")");
        }

        return pedido;

    }

    public void deletaPedido(Integer pedidoId) throws Exceptions{

        //Deleta os itens do pedido
        ItemPedidoDAO itemPedidoDAO = ItemPedidoDAO.getInstance(context);
        itemPedidoDAO.deletaItemPedido(pedidoId);

        String sqlWhere = "id = "+pedidoId;

        Integer executou = (int) dataBase.delete(PEDIDO_TABLE_NAME,sqlWhere,null);

        if(executou <= 0){
            throw new Exceptions("Não foi possível deletar o pedido");
        }
    }

    private Pedido trataDependentes(Pedido pedido) {

        if (pedido.getCliente() == null || pedido.getCliente().getId() == null || pedido.getCliente().getId() <= 0) {
            ClienteDAO clienteDAO = ClienteDAO.getInstance(context);
            Cliente cliente;
            cliente = clienteDAO.buscaClienteIdWs(pedido.getCliente().getIdWS());
            pedido.setCliente(cliente);
        }

        if (pedido.getCondicaoPagamento() == null || pedido.getCondicaoPagamento().getId() == null || pedido.getCondicaoPagamento().getId() <= 0) {
            CondicaoPgtoDAO condicaoPgtoDAO = CondicaoPgtoDAO.getInstance(context);
            CondicaoPagamento condicaoPagamento;
            condicaoPagamento = condicaoPgtoDAO.buscaCondicaoPgtoIdWs(pedido.getCondicaoPagamento().getIdWS());
            pedido.setCondicaoPagamento(condicaoPagamento);
        }

        if (pedido.getTabelaPreco() == null || pedido.getTabelaPreco().getId() == null || pedido.getTabelaPreco().getId() <= 0) {
            TabelaPrecoDAO tabelaPrecoDAO = TabelaPrecoDAO.getInstance(context);
            TabelaPreco tabelaPreco;
            tabelaPreco = tabelaPrecoDAO.buscaTabelaPrecoIdWs(pedido.getTabelaPreco().getIdWS());

            pedido.setTabelaPreco(tabelaPreco);
        }

        return pedido;
    }

    public void truncatePedidos(){
        dataBase.delete(PEDIDO_TABLE_NAME,null,null);
    }

    public void fecharConexao() {
        if(dataBase != null && dataBase.isOpen()) {
            dataBase.close();
        }
    }

}
