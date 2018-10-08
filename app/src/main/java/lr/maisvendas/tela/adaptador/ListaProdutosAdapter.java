package lr.maisvendas.tela.adaptador;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import lr.maisvendas.R;
import lr.maisvendas.modelo.Imagem;
import lr.maisvendas.modelo.ItemTabelaPreco;
import lr.maisvendas.modelo.Pedido;
import lr.maisvendas.modelo.Produto;
import lr.maisvendas.repositorio.sql.ItemTabelaPrecoDAO;
import lr.maisvendas.repositorio.sql.PedidoDAO;
import lr.maisvendas.tela.interfaces.ItemProdutoClickListener;
import lr.maisvendas.utilitarios.Ferramentas;
import lr.maisvendas.utilitarios.StatusPedido;

public class ListaProdutosAdapter extends RecyclerView.Adapter<ListaProdutosAdapter.ProdutoViewHolder> {//implements Filterable {

    private Context context;
    private List<Produto> itens;
    private List<Pedido> pedidos;
    private List<ItemTabelaPreco> itensTabelaPreco;
    private Object lock = new Object();
    //private ProdutoFilter filter = new ProdutoFilter();
    private Ferramentas ferramentas;
    private static ItemProdutoClickListener itemProdutoClickListener;
    //private Pedido pedido;
    //private ItemTabelaPreco itemTabelaPreco;

    public ListaProdutosAdapter(Context context, List<Produto> Produtos) {
        this.context = context;
        this.itens = Produtos;
        pedidos = new ArrayList<>();
        itensTabelaPreco = new ArrayList<>();
        ferramentas = new Ferramentas();
    }

