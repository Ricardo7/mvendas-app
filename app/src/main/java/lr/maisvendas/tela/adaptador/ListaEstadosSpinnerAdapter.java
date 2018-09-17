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
import lr.maisvendas.modelo.Estado;
import lr.maisvendas.utilitarios.Ferramentas;

public class ListaEstadosSpinnerAdapter extends ArrayAdapter<Estado> {

    private Context context;

    public ListaEstadosSpinnerAdapter(Context context, List<Estado> estados) {
        super(context, 0, estados);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View linhaView = convertView;
        //TextView textSigla;
        TextView textDescricao;
        Ferramentas ferramentas = new Ferramentas();

        //Se a view ainda não foi criada irá criá-la
        if(linhaView == null){
            LayoutInflater inflater = LayoutInflater.from(context);
            linhaView = inflater.inflate(R.layout.linha_lista_estados_spinner,parent, false);
        }

        //textSigla = (TextView) linhaView.findViewById(R.id.linha_lista_estados_spinner_sigla);
        textDescricao = (TextView) linhaView.findViewById(R.id.linha_lista_estados_spinner_descricao);

        Estado estado = getItem(position);

        //textSigla.setText(estado.getSigla());
        textDescricao.setText(estado.getDescricao());

        return linhaView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getView(position, convertView, parent);
    }
}