package lr.maisvendas.tela.adaptador;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import lr.maisvendas.R;
import lr.maisvendas.modelo.Pedido;
import lr.maisvendas.utilitarios.Ferramentas;

public class ListaPedidosAdapter extends ArrayAdapter<Pedido> implements Filterable {

    private Context context;
    private List<Pedido> itens;
    private Object lock = new Object();
    private PedidoFilter filter = new PedidoFilter();

    public ListaPedidosAdapter(Context context, List<Pedido> Pedidos) {
        super(context, 0, Pedidos);
        this.context = context;
        this.itens = Pedidos;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Ferramentas ferramentas = new Ferramentas();
        View linhaView = convertView;
        TextView textCnpjRazSocial;
        TextView textNumPedido;
        TextView textDataPedido;
        ImageView imagePedidoSituacao;
        ImageView imagePedidoStatus;

        //Se a view ainda não foi criada irá criá-la
        if (linhaView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            linhaView = inflater.inflate(R.layout.linha_lista_pedidos, parent, false);
        }

        textCnpjRazSocial = (TextView) linhaView.findViewById(R.id.linha_lista_pedidos_cnpj_raz_social);
        textNumPedido = (TextView) linhaView.findViewById(R.id.linha_lista_pedidos_num_pedido);
        textDataPedido = (TextView) linhaView.findViewById(R.id.linha_lista_pedidos_data_pedido);
        imagePedidoSituacao = (ImageView) linhaView.findViewById(R.id.linha_lista_pedidos_ic_situacao);
        imagePedidoStatus = (ImageView) linhaView.findViewById(R.id.linha_lista_pedidos_ic_status);

        Pedido pedido = getItem(position);

        textCnpjRazSocial.setText("("+pedido.getCliente().getCnpj()+") "+pedido.getCliente().getRazaoSocial());
        textNumPedido.setText(pedido.getNumero().toString());
        textDataPedido.setText(ferramentas.formatDateUser(pedido.getDtCriacao()));

        if (pedido.getSituacao() == 0) {
            imagePedidoSituacao.setBackgroundResource(R.drawable.ic_pedido_pendente);
        } else if (pedido.getSituacao() == 1) {
            imagePedidoSituacao.setBackgroundResource(R.drawable.ic_pedido_bloqueado);
        } else if (pedido.getSituacao() == 2) {
            imagePedidoSituacao.setBackgroundResource(R.drawable.ic_pedido_aprovado);
        } else if (pedido.getSituacao() == 3) {
            imagePedidoSituacao.setBackgroundResource(R.drawable.ic_pedido_cancelado);
        }

        if (pedido.getStatus() == 0) {
            imagePedidoStatus.setBackgroundResource(R.drawable.ic_pedido_aberto);
        } else if (pedido.getStatus() == 1) {
            imagePedidoStatus.setBackgroundResource(R.drawable.ic_pedido_fechado);
        } else if (pedido.getStatus() == 2) {
            imagePedidoStatus.setBackgroundResource(R.drawable.ic_pedido_enviado);
        }
        return linhaView;

    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getView(position, convertView, parent);
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    //Verificar possíbilidade de melhorar código
    private class PedidoFilter extends Filter {

        private ArrayList<Pedido> originalItens;

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (originalItens == null) {
                synchronized (lock) {
                    originalItens = new ArrayList<Pedido>(itens);
                }
            }

            if (constraint == null || constraint.length() == 0) {
                ArrayList<Pedido> list = new ArrayList<Pedido>(originalItens);

                results.count = list.size();
                results.values = list;
            } else {
                ArrayList<Pedido> newValues = new ArrayList<Pedido>(originalItens.size());

                String prefix = constraint.toString().toLowerCase();
                for (Pedido Pedido : originalItens) {
                    //Aqui filtro o objeto Pedido
                    if (Pedido.getNumero().toString().contains(prefix)) {
                        newValues.add(Pedido);
                    } else if(Pedido.getCliente().getRazaoSocial().toLowerCase().contains(prefix)){
                        newValues.add(Pedido);
                    } else if(Pedido.getCliente().getCnpj().toLowerCase().contains(prefix)){
                        newValues.add(Pedido);
                    }
                }

                results.count = newValues.size();
                results.values = newValues;
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.count > 0) {
                itens = (List<Pedido>) results.values;
                clear();

                for (Pedido Pedido : itens)
                    add(Pedido);

                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }

    }

}
