package lr.maisvendas.tela.fragmentos;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

import lr.maisvendas.R;
import lr.maisvendas.modelo.CondicaoPgto;
import lr.maisvendas.modelo.Pedido;
import lr.maisvendas.repositorio.sql.CondicaoPgtoDAO;
import lr.maisvendas.tela.adaptador.ListaCondPgtoSpinnerAdapter;
import lr.maisvendas.tela.interfaces.ComunicadorCadastroPedido;
import lr.maisvendas.utilitarios.Ferramentas;

public class CadastroPedidoFimFragment extends Fragment implements View.OnClickListener{

    private static final String TAG = "CadastroPedidoFimFragment";
    public static final String PARAM_PEDIDO_INI = "PARAM_PEDIDO_FIM";

    //Campos da tela
    private Spinner spinnerCondPgto;
    private TextView textVlrTotal;
    private TextView textVlrDescoto;
    private TextView textVlrFinal;
    private Button buttonExcluir;
    private Button buttonEnviar;
    private Button buttonAnt;

    //Variáveis
    private ComunicadorCadastroPedido comunicadorCadastroPedido;
    private ListaCondPgtoSpinnerAdapter listaCondPgtoSpinnerAdapter;
    private List<CondicaoPgto> listaCondicoesPgto;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cadastro_pedido_fim,container,false);

        //Tratar campos da tela aqui
        spinnerCondPgto = (Spinner) view.findViewById(R.id.fragment_cadastro_pedido_fim_spinner_condicao_pgto);
        textVlrTotal = (TextView) view.findViewById(R.id.fragment_cadastro_pedido_fim_text_vlr_total);
        textVlrDescoto = (TextView) view.findViewById(R.id.fragment_cadastro_pedido_fim_text_vlr_desconto);
        textVlrFinal = (TextView) view.findViewById(R.id.fragment_cadastro_pedido_fim_text_vlr_final);
        buttonExcluir = (Button) view.findViewById(R.id.fragment_cadastro_pedido_fim_button_excluir);
        buttonEnviar = (Button) view.findViewById(R.id.fragment_cadastro_pedido_fim_button_enviar);
        buttonAnt = (Button) view.findViewById(R.id.fragment_cadastro_pedido_fim_button_ant);

        buttonAnt.setOnClickListener(this);

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
    }

    @Override
    public void onClick(View view) {
        if (view == buttonAnt){
            CadastroPedidoItemFragment cadastroPedidoItemFragment = new CadastroPedidoItemFragment();
            //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.activity_cadastro_pedido_container,cadastroPedidoItemFragment);
            getActivity().getFragmentManager().beginTransaction().replace(R.id.activity_cadastro_pedido_container,cadastroPedidoItemFragment).commit();
        }

    }

    //Metodo para carregar informação ao abriar a Activity.
    private void loadDataFromActivity() {

        Ferramentas ferramentas = new Ferramentas();

        Pedido pedido = comunicadorCadastroPedido.getPedido();

        CondicaoPgtoDAO condicaoPgtoDAO = CondicaoPgtoDAO.getInstance(getActivity());
        listaCondicoesPgto = condicaoPgtoDAO.buscaCondicaoPgto();

        CondicaoPgto condicaoPgto = new CondicaoPgto();
        condicaoPgto.setCod("");
        condicaoPgto.setDescricao("Selecione");
        listaCondicoesPgto.add(0,condicaoPgto);
        if (listaCondicoesPgto != null) {
            listaCondPgtoSpinnerAdapter = new ListaCondPgtoSpinnerAdapter(getActivity(), listaCondicoesPgto);
            spinnerCondPgto.setAdapter(listaCondPgtoSpinnerAdapter);
        }

        if (pedido != null) {

        }
    }
}
