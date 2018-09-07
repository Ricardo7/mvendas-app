package lr.maisvendas.repositorio.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import lr.maisvendas.adaptadorModelo.ClienteAdap;
import lr.maisvendas.modelo.Cliente;
import lr.maisvendas.repositorio.DatabaseHelper;
import lr.maisvendas.utilitarios.Ferramentas;

public class ClienteDAO {
	
    private Context context;
    //private ClienteAdap clienteAdap;
    private static final String CLIENTE_TABLE_NAME = "tclientes";

    public static final String CLIENTE_TABLE_CREATE = "CREATE TABLE if not exists " + CLIENTE_TABLE_NAME + " ("+
                    "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "CODIGO TEXT NOT NULL, " +
                    "CNPJ TEXT NOT NULL, " +
                    "RAZAO_SOCIAL TEXT NOT NULL, " +
                    "NOME_FAN TEXT," +
                    "INS_EST TEXT," +
                    "CID_ID INTEGER NOT NULL, " +
                    "BAIRRO TEXT NOT NULL,"+
                    "LOGRADOURO TEXT NOT NULL," +
                    "NUMERO INTEGER NOT NULL," +
                    "STATUS INTEGER," +
                    "ATIVO INTEGER," +
                    "SEGMER_ID INTEGER," +
                    "DT_CADASTRO TEXT," +
                    "DT_ATUALIZACAO TEXT);";

    public static final String SCRIPT_DELECAO_TABELA =  "DROP TABLE IF EXISTS " + CLIENTE_TABLE_NAME;

    private SQLiteDatabase dataBase = null;

    private static ClienteDAO instance;

    public static ClienteDAO getInstance(Context context) {
        if(instance == null)
            instance = new ClienteDAO(context);
        return instance;
    }

    private ClienteDAO(Context context) {
        this.context = context;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        dataBase = databaseHelper.getWritableDatabase();
        //clienteAdap = new ClienteAdap();
    }

    public Cliente buscaClienteId(Integer clienteId){
        Cliente cliente = null;

        //Busca o grupo
        String sql = "SELECT * FROM tclientes WHERE id = "+ clienteId ;
        Cursor cursor = dataBase.rawQuery(sql, null);

        if (cursor != null && cursor.getCount() > 0 ){
            ClienteAdap clienteAdap = new ClienteAdap();

            CidadeDAO cidadeDAO = CidadeDAO.getInstance(context);
            SegmentoMercadoDAO segmentoMercadoDAO = SegmentoMercadoDAO.getInstance(context);
            while(cursor.moveToNext()) {
                //Converte o cursor em um objeto
                cliente = clienteAdap.sqlToCliente(cursor);

                cliente.setCidade(cidadeDAO.buscaCidadeId(cursor.getInt(cursor.getColumnIndex("CID_ID"))));
                cliente.setSegmentoMercado(segmentoMercadoDAO.buscaSegmentoMercadoId(cursor.getInt(cursor.getColumnIndex("SEGMER_ID"))));
            }
            cursor.close();
        }

        return cliente;
    }
    
    public List<Cliente> buscaClientes(){
        List<Cliente> clientes = new ArrayList<>();
        Cliente cliente;
        //Busca o usuario
        String sql = "SELECT * " +
                "  FROM tclientes ";

        Cursor cursor = dataBase.rawQuery(sql, null);

        if (cursor != null && cursor.getCount() > 0 ){
            ClienteAdap clienteAdap = new ClienteAdap();

            CidadeDAO cidadeDAO = CidadeDAO.getInstance(context);
            SegmentoMercadoDAO segmentoMercadoDAO = SegmentoMercadoDAO.getInstance(context);

            while(cursor.moveToNext()) {
                //Converte o cursor em um objeto
                cliente = clienteAdap.sqlToCliente(cursor);
                Ferramentas ferramentas = new Ferramentas();
                cliente.setCidade(cidadeDAO.buscaCidadeId(cursor.getInt(cursor.getColumnIndex("CID_ID"))));
                cliente.setSegmentoMercado(segmentoMercadoDAO.buscaSegmentoMercadoId(cursor.getInt(cursor.getColumnIndex("SEGMER_ID"))));
                clientes.add(cliente);
            }
            cursor.close();
        }

        return clientes;

    }

    public Cliente insereCliente(Cliente cliente) {

        ClienteAdap clienteAdap = new ClienteAdap();
        //Converte o objeto em um contetValue para inserir no banco
        ContentValues content = clienteAdap.clienteToContentValue(cliente);
        //Insere o cliente no banco
        Integer clienteId = (int) dataBase.insert(CLIENTE_TABLE_NAME, null, content);
        /*
        if(loginId <= 0){
            throw new Exceptions("Não foi possível inserir o usuário");
        }
        */
        cliente.setId(clienteId);

        return cliente;

    }    

    public void truncateClientes(){
        dataBase.delete(CLIENTE_TABLE_NAME,null,null);
    }

    public void fecharConexao() {
        if(dataBase != null && dataBase.isOpen()) {
            dataBase.close();
        }
    }

}
