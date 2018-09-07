package lr.maisvendas.adaptadorModelo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import lr.maisvendas.modelo.Cidade;

public class CidadeAdap {

    private Context context;

    public void CidadeAdap(Context context){
        this.context = context;
    }

    public Cidade sqlToCidade(Cursor cursor){
        Cidade cidade = new Cidade();

        cidade.setId(cursor.getInt(0));
        cidade.setDescricao(cursor.getString(1));
        cidade.setSigla(cursor.getString(2));
        //Estado | ESTADO_ID INTEGER

        return cidade;
    }

    public ContentValues cidadeToContentValue(Cidade cidade){
        ContentValues content = new ContentValues();

        content.put("ID", cidade.getId());
        content.put("DESCRICAO", cidade.getDescricao());
        content.put("SIGLA", cidade.getSigla());
        content.put("ESTADO_ID", cidade.getEstado().getId());

        return content;
    }
}
