package lr.maisvendas.repositorio.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import lr.maisvendas.adaptadorModelo.AtividadeAdap;
import lr.maisvendas.modelo.Atividade;
import lr.maisvendas.modelo.Cliente;
import lr.maisvendas.modelo.Usuario;
import lr.maisvendas.repositorio.DatabaseHelper;
import lr.maisvendas.utilitarios.Exceptions;

public class AtividadeDAO {
	
    private Context context;
	
    //private AtividadeAdap atividadeAdap;
    private static final String ATIVIDADE_TABLE_NAME = "tatividades";

    public static final String ATIVIDADE_TABLE_CREATE = "CREATE TABLE if not exists " + ATIVIDADE_TABLE_NAME + " (" +
            "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "ID_WS INTEGER, " +
            "ASSUNTO TEXT NOT NULL, " +
            "CLIENTE_ID INTEGER NOT NULL, " +
            "USUARIO_ID INTEGER NOT NULL, " +
            "OBSERVACAO TEXT," +
            "DATA_ATIVIDADE TEXT NOT NULL, " +
            "HORA_ATIVIDADE TEXT NOT NULL, " +
            "DT_CADASTRO TEXT, " +
            "DT_ATUALIZACAO TEXT);";

    public static final String SCRIPT_DELECAO_TABELA =  "DROP TABLE IF EXISTS " + ATIVIDADE_TABLE_NAME;

    private SQLiteDatabase dataBase = null;

    private static AtividadeDAO instance;

    public static AtividadeDAO getInstance(Context context) {
        if(instance == null)
            instance = new AtividadeDAO(context);
        return instance;
    }

    private AtividadeDAO(Context context) {
        this.context = context;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        dataBase = databaseHelper.getWritableDatabase();
        //atividadeAdap = new AtividadeAdap();
    }

    public Atividade buscaAtividadeId(Integer atividadeId){
        Atividade atividade = null;

        //Busca o grupo
        String sql = "SELECT * FROM tatividades WHERE id = "+ atividadeId ;
        Cursor cursor = dataBase.rawQuery(sql, null);

        if (cursor != null && cursor.getCount() > 0 ){
            AtividadeAdap atividadeAdap = new AtividadeAdap();
            ClienteDAO clienteDAO = ClienteDAO.getInstance(context);
            UsuarioDAO usuarioDAO = UsuarioDAO.getInstance(context);

            while(cursor.moveToNext()) {
                //Converte o cursor em um objeto
                atividade = atividadeAdap.sqlToAtividade(cursor);
                atividade.setCliente(clienteDAO.buscaClienteId(cursor.getInt(cursor.getColumnIndex("CLIENTE_ID"))));
                atividade.setUsuario(usuarioDAO.buscaUsuarioId(cursor.getInt(cursor.getColumnIndex("USUARIO_ID"))));
            }
            cursor.close();
        }

        return atividade;
    }

    public List<Atividade> buscaAtividadesDia(String dataAtividade){
        List<Atividade> atividades = new ArrayList<>();
        Atividade atividade = null;

        //Busca o grupo
        String sql = "SELECT * " +
                      " FROM tatividades " +
                     " WHERE data_atividade = ifnull('" + dataAtividade +"','1990-01-01')" +
                     " ORDER BY hora_atividade";
        Cursor cursor = dataBase.rawQuery(sql, null);

        if (cursor != null && cursor.getCount() > 0 ){
            AtividadeAdap atividadeAdap = new AtividadeAdap();
            ClienteDAO clienteDAO = ClienteDAO.getInstance(context);
            UsuarioDAO usuarioDAO = UsuarioDAO.getInstance(context);

            while(cursor.moveToNext()) {
                //Converte o cursor em um objeto
                atividade = atividadeAdap.sqlToAtividade(cursor);
                atividade.setCliente(clienteDAO.buscaClienteId(cursor.getInt(cursor.getColumnIndex("CLIENTE_ID"))));
                atividade.setUsuario(usuarioDAO.buscaUsuarioId(cursor.getInt(cursor.getColumnIndex("USUARIO_ID"))));

                atividades.add(atividade);
            }
            cursor.close();
        }

        return atividades;
    }

    public Atividade buscaAtividadeIdWs(String atividadeIdWs){
        Atividade atividade = null;

        //Busca o grupo
        String sql = "SELECT * FROM tatividades WHERE id_ws = '"+ atividadeIdWs +"'" ;
        Cursor cursor = dataBase.rawQuery(sql, null);

        if (cursor != null && cursor.getCount() > 0 ){
            AtividadeAdap atividadeAdap = new AtividadeAdap();
            ClienteDAO clienteDAO = ClienteDAO.getInstance(context);
            UsuarioDAO usuarioDAO = UsuarioDAO.getInstance(context);

            while(cursor.moveToNext()) {
                //Converte o cursor em um objeto
                atividade = atividadeAdap.sqlToAtividade(cursor);
                atividade.setCliente(clienteDAO.buscaClienteId(cursor.getInt(cursor.getColumnIndex("CLIENTE_ID"))));
                atividade.setUsuario(usuarioDAO.buscaUsuarioId(cursor.getInt(cursor.getColumnIndex("USUARIO_ID"))));
            }
            cursor.close();
        }

        return atividade;
    }
    
