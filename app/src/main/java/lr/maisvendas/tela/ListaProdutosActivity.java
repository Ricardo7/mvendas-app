package lr.maisvendas.tela;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.View;

import java.util.List;

import lr.maisvendas.R;
import lr.maisvendas.modelo.ItemTabelaPreco;
import lr.maisvendas.modelo.Pedido;
import lr.maisvendas.modelo.Produto;
import lr.maisvendas.repositorio.sql.ItemTabelaPrecoDAO;
import lr.maisvendas.repositorio.sql.PedidoDAO;
import lr.maisvendas.repositorio.sql.ProdutoDAO;
import lr.maisvendas.tela.adaptador.ListaProdutosAdapter;
import lr.maisvendas.tela.interfaces.ItemProdutoClickListener;
import lr.maisvendas.utilitarios.StatusPedido;

public class ListaProdutosActivity extends BaseActivity implements ItemProdutoClickListener {

    private RecyclerView recyclerViewProdutos;
    private ListaProdutosAdapter listaProdutosAdapter;
    private List<Produto> listaProdutos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_lista_produtos);
        setDrawerLayoutId(R.id.activity_lista_produtos_drawer);
        setToolbarId(R.id.toolbar);
        setIcMenuId(R.drawable.ic_menus);
        setNavigationViewId(R.id.navigation_view);
        super.onCreate(savedInstanceState);

        setTitle("Produtos");

        recyclerViewProdutos = (RecyclerView) findViewById(R.id.activity_lista_produtos_list_view);

        recyclerViewProdutos.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewProdutos.setLayoutManager(layoutManager);


        ProdutoDAO produtoDAO = ProdutoDAO.getInstance(this);
        listaProdutos = produtoDAO.buscaProdutos();
        listaProdutosAdapter = new ListaProdutosAdapter(this,listaProdutos);
        recyclerViewProdutos.setAdapter(listaProdutosAdapter);

        listaProdutosAdapter.setClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDataFromActivity();
    }

    @Override
    public void onClick(View view, int position) {

        Produto produto = listaProdutosAdapter.getItem(position);
        //Verifica se tem um pedido no carrinho, em construção (Status = 0 )
        PedidoDAO pedidoDAO = PedidoDAO.getInstance(this);
        Pedido pedido = pedidoDAO.buscaPedidoStatusProduto(StatusPedido.emConstrucao,produto.getId());

        //Busca o item da tabela de preço do pedido (Só irá ter valor)
        ItemTabelaPreco itemTabelaPreco = null;
        if (pedido != null){
            ItemTabelaPrecoDAO itemTabelaPrecoDAO = ItemTabelaPrecoDAO.getInstance(this);
            itemTabelaPreco = itemTabelaPrecoDAO.buscaItemTabelaPrecoPedidoProduto(pedido.getId(),produto.getId());
        }

        Intent intent = new Intent(this,DetalhesProdutoActivity.class);
        intent.putExtra(DetalhesProdutoActivity.PARAM_PRODUTO,produto);
        intent.putExtra(DetalhesProdutoActivity.PARAM_PEDIDO,pedido);
        intent.putExtra(DetalhesProdutoActivity.PARAM_ITEM_TABELA_PRECO,itemTabelaPreco);
        startActivity(intent);


    }

    //Metodo para carregar informação ao abriar a Activity.
    private void loadDataFromActivity() {


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuSearch().setVisible(true);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        /*
        listaProdutosAdapter.getFilter().filter(newText);
        */
        return super.onQueryTextChange(newText);


    }
}
