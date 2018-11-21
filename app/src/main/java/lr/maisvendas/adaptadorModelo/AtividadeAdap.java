package lr.maisvendas.adaptadorModelo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import lr.maisvendas.modelo.Atividade;
import lr.maisvendas.utilitarios.TipoAgenda;

public class AtividadeAdap {

    private Context context;

    public void AtividadeAdap (Context context){
        this.context = context;
    }

    public Atividade sqlToAtividade (Cursor cursor){
        Atividade atividade = new Atividade();

        atividade.setId(cursor.getInt(cursor.getColumnIndex("ID")));
        atividade.setIdWS(cursor.getString(cursor.getColumnIndex("ID_WS")));
        atividade.setAssunto(cursor.getString(cursor.getColumnIndex("ASSUNTO")));
        atividade.setObservacao(cursor.getString(cursor.getColumnIndex("OBSERVACAO")));
        atividade.setDataAtividade(cursor.getString(cursor.getColumnIndex("DATA_ATIVIDADE")));
        atividade.setHoraAtividade(cursor.getString(cursor.getColumnIndex("HORA_ATIVIDADE")));
        atividade.setLatitude(cursor.getDouble(cursor.getColumnIndex("LATITUDE")));
        atividade.setLongitude(cursor.getDouble(cursor.getColumnIndex("LONGITUDE")));
        atividade.setDataCkeckin(cursor.getString(cursor.getColumnIndex("DATA_CHECKIN")));
        //Tudo que vem do banco será do tipo Efetiva, agendas sugeridas serão buscadas diretamente na API
        atividade.setTipo(TipoAgenda.EFETIVA);
        atividade.setDtCadastro(cursor.getString(cursor.getColumnIndex("DT_CADASTRO")));
        atividade.setDtAtualizacao(cursor.getString(cursor.getColumnIndex("DT_ATUALIZACAO")));

        return atividade;
    }

    public ContentValues atividadeToContentValue (Atividade atividade){
        ContentValues content = new ContentValues();

        content.put("ID", atividade.getId());
        content.put("ID_WS", atividade.getIdWS());
        content.put("ASSUNTO", atividade.getAssunto());
        content.put("CLIENTE_ID", atividade.getCliente().getId());
        content.put("USUARIO_ID", atividade.getUsuario().getId());
        content.put("OBSERVACAO", atividade.getObservacao());
        content.put("DATA_ATIVIDADE", atividade.getDataAtividade());
        content.put("HORA_ATIVIDADE", atividade.getHoraAtividade());
        content.put("LATITUDE", atividade.getLatitude());
        content.put("LONGITUDE", atividade.getLongitude());
        content.put("DATA_CHECKIN", atividade.getDataCkeckin());
        content.put("DT_CADASTRO", atividade.getDtCadastro());
        content.put("DT_ATUALIZACAO", atividade.getDtAtualizacao());

        return content;
    }

}
