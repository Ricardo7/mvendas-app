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
import lr.maisvendas.modelo.Cliente;
import lr.maisvendas.utilitarios.Ferramentas;
import lr.maisvendas.utilitarios.TipoPessoa;

public class ListaClientesSpinnerAdapter extends ArrayAdapter<Cliente> {

    private Context context;

    public ListaClientesSpinnerAdapter(Context context, List<Cliente> clientes) {
        super(context, 0, clientes);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View linhaView = convertView;
        TextView textCnpj;
        TextView textRazaoSocial;
        Ferramentas ferramentas = new Ferramentas();

        //Se a view ainda não foi criada irá criá-la
        if(linhaView == null){
            LayoutInflater inflater = LayoutInflater.from(context);
            linhaView = inflater.inflate(R.layout.linha_lista_clientes_spinner,parent, false);
        }

        textCnpj = (TextView) linhaView.findViewById(R.id.linha_lista_clientes_spinner_cnpj);
        textRazaoSocial = (TextView) linhaView.findViewById(R.id.linha_lista_clientes_spinner_razao_social);

        Cliente cliente = getItem(position);
        if (!cliente.getCnpj().equals("")) {
            textCnpj.setText(ferramentas.formatCnpjCpf(cliente.getCnpj(), TipoPessoa.juridica));
        }
        textRazaoSocial.setText(cliente.getRazaoSocial());

        return linhaView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getView(position, convertView, parent);
    }
}