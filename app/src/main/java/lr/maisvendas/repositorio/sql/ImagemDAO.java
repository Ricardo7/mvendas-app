package lr.maisvendas.repositorio.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import lr.maisvendas.adaptadorModelo.ImagemAdap;
import lr.maisvendas.modelo.Imagem;
import lr.maisvendas.repositorio.DatabaseHelper;

public class ImagemDAO {
	
    private Context context;
	
    //private TabelaAdap tabelaAdap;
    private static final String IMAGEM_TABLE_NAME = "timagens";

    public static final String IMAGEM_TABLE_CREATE = "CREATE TABLE if not exists " + IMAGEM_TABLE_NAME + " (" +
            "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "PRODUTO_ID INTEGER, " +
            "NOME TEXT," +
            "PRINCIPAL INTEGER," +
            "CAMINHO TEXT," +
            "TAMANHO REAL," +
            "DT_CRIACAO TEXT," +
            "DT_ATUALIZACAO TEXT);";

    public static final String SCRIPT_DELECAO_TABELA =  "DROP TABLE IF EXISTS " + IMAGEM_TABLE_NAME;

    private SQLiteDatabase dataBase = null;

    private static ImagemDAO instance;

    public static ImagemDAO getInstance(Context context) {
        if(instance == null)
            instance = new ImagemDAO(context);
        return instance;
    }

    private ImagemDAO(Context context) {
        this.context = context;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        dataBase = databaseHelper.getWritableDatabase();
        //tabelaAdap = new TabelaAdap();
    }

    public List<Imagem> buscaImagensProduto(Integer produtoId){
        List<Imagem> imagens = new ArrayList<>();
        Imagem imagem = null;

        //Busca o grupo
        String sql = "SELECT * FROM timagens WHERE produto_id = "+ produtoId ;
        Cursor cursor = dataBase.rawQuery(sql, null);

        if (cursor != null && cursor.getCount() > 0 ){
            ImagemAdap imagemAdap = new ImagemAdap();
            PaisDAO paisDAO = PaisDAO.getInstance(context);

            while(cursor.moveToNext()) {
                //Converte o cursor em um objeto
                imagem = imagemAdap.sqlToImagem(cursor);

                imagens.add(imagem);
            }
            cursor.close();
        }

        return imagens;
    }

    public Imagem insereImagem(Imagem imagem, Integer produtoId) {

        ImagemAdap imagemAdap = new ImagemAdap();
        //Converte o objeto em um contetValue para inserir no banco
        ContentValues content = imagemAdap.imagemToContentValue(imagem,produtoId);
        //Insere o imagem no banco
        Integer imagemId = (int) dataBase.insert(IMAGEM_TABLE_NAME, null, content);
        /*
        if(loginId <= 0){
            throw new Exceptions("Não foi possível inserir o usuário");
        }
        */
        imagem.setId(imagemId);

        return imagem;

    }

    public void truncateTabelas(){
        dataBase.delete(IMAGEM_TABLE_NAME,null,null);
    }

    public void fecharConexao() {
        if(dataBase != null && dataBase.isOpen()) {
            dataBase.close();
        }
    }

}
