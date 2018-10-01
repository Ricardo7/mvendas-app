package lr.maisvendas.tela;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;

import java.util.List;

import lr.maisvendas.R;
import lr.maisvendas.modelo.ItemTabelaPreco;
import lr.maisvendas.modelo.Pedido;
import lr.maisvendas.modelo.Produto;
import lr.maisvendas.repositorio.sql.ItemPedidoDAO;
import lr.maisvendas.repositorio.sql.ItemTabelaPrecoDAO;
import lr.maisvendas.repositorio.sql.PedidoDAO;
import lr.maisvendas.repositorio.sql.ProdutoDAO;
import lr.maisvendas.tela.adaptador.ListaProdutosAdapter;
import lr.maisvendas.tela.interfaces.ItemProdutoClickListener;
import lr.maisvendas.utilitarios.Ferramentas;
import lr.maisvendas.utilitarios.StatusPedido;

public class ListaProdutosActivity extends BaseActivity implements ItemProdutoClickListener {

    //Campos da tela
    private ImageButton imagePedidoAdd;

    //Variáveis
    private RecyclerView recyclerViewProdutos;
    private ListaProdutosAdapter listaProdutosAdapter;
    private List<Produto> listaProdutos;
    private Boolean removeItem;


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
        imagePedidoAdd = (ImageButton) findViewById(R.id.linha_lista_produto_button_pedido);

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

        // Colocado para atualizar a lista ao voltar dos Detalhes, caso tenha mexido em alguma coisa no pedido
        if(listaProdutosAdapter != null){
            listaProdutosAdapter.notifyDataSetChanged();
        }
        loadDataFromActivity();
    }

    @Override
    public void onItemProdutoClick(View view, int position) {
        Ferramentas ferramentas = new Ferramentas();

            Produto produto = listaProdutosAdapter.getItem(position);
            //Verifica se tem um pedido no carrinho, em construção (Status = 0 )
            PedidoDAO pedidoDAO = PedidoDAO.getInstance(this);
            Pedido pedido = pedidoDAO.buscaPedidoStatus(StatusPedido.emConstrucao);
        //ferramentas.customLog("RRRRR-PDV",pedido.getId().toString());
        //ferramentas.customLog("RRRRR-PRD",produto.getId().toString());
            //Busca o item da tabela de preço do pedido (Só irá ter valor)
            ItemTabelaPreco itemTabelaPreco = null;
            if (pedido != null) {
                ItemTabelaPrecoDAO itemTabelaPrecoDAO = ItemTabelaPrecoDAO.getInstance(this);
                itemTabelaPreco = itemTabelaPrecoDAO.buscaItemTabelaPrecoPedidoProduto(pedido.getId(), produto.getId());

                //ferramentas.customLog("RRRRR-IT",itemTabelaPreco.getId().toString());
            }

            Intent intent = new Intent(this, DetalhesProdutoActivity.class);
            intent.putExtra(DetalhesProdutoActivity.PARAM_PRODUTO, produto);
            intent.putExtra(DetalhesProdutoActivity.PARAM_PEDIDO, pedido);
            intent.putExtra(DetalhesProdutoActivity.PARAM_ITEM_TABELA_PRECO, itemTabelaPreco);
            startActivity(intent);


    }

    @Override
    public void onAddPedidoClick(View view, int position) {

        Produto produto = (Produto) listaProdutosAdapter.getItem(position);
        Pedido pedido = listaProdutosAdapter.getPedido();
        //Se item estiver no pedido (pedido existe) irá remover. Senão, será tratado para direcionar a tela de inclusão de pedido ou produto.
        if (pedido != null){
            /*VERIFICAR  A NECESSINDADE DE LIMPAR O PEDIDO DO PRODUTO, HOJE NÃO SERÁ LIMPADO
            listaProdutosAdapter.setPedido(null);
            listaProdutosAdapter.setItemTabelaPreco(null);
            */

            if (confirmDialog()){
                ItemPedidoDAO itemPedidoDAO = ItemPedidoDAO.getInstance(this);
                itemPedidoDAO.deletaItemPedidoProduto(pedido.getId(),produto.getId());


                // Informa ao adapter que o conteúdo do array list foi modificado, logo o ListView deve ser atualizado
                if(listaProdutosAdapter != null) {
                    listaProdutosAdapter.notifyDataSetChanged();
                }
            }
        } else{
            //Verifica se existe algum pedido em aberto
            PedidoDAO pedidoDAO = PedidoDAO.getInstance(this);
            pedido = pedidoDAO.buscaPedidoStatus(StatusPedido.emConstrucao);

            Intent intent = null;
            //Se existir pedido aberto, direciona para detalhes de itens para que o mesmo seja adicionado
            if (pedido != null) {
                ItemTabelaPrecoDAO itemTabelaPrecoDAO = ItemTabelaPrecoDAO.getInstance(this);
                ItemTabelaPreco itemTabelaPreco = itemTabelaPrecoDAO.buscaItemTabelaPrecoPedidoProduto(pedido.getId(), produto.getId());

                intent = new Intent(this, DetalhesProdutoActivity.class);
                intent.putExtra(DetalhesProdutoActivity.PARAM_PRODUTO, produto);
                intent.putExtra(DetalhesProdutoActivity.PARAM_PEDIDO, pedido);
                intent.putExtra(DetalhesProdutoActivity.PARAM_ITEM_TABELA_PRECO, itemTabelaPreco);
                startActivity(intent);

                // Informa ao adapter que o conteúdo do array list foi modificado, logo o ListView deve ser atualizado
                if(listaProdutosAdapter != null){
                    listaProdutosAdapter.notifyDataSetChanged();
                }
            }else{
                //Se Não existir pedido aberto, direciona para cadastro de pedido
                intent = new Intent(this,CadastroPedidoActivity.class);
                startActivity(intent);
            }

        }
    }

    //Metodo para carregar informação ao abriar a Activity.
    private void loadDataFromActivity() {


    }

    private Boolean confirmDialog() {
        removeItem = false;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder
                .setMessage("O Item será removido do pedido." +
                        "\n Deseja continuar?")
                .setPositiveButton("Sim",  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                         removeItem = true;
                    }
                })
                .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,int id) {
                        removeItem = false;
                    }
                })
                .show();

        return removeItem;
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
