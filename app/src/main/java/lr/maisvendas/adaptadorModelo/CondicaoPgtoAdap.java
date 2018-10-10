package lr.maisvendas.adaptadorModelo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import lr.maisvendas.modelo.CondicaoPagamento;

public class CondicaoPgtoAdap {

    private Context context;

    public void CondicaoPgtoAdap(Context context){
        this.context = context;
    }

    public CondicaoPagamento sqlToCondicaoPgto(Cursor cursor){
        CondicaoPagamento condicaoPagamento = new CondicaoPagamento();

        condicaoPagamento.setId(cursor.getInt(cursor.getColumnIndex("ID")));
        condicaoPagamento.setIdWS(cursor.getString(cursor.getColumnIndex("ID_WS")));
        condicaoPagamento.setCod(cursor.getString(cursor.getColumnIndex("CODIGO")));
        condicaoPagamento.setDescricao(cursor.getString(cursor.getColumnIndex("DESCRICAO")));
        condicaoPagamento.setDescAcr(cursor.getDouble(cursor.getColumnIndex("DESC_ACR")));

        return condicaoPagamento;
    }

    public ContentValues condicaoPgtoToContentValue(CondicaoPagamento condicaoPagamento){
        ContentValues content = new ContentValues();

        content.put("ID", condicaoPagamento.getId());
        content.put("ID_WS", condicaoPagamento.getIdWS());
        content.put("CODIGO", condicaoPagamento.getCod());
        content.put("DESCRICAO", condicaoPagamento.getDescricao());
        content.put("DESC_ACR", condicaoPagamento.getDescAcr());

        return content;
    }
}
