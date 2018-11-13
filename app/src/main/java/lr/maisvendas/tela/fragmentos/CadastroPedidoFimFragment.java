package lr.maisvendas.tela.fragmentos;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import lr.maisvendas.R;
import lr.maisvendas.comunicacao.pedido.CadastrarPedidoCom;
import lr.maisvendas.comunicacao.pedido.EditarPedidoCom;
import lr.maisvendas.modelo.ItemPedido;
import lr.maisvendas.modelo.Pedido;
import lr.maisvendas.repositorio.sql.PedidoDAO;
import lr.maisvendas.tela.interfaces.ComunicadorCadastroPedido;
import lr.maisvendas.utilitarios.Exceptions;
import lr.maisvendas.utilitarios.Ferramentas;

public class CadastroPedidoFimFragment extends Fragment implements View.OnClickListener, CadastrarPedidoCom.CadastrarPedidoTaskCallBack, EditarPedidoCom.EditarPedidoTaskCallBack {

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
    public ProgressDialog progressDialog;

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
            enviarPedido();
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

            if(pedido.getStatus() != 0){
                //Caso o pedido já tenha sido enviado ao servidor o botão 'Excluir' deverá ser ocultado.
                buttonExcluir.setVisibility(View.INVISIBLE);
            }
            if (pedido.getSituacao() != 0){
                //Se o pedido já tenha sido fechado o botão Enviar' deverá ser ocultado.
                buttonEnviar.setVisibility(View.INVISIBLE);
            }
        }

    }

    public Double totalTotalPedido(Pedido pedido){

        List<ItemPedido> itensPedido = pedido.getItensPedido();
        Double total = 0.0;

        for (ItemPedido itemPedido:itensPedido) {
            total = total + itemPedido.getVlrTotal() + itemPedido.getVlrDesconto();
        }

        BigDecimal bd = new BigDecimal(total).setScale(2, RoundingMode.HALF_EVEN);
        return bd.doubleValue();
    }

    public Double totalDescontoPedido(Pedido pedido){

        List<ItemPedido> itensPedido = pedido.getItensPedido();
        Double desconto = 0.0;

        for (ItemPedido itemPedido:itensPedido) {
            desconto = desconto + itemPedido.getVlrDesconto();
        }

        BigDecimal bd = new BigDecimal(desconto).setScale(2, RoundingMode.HALF_EVEN);
        return bd.doubleValue();
    }

    public Double totalLiquidoPedido(Pedido pedido){

        List<ItemPedido> itensPedido = pedido.getItensPedido();
        Double liquido = 0.0;

        for (ItemPedido itemPedido:itensPedido) {
            liquido = liquido + itemPedido.getVlrTotal();
        }

        BigDecimal bd = new BigDecimal(liquido).setScale(2, RoundingMode.HALF_EVEN);
        return bd.doubleValue();
    }

    public void enviarPedido(){

        //Se não foi sincronizado ainda o pedido será inserido caso contrário, será atualizado
        if(pedido.getStatus() == 0 && pedido.getIdWS().equals("")) {
            progressDialog = ProgressDialog.show(getActivity(), "Aguarde", "Enviando pedido, por favor aguarde", true, false);
            //Enviar pedido
            new CadastrarPedidoCom(this).execute(pedido);
        }else{
            progressDialog = ProgressDialog.show(getActivity(), "Aguarde", "Atualizando pedido, por favor aguarde", true, false);
            new EditarPedidoCom(this).execute(pedido);
        }

    }

    private void excluiDados() throws Exceptions {

        if (pedido.getStatus() != 0){
            throw new Exceptions("Pedido Fechado, não pode ser excluído. Para Excluir entre em contato com o Administrador do sistema.");
        }

        PedidoDAO pedidoDAO = PedidoDAO.getInstance(getActivity());
        pedidoDAO.deletaPedido(pedido.getId());


    }

    @Override
    public void onCadastrarPedidoSuccess(Pedido pedido) {

        //Atualiza o módulo principalmente para setar o IDWS, caso ainda não tenha ocorrido
        PedidoDAO pedidoDAO = PedidoDAO.getInstance(getActivity());
        try {
            pedidoDAO.atualizaPedido(pedido);
        } catch (Exceptions ex) {
            ferramentas.customLog(TAG,ex.getMessage());
        }

        progressDialog.dismiss();
        ferramentas.customToast(getActivity(),"Pedido enviado com sucesso.");
        ferramentas.customLog(TAG,"Inseriu PEDIDO id ("+pedido.getIdWS()+")");

        comunicadorCadastroPedido.setPedido(pedido);
    }

    @Override
    public void onCadastrarPedidoFailure(String mensagem) {

        progressDialog.dismiss();
    }

    @Override
    public void onEditarPedidoSuccess(Pedido pedido) {

        //Atualiza o módulo principalmente para setar o IDWS, caso ainda não tenha ocorrido
        PedidoDAO pedidoDAO = PedidoDAO.getInstance(getActivity());
        try {
            pedidoDAO.atualizaPedido(pedido);
        } catch (Exceptions ex) {
            ferramentas.customLog(TAG,ex.getMessage());
        }

        progressDialog.dismiss();
        ferramentas.customToast(getActivity(),"Pedido atualizado com sucesso.");
        ferramentas.customLog(TAG,"Atualizou PEDIDO id ("+pedido.getIdWS()+")");

        comunicadorCadastroPedido.setPedido(pedido);
    }

    @Override
    public void onEditarPedidoFailure(String mensagem) {
        progressDialog.dismiss();
    }
}
