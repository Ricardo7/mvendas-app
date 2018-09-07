package lr.maisvendas.adaptadorModelo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import lr.maisvendas.modelo.ItemTabelaPreco;

public class ItemTabelaPrecoAdap {

    private Context context;

    public void ItemTabelaPrecoAdap(Context context){
        this.context = context;
    }

    public ItemTabelaPreco sqlToItemTabelaPreco(Cursor cursor){
        ItemTabelaPreco itemTabelaPreco = new ItemTabelaPreco();

        itemTabelaPreco.setId(cursor.getInt(cursor.getColumnIndex("ID")));
        itemTabelaPreco.setVlrUnitario(cursor.getDouble(cursor.getColumnIndex("VLR_UNITARIO")));
        itemTabelaPreco.setMaxDesc(cursor.getDouble(cursor.getColumnIndex("MAX_DESC")));

        return itemTabelaPreco;
    }

    public ContentValues itemTabelaPrecoToContentValue(ItemTabelaPreco itemTabelaPreco, Integer tabelaPrecoId){
        ContentValues content = new ContentValues();

        content.put("ID",itemTabelaPreco.getId());
        content.put("VLR_UNITARIO",itemTabelaPreco.getVlrUnitario());
        content.put("MAX_DESC",itemTabelaPreco.getMaxDesc());
        content.put("PRODUTO_ID",itemTabelaPreco.getProduto().getId());
        content.put("TABELA_PRECO_ID",tabelaPrecoId);

        return content;
    }
}
