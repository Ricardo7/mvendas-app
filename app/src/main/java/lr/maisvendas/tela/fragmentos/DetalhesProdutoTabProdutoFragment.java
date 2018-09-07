package lr.maisvendas.tela.fragmentos;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import lr.maisvendas.R;
import lr.maisvendas.modelo.ItemTabelaPreco;
import lr.maisvendas.modelo.Pedido;
import lr.maisvendas.modelo.Produto;
import lr.maisvendas.tela.interfaces.ComunicadorCadastroPedido;
import lr.maisvendas.tela.interfaces.ComunicadorDetalhesProduto;

public class DetalhesProdutoTabProdutoFragment extends Fragment {

    private static final String TAG = "DetalhesProdutoTabProdutoFragment";
    public static String ARG_POSITION = "arg_position";

    //Campos da tela
    private TextView textCodigo;
    private TextView textDescricao;
    private TextView textValor;

    //Variáveis
    private ComunicadorDetalhesProduto comunicadorDetalhesProduto;
    private ComunicadorCadastroPedido comunicadorCadastroPedido;

    public DetalhesProdutoTabProdutoFragment(){}

    public static DetalhesProdutoTabProdutoFragment newInstance(int postion){
        Bundle parameters = new Bundle();
        parameters.putInt(ARG_POSITION, postion);
        DetalhesProdutoTabProdutoFragment frag = new DetalhesProdutoTabProdutoFragment();
        frag.setArguments(parameters);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_detalhes_produto_tab_produto, container, false);

        textCodigo = (TextView) view.findViewById(R.id.fragment_detalhes_produto_tab_text_codigo);
        textDescricao = (TextView) view.findViewById(R.id.fragment_detalhes_produto_tab_text_desc);
        textValor = (TextView) view.findViewById(R.id.fragment_detalhes_produto_tab_text_vlr_unit);


        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        comunicadorDetalhesProduto = (ComunicadorDetalhesProduto) activity;
        comunicadorCadastroPedido  = (ComunicadorCadastroPedido) activity;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        loadDataFromActivity();

    }

    //Metodo para carregar informação ao abriar a Activity.
    private void loadDataFromActivity() {

        Produto produto = comunicadorDetalhesProduto.getProduto();
        ItemTabelaPreco itemTabelaPreco = comunicadorDetalhesProduto.getItemTabelaPreco();
        Pedido pedido = comunicadorCadastroPedido.getPedido();
        textCodigo.setText(produto.getCod());
        textDescricao.setText(produto.getDescricao());
        if (itemTabelaPreco != null) {
            if (itemTabelaPreco.getVlrUnitario() != null) {
                textValor.setText(itemTabelaPreco.getVlrUnitario().toString());
            }else{
                textValor.setText("-");
            }
        }


    }
}