    public List<Atividade> buscaAtividadesData(String dataAt){

        List<Atividade> atividades = new ArrayList<>();
        Atividade atividade;

        //Busca a Atividade
        String sql = "SELECT * " +
                "  FROM tatividades " +
                " WHERE dt_atualizacao >= ifnull('" + dataAt +"','1990-01-01 00:00:00')";
        Cursor cursor = dataBase.rawQuery(sql, null);

        if (cursor != null && cursor.getCount() > 0 ){
            AtividadeAdap atividadeAdap = new AtividadeAdap();

            ClienteDAO clienteDAO = ClienteDAO.getInstance(context);
            UsuarioDAO usuarioDAO = UsuarioDAO.getInstance(context);

            while(cursor.moveToNext()) {
                //Converte o cursor em um objeto
                atividade = atividadeAdap.sqlToAtividade(cursor);
                atividade.setCliente(clienteDAO.buscaClienteId(cursor.getInt(cursor.getColumnIndex("CLIENTE_ID"))));
                atividade.setUsuario(usuarioDAO.buscaUsuarioId(cursor.getInt(cursor.getColumnIndex("USUARIO_ID"))));
            }

            cursor.close();
        }

        return atividades;
    }
    
    public Atividade buscaAtividadeDataHora(String data, String hora){
        Atividade atividade = null;

        //Busca o grupo
        String sql = "SELECT * " +
                     " FROM tatividades " +
                     " WHERE data_atividade = '"+ data +"'" +
                     "   AND hora_atividade = '"+ hora +"'" ;
        Cursor cursor = dataBase.rawQuery(sql, null);

        if (cursor != null && cursor.getCount() > 0 ){
            AtividadeAdap atividadeAdap = new AtividadeAdap();
            ClienteDAO clienteDAO = ClienteDAO.getInstance(context);
            UsuarioDAO usuarioDAO = UsuarioDAO.getInstance(context);

            while(cursor.moveToNext()) {
                //Converte o cursor em um objeto
                atividade = atividadeAdap.sqlToAtividade(cursor);
                atividade.setCliente(clienteDAO.buscaClienteId(cursor.getInt(cursor.getColumnIndex("CLIENTE_ID"))));
                atividade.setUsuario(usuarioDAO.buscaUsuarioId(cursor.getInt(cursor.getColumnIndex("USUARIO_ID"))));
            }
            cursor.close();
        }

        return atividade;
    }


    public Atividade insereAtividade(Atividade atividade) {

        trataDependencias(atividade);

        AtividadeAdap atividadeAdap = new AtividadeAdap();
        //Converte o objeto em um contetValue para inserir no banco
        ContentValues content = atividadeAdap.atividadeToContentValue(atividade);

        atividade.setId(null);
        //Insere o atividade no banco
        Integer atividadeId = (int) dataBase.insert(ATIVIDADE_TABLE_NAME, null, content);
        /*
        if(loginId <= 0){
            throw new Exceptions("Não foi possível inserir o usuário");
        }
        */
        atividade.setId(atividadeId);

        return atividade;

    }

    public Atividade atualizaAtividade(Atividade atividade) throws Exceptions {

        trataDependencias(atividade);
        AtividadeAdap atividadeAdap = new AtividadeAdap();
        //Converte o objeto em um contetValue para inserir no banco
        ContentValues content = atividadeAdap.atividadeToContentValue(atividade);
        String sqlWhere = "id = "+atividade.getId();
        //Insere o atividade no banco
        Integer executou = (int) dataBase.update(ATIVIDADE_TABLE_NAME, content,sqlWhere,null);

        if(executou <= 0){
            throw new Exceptions("Não foi possível atualizar a atividade (ID: "+atividade.getId()+")");
        }

        return atividade;

    }

    private Atividade trataDependencias(Atividade atividade){

        if (atividade.getCliente() == null || atividade.getCliente().getId() == null || atividade.getCliente().getId() <= 0) {
            ClienteDAO clienteDAO = ClienteDAO.getInstance(context);
            Cliente cliente;
            cliente = clienteDAO.buscaClienteIdWs(atividade.getCliente().getIdWS());
            atividade.setCliente(cliente);
        }

        if (atividade.getUsuario() == null || atividade.getUsuario().getId() == null || atividade.getUsuario().getId() <= 0) {
            UsuarioDAO usuarioDAO = UsuarioDAO.getInstance(context);
            Usuario usuario;
            usuario = usuarioDAO.buscaUsuarioIdWs(atividade.getUsuario().getIdWS());
            atividade.setUsuario(usuario);
        }

        return atividade;
    }

    public void truncateAtividades(){
        dataBase.delete(ATIVIDADE_TABLE_NAME,null,null);
    }

    public void fecharConexao() {
        if(dataBase != null && dataBase.isOpen()) {
            dataBase.close();
        }
    }

}
