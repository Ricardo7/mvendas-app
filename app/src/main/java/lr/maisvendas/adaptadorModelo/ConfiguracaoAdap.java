package lr.maisvendas.adaptadorModelo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import lr.maisvendas.modelo.Configuracao;

public class ConfiguracaoAdap {

    private Context context;

    public void ConfiguracaoAdap(Context context){
        this.context = context;
    }

    public Configuracao sqlToConfiguracao(Cursor cursor){
        Configuracao configuracao = new Configuracao();

        /*
    private Integer id;
    private Boolean downloadImgWifi;
    private Boolean rastrearGps;
    private Boolean sincImg;
    private Boolean sincPedidos;
    private Boolean sincClientes;
    private Boolean sincProdutos;*/

        return configuracao;
    }

    public ContentValues configuracaoToContentValue(Configuracao configuracao){
        ContentValues content = new ContentValues();

        /*"ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "DOWNLOAD_IMG_WIFI INTEGER, " +
            "RASTREA_GPS INTEGER," +
            "SINC_IMG INTEGER," +
            "SINC_PEDIDOS INTEGER," +
            "SINC_CLIENTES INTEGER," +
            "SINC_PRODUTOS INTEGER)";*/

        return content;
    }
}
