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
import lr.maisvendas.modelo.Produto;
import lr.maisvendas.tela.interfaces.ComunicadorDetalhesProduto;

public class DetalhesProdutoTabObsFragment extends Fragment {
    private static final String TAG = "DetalhesProdutoTabObsFragment";
    public static String ARG_POSITION = "arg_position";

    //Campos da tela
    private TextView textObs;

    //Variáveis
    private ComunicadorDetalhesProduto comunicadorDetalhesProduto;

    public DetalhesProdutoTabObsFragment(){}

    public static DetalhesProdutoTabObsFragment newInstance(int postion){
        Bundle parameters = new Bundle();
        parameters.putInt(ARG_POSITION, postion);
        DetalhesProdutoTabObsFragment frag = new DetalhesProdutoTabObsFragment();
        frag.setArguments(parameters);
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_detalhes_produto_tab_obs, container, false);

        //Setar cmpos
        textObs = (TextView) view.findViewById(R.id.fragment_detalhes_produto_tab_obs_text_obs);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        comunicadorDetalhesProduto = (ComunicadorDetalhesProduto) activity;
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
        if (produto.getObservacao() != null) {
            textObs.setText(produto.getObservacao());
        }

    }

}