    @Override
    public ProdutoViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.linha_lista_produtos, viewGroup, false);
        return new ProdutoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ProdutoViewHolder viewHolder, int i) {
        Produto produto = itens.get(i);
        Pedido pedido;
        ItemTabelaPreco itemTabelaPreco=null;
        viewHolder.textCodigoDesc.setText("("+produto.getCod()+") "+produto.getDescricao());

        //------------------------------------------------------------------
        //Popula as variáveis com informações do pedido.
        //Verifica se tem um pedido no carrinho e se o produto já está no carrinho, em construção (Status = 0 )
        PedidoDAO pedidoDAO = PedidoDAO.getInstance(context);
        pedido = pedidoDAO.buscaPedidoStatusProduto(StatusPedido.emConstrucao,produto.getId());
        ItemTabelaPrecoDAO itemTabelaPrecoDAO = ItemTabelaPrecoDAO.getInstance(context);

        if (pedido != null){

            //Busca o item da tabela de preço do pedido
            itemTabelaPreco = itemTabelaPrecoDAO.buscaItemTabelaPrecoPedidoProduto(pedido.getId(),produto.getId());

            viewHolder.imageAddPedido.setBackgroundResource(R.mipmap.ic_pedido_remov);
        }else{
            //Como o produto em questão ainda não está no carrinho busca o pedido do carrinho
            Pedido pedidoCarrinho = pedidoDAO.buscaPedidoStatus(StatusPedido.emConstrucao);

            if(pedidoCarrinho != null){
                //Busca o item da tabela de preço do pedido
                itemTabelaPreco = itemTabelaPrecoDAO.buscaItemTabelaPrecoPedidoProduto(pedidoCarrinho.getId(),produto.getId());
            }
            viewHolder.imageAddPedido.setBackgroundResource(R.mipmap.ic_pedido_add);
        }

        if (itemTabelaPreco != null) {

            viewHolder.textVlrUnit.setText(itemTabelaPreco.getVlrUnitario().toString());
        }else {
            viewHolder.textVlrUnit.setText("-");
        }
        //Popula na lista mesmo sendo null, pois será utilizado nos testes posteriormente se é null ou não
        itensTabelaPreco.add(i,itemTabelaPreco);
        pedidos.add(i,pedido);
        //------------------------------------------------------------------

        String caminhoImagem = null;
        for (Imagem imagem:produto.getImagens()) {
            if (imagem.getPrincipal() == 1){
                caminhoImagem = imagem.getCaminho();
            }
        }

        if (caminhoImagem != null) {
            File imgFile = new  File(caminhoImagem);

            Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

            if (bitmap != null) {
                Bitmap reduzirBitmap = Bitmap.createScaledBitmap(bitmap, 200, 200, true);
                viewHolder.campoImagem.setImageBitmap(reduzirBitmap);
                viewHolder.campoImagem.setScaleType(ImageView.ScaleType.FIT_XY);
            }
        }
    }

    @Override
    public int getItemCount() {
        return itens.size();
    }

    public void setClickListener(ItemProdutoClickListener itemProdutoClickListener) {
        this.itemProdutoClickListener = itemProdutoClickListener;
    }


    protected static class ProdutoViewHolder  extends RecyclerView.ViewHolder implements View.OnClickListener{

        protected TextView textCodigoDesc;
        protected TextView textVlrUnit;
        protected ImageView campoImagem;
        protected ImageButton imageAddPedido;

        public ProdutoViewHolder(View itemView) {
            super(itemView);
            textCodigoDesc = (TextView) itemView.findViewById(R.id.linha_lista_produtos_codigo_desc);
            textVlrUnit = (TextView) itemView.findViewById(R.id.linha_lista_produtos_vlr_unit);
            campoImagem = (ImageView) itemView.findViewById(R.id.linha_lista_produtos_imagem_produto);
            imageAddPedido = (ImageButton) itemView.findViewById(R.id.linha_lista_produto_button_pedido);
            itemView.setTag(itemView);
            itemView.setOnClickListener(this);
            imageAddPedido.setOnClickListener(this);

        }

            @Override
            public void onClick(View view) {
               if (view == imageAddPedido){
                   itemProdutoClickListener.onAddPedidoClick(view,getAdapterPosition());
               }else{
                   itemProdutoClickListener.onItemProdutoClick(view,getAdapterPosition());
               }

            }

    }

    public Produto getItem(int position)
    {
        return itens.get(position);
    }

    public Pedido getPedido(int position){
        return pedidos.get(position);
    }

    public void setPedido(int position, Pedido pedido){
        this.pedidos.add(position,pedido);
    }

    public ItemTabelaPreco getItemTabelaPreco(int position){
        return itensTabelaPreco.get(position);
    }

    public void setItemTabelaPreco(int position,ItemTabelaPreco itemTabelaPreco){
        this.itensTabelaPreco.add(position,itemTabelaPreco);
    }
    /*
    @Override
    public Filter getFilter() {
        return filter;
    }

    //Verificar possíbilidade de melhorar código
    private class ProdutoFilter extends Filter {

        private ArrayList<Produto> originalItens;

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (originalItens == null) {
                synchronized (lock) {
                    originalItens = new ArrayList<Produto>(itens);
                }
            }

            if (constraint == null || constraint.length() == 0) {
                ArrayList<Produto> list = new ArrayList<Produto>(originalItens);

                results.count = list.size();
                results.values = list;
            } else {
                ArrayList<Produto> newValues = new ArrayList<Produto>(originalItens.size());

                String prefix = constraint.toString().toLowerCase();
                for (Produto Produto : originalItens) {
                    //Aqui filtro o objeto Produto
                    if (Produto.getCod().toString().contains(prefix)) {
                        newValues.add(Produto);
                    } else if(Produto.getDescricao().toLowerCase().contains(prefix)){
                        newValues.add(Produto);
                    }
                }

                results.count = newValues.size();
                results.values = newValues;
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.count > 0) {
                itens = (List<Produto>) results.values;
                clear();

                for (Produto Produto : itens)
                    add(Produto);

                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }


    }*/

}
