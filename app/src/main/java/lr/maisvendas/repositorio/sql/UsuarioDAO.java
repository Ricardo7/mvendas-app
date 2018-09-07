package lr.maisvendas.repositorio.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import lr.maisvendas.adaptadorModelo.UsuarioAdap;
import lr.maisvendas.modelo.Usuario;
import lr.maisvendas.repositorio.DatabaseHelper;
import lr.maisvendas.utilitarios.Exceptions;

public class UsuarioDAO {
	
    private Context context;
    private UsuarioAdap usuarioAdap;
    private static final String USUARIO_TABLE_NAME = "tusuarios";

    public static final String USUARIO_TABLE_CREATE = "CREATE TABLE if not exists " + USUARIO_TABLE_NAME + " (" +
            "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "NOME TEXT NOT NULL, " +
            "EMAIL TEXT NOT NULL, " +
            "SENHA TEXT NOT NULL, " +
            "ATIVO INTEGER NOT NULL," +
            "TOKEN TEXT);";

    public static final String SCRIPT_DELECAO_TABELA =  "DROP TABLE IF EXISTS " + USUARIO_TABLE_NAME;

    private SQLiteDatabase dataBase = null;

    private static UsuarioDAO instance;

    public static UsuarioDAO getInstance(Context context) {
        if(instance == null)
            instance = new UsuarioDAO(context);
        return instance;
    }

    private UsuarioDAO(Context context) {
        this.context = context;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        dataBase = databaseHelper.getWritableDatabase();
        usuarioAdap = new UsuarioAdap();
    }

    public Usuario buscaUsuarioLoginToken() {
        Usuario usuario = null;

        //Busca o login
        String sql = "SELECT * FROM tusuarios LIMIT 1";
        Cursor cursor = dataBase.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            //Converte o cursor em um objeto
            UsuarioAdap usuarioAdap = new UsuarioAdap();
            usuario = usuarioAdap.sqlToUsuario(cursor);
        }

        return usuario;
    }

    public void insereUsuario(Usuario usuario) {

        UsuarioAdap usuarioAdap = new UsuarioAdap();
        //Converte o objeto em um contetValue para inserir no banco
        ContentValues content = usuarioAdap.usuarioToContentValue(usuario);
        //Insere o usuario no banco
        Integer loginId = (int) dataBase.insert(USUARIO_TABLE_NAME, null, content);
        /*
        if(loginId <= 0){
            throw new Exceptions("Não foi possível inserir o usuário");
        }
        */

    }

    public void atualizaUsuario(Usuario usuario) throws Exceptions {


        UsuarioAdap usuarioAdap = new UsuarioAdap();
        ContentValues contentValues = usuarioAdap.usuarioToContentValue(usuario);

        Integer usuarioId = usuario.getId();
        String sqlWhere = "id = "+usuarioId ;

        int executou = dataBase.update(USUARIO_TABLE_NAME, contentValues, sqlWhere, null);

        if (executou <= 0){
            throw new Exceptions("Não foi possível atualizar o Usuário ID "+usuarioId);
        }

    }

    public long deletaUsuario(Usuario usuario) throws Exceptions{

        String sqlWhere = "id = " + usuario.getId();
        long loginId = dataBase.delete(USUARIO_TABLE_NAME, sqlWhere, null);
        return loginId;
    }

    public void truncateUsuarios(){
        dataBase.delete(USUARIO_TABLE_NAME,null,null);
    }

    public void fecharConexao() {
        if(dataBase != null && dataBase.isOpen()) {
            dataBase.close();
        }
    }

}
