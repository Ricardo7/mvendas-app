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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import lr.maisvendas.R;
import lr.maisvendas.modelo.Cliente;
import lr.maisvendas.utilitarios.Ferramentas;
import lr.maisvendas.utilitarios.TipoPessoa;

public class ListaClientesAdapter extends ArrayAdapter<Cliente> implements Filterable {

    private Context context;
    private List<Cliente> itens;
    private Object lock = new Object();
    private ClienteFilter filter = new ClienteFilter();

    public ListaClientesAdapter(Context context, List<Cliente> clientes) {
        super(context, 0, clientes);
        this.context = context;
        this.itens = clientes;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Ferramentas ferramentas = new Ferramentas();
        View linhaView = convertView;
        TextView textRazaoSocial;
        TextView textCnpj;

        //Se a view ainda não foi criada irá criá-la
        if (linhaView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            linhaView = inflater.inflate(R.layout.linha_lista_clientes, parent, false);
        }

        textRazaoSocial = (TextView) linhaView.findViewById(R.id.linha_lista_clientes_razao_social);
        textCnpj = (TextView) linhaView.findViewById(R.id.linha_lista_clientes_cnpj);

        Cliente cliente = getItem(position);

        textRazaoSocial.setText(cliente.getRazaoSocial());
        textCnpj.setText(ferramentas.formatCnpjCpf(cliente.getCnpj(), TipoPessoa.juridica));

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
    private class ClienteFilter extends Filter {

        private ArrayList<Cliente> originalItens;

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (originalItens == null) {
                synchronized (lock) {
                    originalItens = new ArrayList<Cliente>(itens);
                }
            }

            if (constraint == null || constraint.length() == 0) {
                ArrayList<Cliente> list = new ArrayList<Cliente>(originalItens);

                results.count = list.size();
                results.values = list;
            } else {
                ArrayList<Cliente> newValues = new ArrayList<Cliente>(originalItens.size());

                String prefix = constraint.toString().toLowerCase();
                for (Cliente Cliente : originalItens) {

                    //Aqui filtro o objeto Cliente
                    if(Cliente.getRazaoSocial().toLowerCase().contains(prefix)){
                        newValues.add(Cliente);
                    } else if(Cliente.getCnpj().contains(prefix)){
                        newValues.add(Cliente);
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
                itens = (List<Cliente>) results.values;
                clear();

                for (Cliente Cliente : itens)
                    add(Cliente);

                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }

    }
}
