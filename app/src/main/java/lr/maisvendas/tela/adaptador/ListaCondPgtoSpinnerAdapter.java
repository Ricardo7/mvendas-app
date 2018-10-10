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
import lr.maisvendas.modelo.CondicaoPagamento;

public class ListaCondPgtoSpinnerAdapter extends ArrayAdapter<CondicaoPagamento> {

    private Context context;

    public ListaCondPgtoSpinnerAdapter(Context context, List<CondicaoPagamento> lojas) {
        super(context, 0, lojas);
        this.context = context;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View linhaView = convertView;
        TextView textCod;
        TextView textDescricao;

        //Se a view ainda não foi criada irá criá-la
        if(linhaView == null){
            LayoutInflater inflater = LayoutInflater.from(context);
            linhaView = inflater.inflate(R.layout.linha_lista_cond_pgto_spinner,parent, false);
        }

        textCod = (TextView) linhaView.findViewById(R.id.linha_lista_cond_pgto_spinner_cod);
        textDescricao = (TextView) linhaView.findViewById(R.id.linha_lista_cond_pgto_spinner_descricao);

        CondicaoPagamento CondicaoPagamento = getItem(position);

        textCod.setText(CondicaoPagamento.getCod());
        textDescricao.setText(CondicaoPagamento.getDescricao());

        return linhaView;
    }
    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getView(position, convertView, parent);
    }
}