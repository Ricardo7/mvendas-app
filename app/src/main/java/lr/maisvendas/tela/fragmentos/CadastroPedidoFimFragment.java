package lr.maisvendas.tela.fragmentos;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import lr.maisvendas.R;
import lr.maisvendas.modelo.ItemPedido;
import lr.maisvendas.modelo.Pedido;
import lr.maisvendas.repositorio.sql.PedidoDAO;
import lr.maisvendas.tela.interfaces.ComunicadorCadastroPedido;
import lr.maisvendas.utilitarios.Exceptions;
import lr.maisvendas.utilitarios.Ferramentas;

public class CadastroPedidoFimFragment extends Fragment implements View.OnClickListener{

    private static final String TAG = "CadastroPedidoFimFragment";
    public static final String PARAM_PEDIDO_INI = "PARAM_PEDIDO_FIM";
    public static String ARG_POSITION = "arg_position";

    //Campos da tela
    private TextView textVlrTotal;
    private TextView textVlrDescoto;
    private TextView textVlrFinal;
    private Button buttonExcluir;
    private Button buttonEnviar;
    //private Button buttonAnt;

    //Variáveis
    private ComunicadorCadastroPedido comunicadorCadastroPedido;
    private Pedido pedido;
    private Ferramentas ferramentas;

    public CadastroPedidoFimFragment(){};

    public static CadastroPedidoFimFragment newInstance(int postion){
        Bundle parameters = new Bundle();
        parameters.putInt(ARG_POSITION, postion);
        CadastroPedidoFimFragment frag = new CadastroPedidoFimFragment();
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
        View view = inflater.inflate(R.layout.fragment_cadastro_pedido_fim,container,false);

        //Tratar campos da tela aqui
        textVlrTotal = (TextView) view.findViewById(R.id.fragment_cadastro_pedido_fim_text_vlr_total);
        textVlrDescoto = (TextView) view.findViewById(R.id.fragment_cadastro_pedido_fim_text_vlr_desconto);
        textVlrFinal = (TextView) view.findViewById(R.id.fragment_cadastro_pedido_fim_text_vlr_final);
        buttonExcluir = (Button) view.findViewById(R.id.fragment_cadastro_pedido_fim_button_excluir);
        buttonEnviar = (Button) view.findViewById(R.id.fragment_cadastro_pedido_fim_button_enviar);
        /*
        buttonAnt = (Button) view.findViewById(R.id.fragment_cadastro_pedido_fim_button_ant);
        */

        ferramentas = new Ferramentas();
        /*
        buttonAnt.setOnClickListener(this);
        */
        buttonEnviar.setOnClickListener(this);
        buttonExcluir.setOnClickListener(this);


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

        /*if (view == buttonAnt){
            CadastroPedidoItemFragment cadastroPedidoItemFragment = new CadastroPedidoItemFragment();
            //getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.activity_cadastro_pedido_container,cadastroPedidoItemFragment);
            getActivity().getFragmentManager().beginTransaction().replace(R.id.activity_cadastro_pedido_container,cadastroPedidoItemFragment).commit();
        }else */
        if(view == buttonEnviar){

        }else if (view == buttonExcluir){

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            builder
                .setMessage("O Pedido será excluído definitivamente." +
                        "\n Deseja continuar?")
                .setPositiveButton("Sim",  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            excluiDados();
                        } catch (Exceptions ex) {
                            ferramentas.customToast(getActivity(),ex.getMessage());
                        }
                    }
                })
                .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,int id) {
                        return;
                    }
                })
                .show();
        }

    }

    //Metodo para carregar informação ao abriar a Activity.
    private void loadDataFromActivity() {

        Ferramentas ferramentas = new Ferramentas();

        pedido = comunicadorCadastroPedido.getPedido();

        if (pedido != null) {

            textVlrTotal.setText("R$ "+totalTotalPedido(pedido).toString());
            textVlrDescoto.setText("R$ "+totalDescontoPedido(pedido).toString());
            textVlrFinal.setText("R$ "+totalLiquidoPedido(pedido).toString());
        }
    }

    public Double totalTotalPedido(Pedido pedido){

        List<ItemPedido> itensPedido = pedido.getItensPedido();
        Double total = 0.0;

        for (ItemPedido itemPedido:itensPedido) {
            total = total + itemPedido.getVlrTotal() + itemPedido.getVlrDesconto();
        }

        return total;
    }

    public Double totalDescontoPedido(Pedido pedido){

        List<ItemPedido> itensPedido = pedido.getItensPedido();
        Double desconto = 0.0;

        for (ItemPedido itemPedido:itensPedido) {
            desconto = desconto + itemPedido.getVlrDesconto();
        }

        return desconto;
    }

    public Double totalLiquidoPedido(Pedido pedido){

        List<ItemPedido> itensPedido = pedido.getItensPedido();
        Double liquido = 0.0;

        for (ItemPedido itemPedido:itensPedido) {
            liquido = liquido + itemPedido.getVlrTotal();
        }

        return liquido;
    }

    public void salvaDados() throws Exceptions{

        pedido.setDtAtualizacao(ferramentas.getCurrentDate());
        pedido.setStatus(1);

        PedidoDAO pedidoDAO = PedidoDAO.getInstance(getActivity());
        pedidoDAO.atualizaPedido(pedido);

        comunicadorCadastroPedido.setPedido(pedido);

        //Enviar pedido

    }

    private void excluiDados() throws Exceptions {

        if (pedido.getStatus() != 0){
            throw new Exceptions("Pedido Fechado, não pode ser excluído. Para Excluir entre em contato com o Administrador do sistema.");
        }

        PedidoDAO pedidoDAO = PedidoDAO.getInstance(getActivity());
        pedidoDAO.deletaPedido(pedido.getId());


    }
}
