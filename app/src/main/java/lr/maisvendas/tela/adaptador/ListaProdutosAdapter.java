package lr.maisvendas.tela.adaptador;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.List;

import lr.maisvendas.R;
import lr.maisvendas.modelo.Imagem;
import lr.maisvendas.modelo.Produto;
import lr.maisvendas.tela.interfaces.ItemProdutoClickListener;
import lr.maisvendas.utilitarios.Ferramentas;

public class ListaProdutosAdapter extends RecyclerView.Adapter<ListaProdutosAdapter.ProdutoViewHolder> {//implements Filterable {

    private Context context;
    private List<Produto> itens;
    private Object lock = new Object();
    //private ProdutoFilter filter = new ProdutoFilter();
    private Ferramentas ferramentas;
    private static ItemProdutoClickListener itemProdutoClickListener;

    public ListaProdutosAdapter(Context context, List<Produto> Produtos) {
        this.context = context;
        this.itens = Produtos;
        ferramentas = new Ferramentas();
    }

    /*
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Ferramentas ferramentas = new Ferramentas();
        View linhaView = convertView;
        TextView textCodigoDesc;
        TextView textVlrUnit;

        //Se a view ainda não foi criada irá criá-la
        if (linhaView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            linhaView = inflater.inflate(R.layout.linha_lista_produtos, parent, false);
        }

        textCodigoDesc = (TextView) linhaView.findViewById(R.id.linha_lista_produtos_codigo_desc);
        textVlrUnit = (TextView) linhaView.findViewById(R.id.linha_lista_produtos_vlr_unit);

        Produto produto = getItem(position);

        textCodigoDesc.setText("("+produto.getCod()+") "+produto.getDescricao());
        //textVlrUnit.setText(produto.get);

        ImageView CampoImagem = (ImageView) linhaView.findViewById(R.id.linha_lista_produtos_imagem_produto);

        String caminhoImagem = null;
        for (Imagem imagem:produto.getImagens()) {
            if (imagem.getPrincipal() == 1){
                 caminhoImagem = imagem.getCaminho();
            }
        }
        ferramentas.customLog("RRRRRR","Caminho: "+caminhoImagem);
        if (caminhoImagem != null) {
            Bitmap bitmap = BitmapFactory.decodeFile(caminhoImagem);
            ferramentas.customLog("RRRRRR","Antes do Entrou no config");
            if (bitmap != null) {
                ferramentas.customLog("RRRRRR","Entrou no config");
                Bitmap reduzirBitmap = Bitmap.createScaledBitmap(bitmap, 200, 200, true);
                CampoImagem.setImageBitmap(reduzirBitmap);
                CampoImagem.setScaleType(ImageView.ScaleType.FIT_XY);
            }
        }

        return linhaView;

    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getView(position, convertView, parent);
    }
*/
    @Override
    public ProdutoViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.linha_lista_produtos, viewGroup, false);
        return new ProdutoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ProdutoViewHolder viewHolder, int i) {
        Produto produto = itens.get(i);
        viewHolder.textCodigoDesc.setText("("+produto.getCod()+") "+produto.getDescricao());
        viewHolder.textVlrUnit.setText("0");


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

        public ProdutoViewHolder(View itemView) {
            super(itemView);
            textCodigoDesc = (TextView) itemView.findViewById(R.id.linha_lista_produtos_codigo_desc);
            textVlrUnit = (TextView) itemView.findViewById(R.id.linha_lista_produtos_vlr_unit);
            campoImagem = (ImageView) itemView.findViewById(R.id.linha_lista_produtos_imagem_produto);
            itemView.setTag(itemView);
            itemView.setOnClickListener(this);

        }

            @Override
            public void onClick(View view) {
                if (itemProdutoClickListener != null) itemProdutoClickListener.onClick(view, getAdapterPosition());
            }


    }

    public Produto getItem(int position)
    {
        return itens.get(position);
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
