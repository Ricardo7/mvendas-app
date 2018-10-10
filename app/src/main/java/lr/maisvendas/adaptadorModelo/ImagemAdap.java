package lr.maisvendas.adaptadorModelo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import lr.maisvendas.modelo.Imagem;

public class ImagemAdap {

    private Context context;

    public void ImagemAdap(Context context){
        this.context = context;
    }

    public Imagem sqlToImagem(Cursor cursor){
        Imagem imagem = new Imagem();

        imagem.setId(cursor.getInt(cursor.getColumnIndex("ID")));
        imagem.setIdWS(cursor.getString(cursor.getColumnIndex("ID_WS")));
        imagem.setNome(cursor.getString(cursor.getColumnIndex("NOME")));
        imagem.setPrincipal(cursor.getInt(cursor.getColumnIndex("PRINCIPAL")));
        imagem.setCaminho(cursor.getString(cursor.getColumnIndex("CAMINHO")));
        imagem.setProdutoIdWS(cursor.getString(cursor.getColumnIndex("PRODUTO_ID_WS")));
        imagem.setDtCriacao(cursor.getString(cursor.getColumnIndex("DT_CRIACAO")));
        imagem.setDtAtualizacao(cursor.getString(cursor.getColumnIndex("DT_ATUALIZACAO")));

        return imagem;
    }

    public ContentValues imagemToContentValue(Imagem imagem){
        ContentValues content = new ContentValues();

        content.put("ID",imagem.getId());
        content.put("ID_WS",imagem.getIdWS());
        content.put("NOME",imagem.getNome());
        content.put("PRINCIPAL",imagem.getPrincipal());
        content.put("CAMINHO",imagem.getCaminho());
        content.put("PRODUTO_ID_WS",imagem.getProdutoIdWS());
        content.put("DT_CRIACAO",imagem.getDtCriacao());
        content.put("DT_ATUALIZACAO",imagem.getDtAtualizacao());


        return content;
    }
}
