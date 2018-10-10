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
import lr.maisvendas.modelo.Atividade;
import lr.maisvendas.utilitarios.Ferramentas;
import lr.maisvendas.utilitarios.TipoAgenda;

public class ListaAtividadesAdapter extends ArrayAdapter<Atividade> implements Filterable {

    private Context context;
    private List<Atividade> itens;
    private Object lock = new Object();
    private AtividadeFilter filter = new AtividadeFilter();

    public ListaAtividadesAdapter(Context context, List<Atividade> atividades) {
        super(context, 0, atividades);
        this.context = context;
        this.itens = atividades;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Ferramentas ferramentas = new Ferramentas();
        View linhaView = convertView;
        TextView textHora;
        TextView textAtividade;

        //Se a view ainda não foi criada irá criá-la
        if (linhaView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            linhaView = inflater.inflate(R.layout.linha_lista_atividades, parent, false);
        }

        textHora = (TextView) linhaView.findViewById(R.id.linha_lista_atividades_hora);
        textAtividade = (TextView) linhaView.findViewById(R.id.linha_lista_atividades_atividade);
        //ListView listView = (ListView) findViewById(R.id.activity_agenda_list_view);


        Atividade atividade = getItem(position);

        textHora.setText(atividade.getHoraAtividade());
        textAtividade.setText(atividade.getAssunto());

        //Se a agenda for do Tipo Sugestão irá pintar o fundo de cor diferente senão deixa default
        if (atividade.getTipo().equals(TipoAgenda.SUGESTAO)) {

            linhaView.setBackgroundResource(R.drawable.border_line_view_sugestao);

        }else{
            linhaView.setBackgroundResource(R.drawable.border_line_view);
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
    private class AtividadeFilter extends Filter {

        private ArrayList<Atividade> originalItens;

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (originalItens == null) {
                synchronized (lock) {
                    originalItens = new ArrayList<Atividade>(itens);
                }
            }

            if (constraint == null || constraint.length() == 0) {
                ArrayList<Atividade> list = new ArrayList<Atividade>(originalItens);

                results.count = list.size();
                results.values = list;
            } else {
                ArrayList<Atividade> newValues = new ArrayList<Atividade>(originalItens.size());

                String prefix = constraint.toString().toLowerCase();
                for (Atividade Atividade : originalItens) {
                    //Aqui filtro o objeto Atividade
                    if (Atividade.getAssunto().toLowerCase().contains(prefix)) {
                        newValues.add(Atividade);
                    } else if(Atividade.getCliente().getRazaoSocial().contains(prefix)){
                        newValues.add(Atividade);
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
                itens = (List<Atividade>) results.values;
                clear();

                for (Atividade Atividade : itens)
                    add(Atividade);

                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }

    }
}
