package lr.maisvendas.tela.adaptador;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.File;
import java.util.List;

import lr.maisvendas.R;
import lr.maisvendas.modelo.Imagem;
import lr.maisvendas.modelo.Produto;
import lr.maisvendas.tela.interfaces.ProdutoSugeridoClickListener;
import lr.maisvendas.utilitarios.Ferramentas;

public class ListaProdutosSugestaoAdapter extends RecyclerView.Adapter<ListaProdutosSugestaoAdapter.ProdutoViewHolder> {//implements Filterable {

    private Context context;
    private List<Produto> itens;
    private Object lock = new Object();
    private Ferramentas ferramentas;
    private static ProdutoSugeridoClickListener produtoSugeridoClickListener;

    public ListaProdutosSugestaoAdapter(Context context, List<Produto> Produtos) {
        this.context = context;
        this.itens = Produtos;
        ferramentas = new Ferramentas();
    }

    @Override
    public ProdutoViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.linha_lista_produtos_sugestao, viewGroup, false);
        return new ProdutoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ProdutoViewHolder viewHolder, int i) {
        Produto produto = itens.get(i);

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

    public void setClickListener(ProdutoSugeridoClickListener produtoSugeridoClickListener) {
        this.produtoSugeridoClickListener = produtoSugeridoClickListener;
    }


    protected static class ProdutoViewHolder  extends RecyclerView.ViewHolder implements View.OnClickListener{

        protected ImageView campoImagem;

        public ProdutoViewHolder(View itemView) {
            super(itemView);
            campoImagem = (ImageView) itemView.findViewById(R.id.linha_lista_produtos_sugestao_imagem_produto);
            itemView.setTag(itemView);
            itemView.setOnClickListener(this);

        }

            @Override
            public void onClick(View view) {
               //if (view == campoImagem){
                   produtoSugeridoClickListener.onProdutoSugeridoClick(view,getAdapterPosition());
               //}
            }

    }

    public Produto getItem(int position)
    {
        return itens.get(position);
    }


}
