package lr.maisvendas.adaptadorModelo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import lr.maisvendas.modelo.Atividade;

public class AtividadeAdap {

    private Context context;

    public void AtividadeAdap (Context context){
        this.context = context;
    }

    public Atividade sqlToAtividade (Cursor cursor){
        Atividade atividade = new Atividade();

        atividade.setId(cursor.getInt(0));
        atividade.setAssunto(cursor.getString(1));
        //Cliente | CLIENTE_ID INTEGER
        atividade.setObservacao(cursor.getString(3));
        atividade.setDataAtividade(cursor.getString(4));
        atividade.setHoraAtividade(cursor.getString(5));
        atividade.setDtCriacao(cursor.getString(6));
        atividade.setDtAtualizacao(cursor.getString(7));

        return atividade;
    }

    public ContentValues atividadeToContentValue (Atividade atividade){
        ContentValues content = new ContentValues();

        content.put("ID", atividade.getId());
        content.put("ASSUNTO", atividade.getAssunto());
        content.put("CLIENTE_ID", atividade.getCliente().getId());
        content.put("OBSERVACAO", atividade.getObservacao());
        content.put("DATA_ATIVIDADE", atividade.getDataAtividade());
        content.put("HORA_ATIVIDADE", atividade.getHoraAtividade());
        content.put("DT_CRIACAO", atividade.getDtCriacao());
        content.put("DT_ATUALIZACAO", atividade.getDtAtualizacao());

        return content;
    }

}
