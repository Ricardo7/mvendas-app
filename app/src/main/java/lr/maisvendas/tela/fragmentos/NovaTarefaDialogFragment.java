package lr.maisvendas.tela.fragmentos;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import lr.maisvendas.R;


public class NovaTarefaDialogFragment extends Fragment {

    public static String ARG_POSITION = "arg_position";

    public NovaTarefaDialogFragment(){};

    public static NovaTarefaDialogFragment newInstance(int postion){
        Bundle parameters = new Bundle();
        parameters.putInt(ARG_POSITION, postion);
        NovaTarefaDialogFragment frag = new NovaTarefaDialogFragment();
        frag.setArguments(parameters);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cadastro_pedido_ini,container,false);


        //buttonProx.setOnClickListener(this);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        //Acontece quando o fragmento foi atachado na activity
        //listaAtividadesAdapter = new ListaAtividadesAdapter(activity, AtividadeDataSource.getInstance().consultar(null));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        // Cria o adapter e configura o list view para mostrar o conteúdo do ArrayList armazenado em listaAtividades
        //listaAtividadesAdapter = new ListaAtividadesAdapter(getActivity(), listaAtividades);
        //listViewAtividades.setAdapter(listaAtividadesAdapter);

    }


}
