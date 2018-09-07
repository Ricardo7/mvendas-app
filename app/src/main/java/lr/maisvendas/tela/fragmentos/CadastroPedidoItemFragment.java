package lr.maisvendas.tela.fragmentos;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import lr.maisvendas.R;
import lr.maisvendas.modelo.Pedido;
import lr.maisvendas.tela.interfaces.ComunicadorCadastroPedido;
import lr.maisvendas.tela.adaptador.ListaItensPedidoAdapter;

public class CadastroPedidoItemFragment extends Fragment  implements View.OnClickListener{

    private static final String TAG = "CadastroPedidoItemFragment";
    public static final String PARAM_PEDIDO_ITEM = "PARAM_PEDIDO_ITEM";

    //Campos da tela
    private ListView listViewItesPedido;
    private Button buttonAnt;
    private Button buttonProx;

    //Variáveis
    private ListaItensPedidoAdapter listaItensPedidoAdapter;
    private ComunicadorCadastroPedido comunicadorCadastroPedido;
    private Pedido pedido;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cadastro_pedido_item,container,false);

        //Tratar campos da tela aqui
        listViewItesPedido = (ListView) view.findViewById(R.id.fragment_cadastro_pedido_item_list_view);
        buttonAnt = (Button) view.findViewById(R.id.fragment_cadastro_pedido_item_button_ant);
        buttonProx = (Button) view.findViewById(R.id.fragment_cadastro_pedido_item_button_prox);

        buttonAnt.setOnClickListener(this);
        buttonProx.setOnClickListener(this);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        comunicadorCadastroPedido = (ComunicadorCadastroPedido) activity;
        pedido = comunicadorCadastroPedido.getPedido();
        //Acontece quando o fragmento foi atachado na activity
        //listaItensPedidoAdapter = new ListaItensPedidoAdapter(activity, comunicadorCadastroPedido.getPedido().getItensPedido());
        loadDataFromActivity(false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        loadDataFromActivity(true);

    }

    @Override
    public void onClick(View view) {
        if (view == buttonAnt){
            CadastroPedidoIniFragment cadastroPedidoIniFragment = new CadastroPedidoIniFragment();
            //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.activity_cadastro_pedido_container,cadastroPedidoItemFragment);
            getActivity().getFragmentManager().beginTransaction().replace(R.id.activity_cadastro_pedido_container,cadastroPedidoIniFragment).commit();
        }else if (view == buttonProx){
            CadastroPedidoFimFragment cadastroPedidoFimFragment = new CadastroPedidoFimFragment();
            //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.activity_cadastro_pedido_container,cadastroPedidoItemFragment);
            getActivity().getFragmentManager().beginTransaction().replace(R.id.activity_cadastro_pedido_container,cadastroPedidoFimFragment).commit();
        }

    }

    //Metodo para carregar informação ao abriar a Activity.
    private void loadDataFromActivity(Boolean setAdapter) {

        if (pedido != null) {
            // Cria o adapter e configura o list view para mostrar o conteúdo do ArrayList armazenado em listaAtividades
            listaItensPedidoAdapter = new ListaItensPedidoAdapter(getActivity(), pedido.getItensPedido());

            if (setAdapter) {
                listViewItesPedido.setAdapter(listaItensPedidoAdapter);
            }
        }
    }
}
