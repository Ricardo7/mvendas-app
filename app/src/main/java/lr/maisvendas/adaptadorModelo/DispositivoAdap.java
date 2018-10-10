package lr.maisvendas.adaptadorModelo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import lr.maisvendas.modelo.Dispositivo;

public class DispositivoAdap {

    private Context context;

    public void DispositivoAdap(Context context){
        this.context = context;
    }

    public Dispositivo sqlToDispositivo(Cursor cursor){
        Dispositivo dispositivo = new Dispositivo();

        dispositivo.setId(cursor.getInt(cursor.getColumnIndex("ID")));
        dispositivo.setDataSincImagens(cursor.getString(cursor.getColumnIndex("DATA_SINC_IMAGENS")));
        dispositivo.setDataSincPedidos(cursor.getString(cursor.getColumnIndex("DATA_SINC_PEDIDOS")));
        dispositivo.setDataSincClientes(cursor.getString(cursor.getColumnIndex("DATA_SINC_CLIENTES")));
        dispositivo.setDataSincProdutos(cursor.getString(cursor.getColumnIndex("DATA_SINC_PRODUTOS")));
        dispositivo.setDataSincAtividades(cursor.getString(cursor.getColumnIndex("DATA_SINC_ATIVIDADES")));

        return dispositivo;
    }

    public ContentValues dispositivoToContentValue(Dispositivo dispositivo){
        ContentValues content = new ContentValues();

        content.put("ID",dispositivo.getId());
        content.put("DATA_SINC_IMAGENS",dispositivo.getDataSincImagens());
        content.put("DATA_SINC_PEDIDOS",dispositivo.getDataSincPedidos());
        content.put("DATA_SINC_CLIENTES",dispositivo.getDataSincClientes());
        content.put("DATA_SINC_PRODUTOS",dispositivo.getDataSincProdutos());
        content.put("DATA_SINC_ATIVIDADES",dispositivo.getDataSincAtividades());

        return content;
    }
}
