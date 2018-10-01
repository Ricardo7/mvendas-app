package lr.maisvendas.adaptadorModelo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import lr.maisvendas.modelo.CondicaoPgto;

public class CondicaoPgtoAdap {

    private Context context;

    public void CondicaoPgtoAdap(Context context){
        this.context = context;
    }

    public CondicaoPgto sqlToCondicaoPgto(Cursor cursor){
        CondicaoPgto condicaoPgto = new CondicaoPgto();

        condicaoPgto.setId(cursor.getInt(cursor.getColumnIndex("ID")));
        condicaoPgto.setIdWS(cursor.getString(cursor.getColumnIndex("ID_WS")));
        condicaoPgto.setCod(cursor.getString(cursor.getColumnIndex("CODIGO")));
        condicaoPgto.setDescricao(cursor.getString(cursor.getColumnIndex("DESCRICAO")));
        condicaoPgto.setDescAcr(cursor.getDouble(cursor.getColumnIndex("DESC_ACR")));

        return condicaoPgto;
    }

    public ContentValues condicaoPgtoToContentValue(CondicaoPgto condicaoPgto){
        ContentValues content = new ContentValues();

        content.put("ID",condicaoPgto.getId());
        content.put("ID_WS",condicaoPgto.getIdWS());
        content.put("CODIGO",condicaoPgto.getCod());
        content.put("DESCRICAO",condicaoPgto.getDescricao());
        content.put("DESC_ACR",condicaoPgto.getDescAcr());

        return content;
    }
}
