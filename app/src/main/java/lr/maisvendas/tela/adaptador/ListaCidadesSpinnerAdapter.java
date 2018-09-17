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
import lr.maisvendas.modelo.Cidade;

public class ListaCidadesSpinnerAdapter extends ArrayAdapter<Cidade> {

    private Context context;

    public ListaCidadesSpinnerAdapter(Context context, List<Cidade> lojas) {
        super(context, 0, lojas);
        this.context = context;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View linhaView = convertView;
        //TextView textSigla;
        TextView textDescricao;

        //Se a view ainda não foi criada irá criá-la
        if(linhaView == null){
            LayoutInflater inflater = LayoutInflater.from(context);
            linhaView = inflater.inflate(R.layout.linha_lista_cidades_spinner,parent, false);
        }

        //textSigla = (TextView) linhaView.findViewById(R.id.linha_lista_cidades_spinner_sigla);
        textDescricao = (TextView) linhaView.findViewById(R.id.linha_lista_cidades_spinner_descricao);

        Cidade cidade = getItem(position);

        //textSigla.setText(cidade.getSigla());
        textDescricao.setText(cidade.getDescricao());

        return linhaView;
    }
    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getView(position, convertView, parent);
    }
}