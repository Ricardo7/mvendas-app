package lr.maisvendas.adaptadorModelo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import lr.maisvendas.modelo.TabelaPreco;

public class TabelaPrecoAdap {

    private Context context;

    public void TabelaPrecoAdap(Context context){
        this.context = context;
    }

    public TabelaPreco sqlToTabelaPreco(Cursor cursor){
        TabelaPreco tabelaPreco = new TabelaPreco();

        tabelaPreco.setId(cursor.getInt(cursor.getColumnIndex("ID")));
        tabelaPreco.setIdWS(cursor.getString(cursor.getColumnIndex("ID_WS")));
        tabelaPreco.setCod(cursor.getString(cursor.getColumnIndex("CODIGO")));
        tabelaPreco.setDescricao(cursor.getString(cursor.getColumnIndex("DESCRICAO")));

        return tabelaPreco;
    }

    public ContentValues tabelaPrecoToContentValue(TabelaPreco tabelaPreco){
        ContentValues content = new ContentValues();

        content.put("ID",tabelaPreco.getId());
        content.put("ID_WS",tabelaPreco.getIdWS());
        content.put("CODIGO",tabelaPreco.getCod());
        content.put("DESCRICAO",tabelaPreco.getDescricao());

        return content;
    }
}
