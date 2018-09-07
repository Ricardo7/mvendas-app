package lr.maisvendas.adaptadorModelo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import lr.maisvendas.modelo.Meta;

public class MetaAdap {

    private Context context;

    public void MetaAdap(Context context){
        this.context = context;
    }

    public Meta sqlToMeta(Cursor cursor){
        Meta meta = new Meta();

        meta.setId(cursor.getInt(cursor.getColumnIndex("ID")));
        meta.setEstimado(cursor.getDouble(cursor.getColumnIndex("ESTIMADO")));
        meta.setRealizado(cursor.getDouble(cursor.getColumnIndex("REALIZADO")));

        return meta;
    }

    public ContentValues metaToContentValue(Meta meta){
        ContentValues content = new ContentValues();

        content.put("ID",meta.getId());
        content.put("ESTIMADO",meta.getEstimado());
        content.put("REALIZADO",meta.getRealizado());

        return content;
    }
}
