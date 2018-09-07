package lr.maisvendas.adaptadorModelo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import lr.maisvendas.modelo.ItemPedido;

public class ItemPedidoAdap {

    private Context context;

    public void ItemPedidoAdap(Context context){
        this.context = context;
    }

    public ItemPedido sqlToItemPedido(Cursor cursor){
        ItemPedido itemPedido = new ItemPedido();

        itemPedido.setId(cursor.getInt(cursor.getColumnIndex("ID")));
        itemPedido.setQuantidade(cursor.getDouble(cursor.getColumnIndex("QUANTIDADE")));
        itemPedido.setVlrUnitario(cursor.getDouble(cursor.getColumnIndex("VLR_UNITARIO")));
        itemPedido.setVlrDesconto(cursor.getDouble(cursor.getColumnIndex("VLR_DESCONTO")));
        itemPedido.setVlrTotal(cursor.getDouble(cursor.getColumnIndex("VLR_TOTAL")));
        itemPedido.setDtCriacao(cursor.getString(cursor.getColumnIndex("DT_CRIACAO")));
        itemPedido.setDtAtualizacao(cursor.getString(cursor.getColumnIndex("DT_ATUALIZACAO")));

        return itemPedido;
    }

    public ContentValues itemPedidoToContentValue(ItemPedido itemPedido, Integer pedidoId){
        ContentValues content = new ContentValues();

            content.put("ID",itemPedido.getId());
             content.put("QUANTIDADE",itemPedido.getQuantidade());
             content.put("VLR_UNITARIO",itemPedido.getVlrUnitario());
             content.put("VLR_DESCONTO",itemPedido.getVlrDesconto());
             content.put("VLR_TOTAL",itemPedido.getVlrTotal());
             content.put("DT_CRIACAO",itemPedido.getDtCriacao());
             content.put("DT_ATUALIZACAO",itemPedido.getDtAtualizacao());
             content.put("PRODUTO_ID",itemPedido.getProduto().getId());
             content.put("PEDIDO_ID",pedidoId);

        return content;
    }
}
