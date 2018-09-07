package lr.maisvendas.tela.adaptador;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.List;

import lr.maisvendas.R;
import lr.maisvendas.modelo.ItemPedido;
import lr.maisvendas.utilitarios.Ferramentas;

public class ListaItensPedidoAdapter extends ArrayAdapter<ItemPedido> implements Filterable {

    private Context context;
    private List<ItemPedido> itemPedido;
    private Object lock = new Object();

    public ListaItensPedidoAdapter(Context context, List<ItemPedido> itemPedido) {
        super(context, 0, itemPedido);
        this.context = context;
        this.itemPedido = itemPedido;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Ferramentas ferramentas = new Ferramentas();
        View linhaView = convertView;
        TextView textCodigo;
        TextView textDescricao;
        TextView textQuantidade;
        TextView textVlrUnit;
        TextView textVlrDesconto;
        TextView textVlrTotal;

        //Se a view ainda não foi criada irá criá-la
        if (linhaView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            linhaView = inflater.inflate(R.layout.linha_lista_itens_pedido, parent, false);
        }

        textCodigo = (TextView) linhaView.findViewById(R.id.linha_lista_itens_pedido_codigo);
        textDescricao = (TextView) linhaView.findViewById(R.id.linha_lista_itens_pedido_descricao);
        textQuantidade = (TextView) linhaView.findViewById(R.id.linha_lista_itens_pedido_quantidade);
        textVlrUnit = (TextView) linhaView.findViewById(R.id.linha_lista_itens_pedido_vlr_unit);
        textVlrDesconto = (TextView) linhaView.findViewById(R.id.linha_lista_itens_pedido_vlr_desc);
        textVlrTotal = (TextView) linhaView.findViewById(R.id.linha_lista_itens_pedido_vlr_total);

        ItemPedido itemPedido = getItem(position);

        textCodigo.setText(itemPedido.getProduto().getCod());
        textDescricao.setText(itemPedido.getProduto().getDescricao());
        textQuantidade.setText(itemPedido.getQuantidade().toString());
        textVlrUnit.setText(itemPedido.getVlrUnitario().toString());
        textVlrDesconto.setText(itemPedido.getVlrDesconto().toString());
        textVlrTotal.setText(itemPedido.getVlrTotal().toString());
        return linhaView;

    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getView(position, convertView, parent);
    }

}
