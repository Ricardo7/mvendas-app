package lr.maisvendas.adaptadorModelo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import lr.maisvendas.modelo.Pedido;

public class PedidoAdap {

    private Context context;

    public void PedidoAdap(Context context){
        this.context = context;
    }

    public Pedido sqlToPedido(Cursor cursor){
        Pedido pedido = new Pedido();

        pedido.setId(cursor.getInt(cursor.getColumnIndex("ID")));
        pedido.setNumero(cursor.getInt(cursor.getColumnIndex("NUMERO")));
        pedido.setSituacao(cursor.getInt(cursor.getColumnIndex("SITUACAO")));
        pedido.setStatus(cursor.getInt(cursor.getColumnIndex("STATUS")));
        pedido.setDtCriacao(cursor.getString(cursor.getColumnIndex("DT_CRIACAO")));
        pedido.setDtAtualizacao(cursor.getString(cursor.getColumnIndex("DT_ATUALIZACAO")));

        return pedido;
    }

    public ContentValues pedidoToContentValue(Pedido pedido){
        ContentValues content = new ContentValues();

        content.put("ID", pedido.getId());
        content.put("NUMERO", pedido.getNumero());
        content.put("SITUACAO", pedido.getSituacao());
        content.put("STATUS", pedido.getStatus());
        content.put("CLIENTE_ID",pedido.getCliente().getId());
        content.put("CONDICAO_PGTO_ID",pedido.getCondicaoPgto().getId());
        content.put("TABELA_PRECO_ID",pedido.getTabelaPreco().getId());
        content.put("DT_CRIACAO",pedido.getDtCriacao());
        content.put("DT_ATUALIZACAO",pedido.getDtAtualizacao());

        return content;
    }
}
