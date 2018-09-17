package lr.maisvendas.tela.adaptador;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import lr.maisvendas.R;
import lr.maisvendas.modelo.TabelaPreco;
import lr.maisvendas.utilitarios.Ferramentas;

public class ListaTabelaPrecosSpinnerAdapter extends ArrayAdapter<TabelaPreco> {

    private Context context;

    public ListaTabelaPrecosSpinnerAdapter(Context context, List<TabelaPreco> tabelaPrecos) {
        super(context, 0, tabelaPrecos);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View linhaView = convertView;
        TextView textCod;
        TextView textDescricao;
        Ferramentas ferramentas = new Ferramentas();

        //Se a view ainda não foi criada irá criá-la
        if(linhaView == null){
            LayoutInflater inflater = LayoutInflater.from(context);
            linhaView = inflater.inflate(R.layout.linha_lista_tabela_precos_spinner,parent, false);
        }

        textCod = (TextView) linhaView.findViewById(R.id.linha_lista_tabela_precos_spinner_cod);
        textDescricao = (TextView) linhaView.findViewById(R.id.linha_lista_tabela_precos_spinner_descricao);

        TabelaPreco TabelaPreco = getItem(position);

        textCod.setText(TabelaPreco.getCod());
        textDescricao.setText(TabelaPreco.getDescricao());

        return linhaView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getView(position, convertView, parent);
    }
}