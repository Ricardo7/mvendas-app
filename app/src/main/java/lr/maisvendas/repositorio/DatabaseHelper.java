package lr.maisvendas.repositorio;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import lr.maisvendas.repositorio.sql.AtividadeDAO;
import lr.maisvendas.repositorio.sql.CidadeDAO;
import lr.maisvendas.repositorio.sql.ClienteDAO;
import lr.maisvendas.repositorio.sql.CondicaoPgtoDAO;
import lr.maisvendas.repositorio.sql.ConfiguracaoDAO;
import lr.maisvendas.repositorio.sql.DispositivoDAO;
import lr.maisvendas.repositorio.sql.EstadoDAO;
import lr.maisvendas.repositorio.sql.ImagemDAO;
import lr.maisvendas.repositorio.sql.ItemPedidoDAO;
import lr.maisvendas.repositorio.sql.ItemTabelaPrecoDAO;
import lr.maisvendas.repositorio.sql.LocalDAO;
import lr.maisvendas.repositorio.sql.MetaDAO;
import lr.maisvendas.repositorio.sql.NotificacaoDAO;
import lr.maisvendas.repositorio.sql.PaisDAO;
import lr.maisvendas.repositorio.sql.PedidoDAO;
import lr.maisvendas.repositorio.sql.ProdutoDAO;
import lr.maisvendas.repositorio.sql.SegmentoMercadoDAO;
import lr.maisvendas.repositorio.sql.TabelaPrecoDAO;
import lr.maisvendas.repositorio.sql.UsuarioDAO;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String NOME_BANCO =  "maisVendas.db";
	
    public static final int VERSAO =4;

    private static DatabaseHelper instance;

    public DatabaseHelper(Context context) {
        super(context, NOME_BANCO, null, VERSAO);
    }

    public static DatabaseHelper getInstance(Context context) {
        if(instance == null)
            instance = new DatabaseHelper(context);

        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(UsuarioDAO.USUARIO_TABLE_CREATE);
        db.execSQL(AtividadeDAO.ATIVIDADE_TABLE_CREATE);
        db.execSQL(CidadeDAO.CIDADE_TABLE_CREATE);
        db.execSQL(ClienteDAO.CLIENTE_TABLE_CREATE);
        db.execSQL(CondicaoPgtoDAO.CONDICAO_PGTO_TABLE_CREATE);
        db.execSQL(ConfiguracaoDAO.CONFIGURACAO_TABLE_CREATE);
        db.execSQL(EstadoDAO.ESTADO_TABLE_CREATE);
        db.execSQL(ImagemDAO.IMAGEM_TABLE_CREATE);
        db.execSQL(ItemTabelaPrecoDAO.ITEM_TABELA_PRECO_TABLE_CREATE);
        db.execSQL(LocalDAO.LOCAL_TABLE_CREATE);
        db.execSQL(MetaDAO.META_TABLE_CREATE);
        db.execSQL(NotificacaoDAO.NOTIFICACAO_TABLE_CREATE);
        db.execSQL(PaisDAO.PAIS_TABLE_CREATE);
        db.execSQL(PedidoDAO.PEDIDO_TABLE_CREATE);
        db.execSQL(ItemPedidoDAO.ITEM_PEDIDO_TABLE_CREATE);
        db.execSQL(ProdutoDAO.PRODUTO_TABLE_CREATE);
        db.execSQL(SegmentoMercadoDAO.SEGMENTO_MERCADO_TABLE_CREATE);
        db.execSQL(TabelaPrecoDAO.TABELA_PRECOS_TABLE_CREATE);
        db.execSQL(DispositivoDAO.DISPOSITIVO_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(UsuarioDAO.SCRIPT_DELECAO_TABELA);
        db.execSQL(AtividadeDAO.SCRIPT_DELECAO_TABELA);
        db.execSQL(CidadeDAO.SCRIPT_DELECAO_TABELA);
        db.execSQL(ClienteDAO.SCRIPT_DELECAO_TABELA);
        db.execSQL(CondicaoPgtoDAO.SCRIPT_DELECAO_TABELA);
        db.execSQL(ConfiguracaoDAO.SCRIPT_DELECAO_TABELA);
        db.execSQL(EstadoDAO.SCRIPT_DELECAO_TABELA);
        db.execSQL(ImagemDAO.SCRIPT_DELECAO_TABELA);
        db.execSQL(ItemTabelaPrecoDAO.SCRIPT_DELECAO_TABELA);
        db.execSQL(LocalDAO.SCRIPT_DELECAO_TABELA);
        db.execSQL(MetaDAO.SCRIPT_DELECAO_TABELA);
        db.execSQL(NotificacaoDAO.SCRIPT_DELECAO_TABELA);
        db.execSQL(PaisDAO.SCRIPT_DELECAO_TABELA);
        db.execSQL(PedidoDAO.SCRIPT_DELECAO_TABELA);
        db.execSQL(ItemPedidoDAO.SCRIPT_DELECAO_TABELA);
        db.execSQL(ProdutoDAO.SCRIPT_DELECAO_TABELA);
        db.execSQL(SegmentoMercadoDAO.SCRIPT_DELECAO_TABELA);
        db.execSQL(TabelaPrecoDAO.SCRIPT_DELECAO_TABELA);
        db.execSQL(DispositivoDAO.SCRIPT_DELECAO_TABELA);

        onCreate(db);
    }
}
