package lr.maisvendas.adaptadorModelo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import lr.maisvendas.modelo.Produto;

public class ProdutoAdap {

    private Context context;

    public void ProdutoAdap(Context context){
        this.context = context;
    }

    public Produto sqlToProduto(Cursor cursor){
        Produto produto = new Produto();

        produto.setId(cursor.getInt(cursor.getColumnIndex("ID")));
        produto.setCod(cursor.getString(cursor.getColumnIndex("CODIGO")));
        produto.setDescricao(cursor.getString(cursor.getColumnIndex("DESCRICAO")));
        produto.setObservacao(cursor.getString(cursor.getColumnIndex("OBSERVACAO")));

        return produto;
    }

    public ContentValues produtoToContentValue(Produto produto){
        ContentValues content = new ContentValues();

        content.put("ID",produto.getId());
        content.put("CODIGO",produto.getCod());
        content.put("DESCRICAO",produto.getDescricao());
        content.put("OBSERVACAO",produto.getObservacao());

        return content;
    }
}
