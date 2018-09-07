package lr.maisvendas.repositorio.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import lr.maisvendas.adaptadorModelo.CidadeAdap;
import lr.maisvendas.modelo.Cidade;
import lr.maisvendas.repositorio.DatabaseHelper;

public class CidadeDAO {
	
    private Context context;
    //private CidadeAdap cidadeAdap;
    private static final String CIDADE_TABLE_NAME = "tcidades";

    public static final String CIDADE_TABLE_CREATE = "CREATE TABLE if not exists " + CIDADE_TABLE_NAME + " ("
            + "ID INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "DESCRICAO TEXT NOT NULL, "
            + "SIGLA TEXT NOT NULL, "
            + "ESTADO_ID INTEGER NOT NULL);";

    public static final String SCRIPT_DELECAO_TABELA =  "DROP TABLE IF EXISTS " + CIDADE_TABLE_NAME;

    private SQLiteDatabase dataBase = null;

    private static CidadeDAO instance;

    public static CidadeDAO getInstance(Context context) {
        if(instance == null)
            instance = new CidadeDAO(context);
        return instance;
    }

    private CidadeDAO(Context context) {
        this.context = context;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        dataBase = databaseHelper.getWritableDatabase();
        //cidadeAdap = new CidadeAdap();
    }

    public Cidade buscaCidadeId(Integer cidadeId){
        Cidade cidade = null;

        //Busca o grupo
        String sql = "SELECT * FROM tcidades WHERE id = "+ cidadeId ;
        Cursor cursor = dataBase.rawQuery(sql, null);

        if (cursor != null && cursor.getCount() > 0 ){
            CidadeAdap cidadeAdap = new CidadeAdap();
            EstadoDAO estadoDAO = EstadoDAO.getInstance(context);

            while(cursor.moveToNext()) {
                //Converte o cursor em um objeto
                cidade = cidadeAdap.sqlToCidade(cursor);
                cidade.setEstado(estadoDAO.buscaEstadoId(cursor.getInt(cursor.getColumnIndex("ESTADO_ID"))));
            }
            cursor.close();
        }

        return cidade;

    }

    public Cidade insereCidade(Cidade cidade) {

        CidadeAdap cidadeAdap = new CidadeAdap();
        //Converte o objeto em um contetValue para inserir no banco
        ContentValues content = cidadeAdap.cidadeToContentValue(cidade);
        //Insere o cidade no banco
        Integer cidadeId = (int) dataBase.insert(CIDADE_TABLE_NAME, null, content);
        /*
        if(loginId <= 0){
            throw new Exceptions("Não foi possível inserir o usuário");
        }
        */
        cidade.setId(cidadeId);

        return cidade;

    }

    public void truncateCidades(){
        dataBase.delete(CIDADE_TABLE_NAME,null,null);
    }

    public void fecharConexao() {
        if(dataBase != null && dataBase.isOpen()) {
            dataBase.close();
        }
    }

}
