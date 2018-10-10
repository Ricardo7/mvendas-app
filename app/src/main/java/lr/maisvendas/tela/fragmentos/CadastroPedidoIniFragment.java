package lr.maisvendas.tela.fragmentos;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

import lr.maisvendas.R;
import lr.maisvendas.modelo.Cliente;
import lr.maisvendas.modelo.CondicaoPagamento;
import lr.maisvendas.modelo.Pedido;
import lr.maisvendas.modelo.TabelaPreco;
import lr.maisvendas.repositorio.sql.ClienteDAO;
import lr.maisvendas.repositorio.sql.CondicaoPgtoDAO;
import lr.maisvendas.repositorio.sql.PedidoDAO;
import lr.maisvendas.repositorio.sql.TabelaPrecoDAO;
import lr.maisvendas.tela.adaptador.ListaClientesSpinnerAdapter;
import lr.maisvendas.tela.adaptador.ListaCondPgtoSpinnerAdapter;
import lr.maisvendas.tela.adaptador.ListaTabelaPrecosSpinnerAdapter;
import lr.maisvendas.tela.interfaces.ComunicadorCadastroPedido;
import lr.maisvendas.utilitarios.Exceptions;
import lr.maisvendas.utilitarios.Ferramentas;

public class CadastroPedidoIniFragment extends Fragment implements View.OnClickListener{

    private static final String TAG = "CadastroPedidoIniFragment";
    public static final String PARAM_PEDIDO_INI = "PARAM_PEDIDO_INI";
    public static String ARG_POSITION = "arg_position";

    //Campos da tela
    private Button buttonProx;
    private TextView textNumPedido;
    private TextView textDataPedido;
    private TextView textSituacao;
    private TextView textStatus;
    private Spinner spinnerCliente;
    private Spinner spinnerTabelaPreco;
    private Spinner spinnerCondPgto;
    private EditText editObservacao;

    //Variáveis
    private ComunicadorCadastroPedido comunicadorCadastroPedido;
    private ListaTabelaPrecosSpinnerAdapter listaTabelaPrecosSpinnerAdapter;
    private ListaClientesSpinnerAdapter listaClientesSpinnerAdapter;
    private ListaCondPgtoSpinnerAdapter listaCondPgtoSpinnerAdapter;
    private List<CondicaoPagamento> listaCondicoesPgto;
    private List<TabelaPreco> listaTabelasPreco;
    private List<Cliente> listaClientes;
    private Pedido pedido;
    private Ferramentas ferramentas;

    public CadastroPedidoIniFragment(){};

