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
        imagem.setNome(cursor.getString(cursor.getColumnIndex("NOME")));
        imagem.setPrincipal(cursor.getInt(cursor.getColumnIndex("PRINCIPAL")));
        imagem.setTamanho(cursor.getInt(cursor.getColumnIndex("TAMANHO")));
        imagem.setCaminho(cursor.getString(cursor.getColumnIndex("CAMINHO")));
        imagem.setDtCriacao(cursor.getString(cursor.getColumnIndex("DT_CRIACAO")));
        imagem.setDtAtualizacao(cursor.getString(cursor.getColumnIndex("DT_ATUALIZACAO")));

        return imagem;
    }

    public ContentValues imagemToContentValue(Imagem imagem, Integer produtoId){
        ContentValues content = new ContentValues();

        content.put("ID",imagem.getId());
        content.put("NOME",imagem.getNome());
        content.put("PRINCIPAL",imagem.getPrincipal());
        content.put("CAMINHO",imagem.getCaminho());
        content.put("TAMANHO",imagem.getTamanho());
        content.put("DT_CRIACAO",imagem.getDtCriacao());
        content.put("DT_ATUALIZACAO",imagem.getDtAtualizacao());
        content.put("PRODUTO_ID",produtoId);

        return content;
    }
}
