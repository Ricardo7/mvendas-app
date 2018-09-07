package lr.maisvendas.tela.fragmentos;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import lr.maisvendas.R;
import lr.maisvendas.modelo.Pedido;
import lr.maisvendas.tela.interfaces.ComunicadorCadastroPedido;
import lr.maisvendas.utilitarios.Ferramentas;

public class CadastroPedidoIniFragment extends Fragment implements View.OnClickListener{

    private static final String TAG = "CadastroPedidoIniFragment";
    public static final String PARAM_PEDIDO_INI = "PARAM_PEDIDO_INI";

    //Campos da tela
    private Button buttonProx;
    private TextView textNumPedido;
    private TextView textDataPedido;
    private TextView textSituacao;
    private TextView textStatus;

    //Variáveis
    private ComunicadorCadastroPedido comunicadorCadastroPedido;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cadastro_pedido_ini,container,false);

        //Tratar campos da tela aqui
        buttonProx = (Button) view.findViewById(R.id.fragment_cadastro_pedido_ini_button_prox);
        textNumPedido = (TextView) view.findViewById(R.id.activity_cadastro_pedido_ini_text_num_pedido);
        textDataPedido = (TextView) view.findViewById(R.id.activity_cadastro_pedido_ini_text_data_pedido);
        textSituacao = (TextView) view.findViewById(R.id.activity_cadastro_pedido_ini_text_situacao);
        textStatus = (TextView) view.findViewById(R.id.activity_cadastro_pedido_ini_text_status);


        buttonProx.setOnClickListener(this);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        comunicadorCadastroPedido = (ComunicadorCadastroPedido) activity;
        //Acontece quando o fragmento foi atachado na activity
        //listaAtividadesAdapter = new ListaAtividadesAdapter(activity, AtividadeDataSource.getInstance().consultar(null));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        loadDataFromActivity();

        // Cria o adapter e configura o list view para mostrar o conteúdo do ArrayList armazenado em listaAtividades
        //listaAtividadesAdapter = new ListaAtividadesAdapter(getActivity(), listaAtividades);
        //listViewAtividades.setAdapter(listaAtividadesAdapter);

    }

    @Override
    public void onClick(View view) {
        if (view == buttonProx){
            CadastroPedidoItemFragment cadastroPedidoItemFragment = new CadastroPedidoItemFragment();
            //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.activity_cadastro_pedido_container,cadastroPedidoItemFragment);
            getActivity().getFragmentManager().beginTransaction().replace(R.id.activity_cadastro_pedido_container,cadastroPedidoItemFragment).commit();
        }
    }

    //Metodo para carregar informação ao abriar a Activity.
    private void loadDataFromActivity() {
        Ferramentas ferramentas = new Ferramentas();

        Pedido pedido = comunicadorCadastroPedido.getPedido();

        if (pedido != null) {
            textNumPedido.setText(pedido.getNumero().toString());
            textDataPedido.setText(ferramentas.formatDateUser(pedido.getDtCriacao()));

            if (pedido.getSituacao() == 0) {
                textSituacao.setText("Pendente");
            } else if (pedido.getSituacao() == 1) {
                textSituacao.setText("Bloqueado");
            } else if (pedido.getSituacao() == 2) {
                textSituacao.setText("Aprovado");
            } else if (pedido.getSituacao() == 3) {
                textSituacao.setText("Cancelado");
            }

            if (pedido.getStatus() == 0) {
                textStatus.setText("Em Construção");
            } else if (pedido.getStatus() == 1) {
                textStatus.setText("Não Enviado");
            } else if (pedido.getStatus() == 2) {
                textStatus.setText("Enviado");
            }
        }

    }
}