    public static CadastroPedidoIniFragment newInstance(int postion){
        Bundle parameters = new Bundle();
        parameters.putInt(ARG_POSITION, postion);
        CadastroPedidoIniFragment frag = new CadastroPedidoIniFragment();
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

        //Tratar campos da tela aqui
       // buttonProx = (Button) view.findViewById(R.id.fragment_cadastro_pedido_ini_button_prox);
        textNumPedido = (TextView) view.findViewById(R.id.fragment_cadastro_pedido_ini_text_num_pedido);
        textDataPedido = (TextView) view.findViewById(R.id.fragment_cadastro_pedido_ini_text_data_pedido);
        textSituacao = (TextView) view.findViewById(R.id.fragment_cadastro_pedido_ini_text_situacao);
        textStatus = (TextView) view.findViewById(R.id.fragment_cadastro_pedido_ini_text_status);
        spinnerCliente = (Spinner) view.findViewById(R.id.fragment_cadastro_pedido_ini_spinner_cliente);
        spinnerTabelaPreco = (Spinner) view.findViewById(R.id.fragment_cadastro_pedido_ini_spinner_tabela_preco);
        spinnerCondPgto = (Spinner) view.findViewById(R.id.fragment_cadastro_pedido_fim_spinner_condicao_pgto);
        editObservacao = (EditText) view.findViewById(R.id.fragment_cadastro_pedido_ini_edit_observacao);

        ferramentas = new Ferramentas();

        //buttonProx.setOnClickListener(this);

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

            try {
                salvaDados();
            } catch (Exceptions exceptions) {
                ferramentas.customToast(getActivity(),exceptions.getMessage());
            }
            /*
            CadastroPedidoItemFragment cadastroPedidoItemFragment = new CadastroPedidoItemFragment();
            //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.activity_cadastro_pedido_container,cadastroPedidoItemFragment);
            getActivity().getFragmentManager().beginTransaction().replace(R.id.activity_cadastro_pedido_container,cadastroPedidoItemFragment).commit();
            */
        }
    }

    //Metodo para carregar informação ao abriar a Activity.
    private void loadDataFromActivity() {
        Ferramentas ferramentas = new Ferramentas();

        //Popula Spinner de tabela de preços
        TabelaPreco tabelaPreco = new TabelaPreco();
        tabelaPreco.setCod("");
        tabelaPreco.setDescricao("Selecione");

        TabelaPrecoDAO tabelaPrecoDAO = TabelaPrecoDAO.getInstance(getActivity());
        listaTabelasPreco = tabelaPrecoDAO.buscaTabelasPreco();
        listaTabelasPreco.add(0,tabelaPreco);
        listaTabelaPrecosSpinnerAdapter = new ListaTabelaPrecosSpinnerAdapter(getActivity(), listaTabelasPreco);
        spinnerTabelaPreco.setAdapter(listaTabelaPrecosSpinnerAdapter);

        //Popula Spinner de clientes
        Cliente cliente = new Cliente();
        cliente.setCnpj("");
        cliente.setRazaoSocial("Selecione");

        ClienteDAO clienteDAO = ClienteDAO.getInstance(getActivity());
        listaClientes = clienteDAO.buscaClientes();
        listaClientes.add(0,cliente);
        listaClientesSpinnerAdapter = new ListaClientesSpinnerAdapter(getActivity(),listaClientes);
        spinnerCliente.setAdapter(listaClientesSpinnerAdapter);

        CondicaoPgtoDAO condicaoPgtoDAO = CondicaoPgtoDAO.getInstance(getActivity());
        listaCondicoesPgto = condicaoPgtoDAO.buscaCondicaoPgto();
        CondicaoPagamento condicaoPagamento = new CondicaoPagamento();
        condicaoPagamento.setCod("");
        condicaoPagamento.setDescricao("Selecione");
        listaCondicoesPgto.add(0, condicaoPagamento);
        if (listaCondicoesPgto != null) {
            listaCondPgtoSpinnerAdapter = new ListaCondPgtoSpinnerAdapter(getActivity(), listaCondicoesPgto);
            spinnerCondPgto.setAdapter(listaCondPgtoSpinnerAdapter);
        }

        pedido = comunicadorCadastroPedido.getPedido();

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

            spinnerTabelaPreco.setSelection(listaTabelaPrecosSpinnerAdapter.getPosition(pedido.getTabelaPreco()));
            spinnerCliente.setSelection(listaClientesSpinnerAdapter.getPosition(pedido.getCliente()));
            spinnerCondPgto.setSelection(listaCondPgtoSpinnerAdapter.getPosition(pedido.getCondicaoPagamento()));
            if (pedido.getObservacao() != null) {
                editObservacao.setText(pedido.getObservacao().toString());
            }
        }else{
            //Se o pedido não existe é porque é novo, neste caso popula os campos de cabeçalho com as informações iniciais
            textSituacao.setText("Pendente");
            textStatus.setText("Em Construção");

        }

    }

    private void salvaDados() throws Exceptions{
        if (spinnerTabelaPreco.getSelectedItem() == null) {
            throw new Exceptions("Tabela de Preços não selecionada.");
        }else if (spinnerCondPgto.getSelectedItem() == null) {
            throw new Exceptions("Condição de pagamento não selecionada.");
        }else if (spinnerCliente.getSelectedItem() == null) {
            throw new Exceptions("Cliente não selecionado.");
        }
        if (pedido == null){
            pedido = new Pedido();
        }

        pedido.setCondicaoPagamento((CondicaoPagamento) spinnerCondPgto.getSelectedItem());
        pedido.setTabelaPreco((TabelaPreco) spinnerTabelaPreco.getSelectedItem());
        pedido.setCliente((Cliente) spinnerCliente.getSelectedItem());
        pedido.setObservacao(editObservacao.getText().toString());

        pedido.setDtAtualizacao(ferramentas.getCurrentDate());

        PedidoDAO pedidoDAO = PedidoDAO.getInstance(getActivity());

        if (pedido != null){
            //Se pedido já existe
            pedidoDAO.atualizaPedido(pedido);
        }else{
            pedido.setStatus(0);
            pedido.setSituacao(0);
            pedido.setDtCriacao(ferramentas.getCurrentDate());
            pedidoDAO.inserePedido(pedido);
        }
        comunicadorCadastroPedido.setPedido(pedido);
    }
}
