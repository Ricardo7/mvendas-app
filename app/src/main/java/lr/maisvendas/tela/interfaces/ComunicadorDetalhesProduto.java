package lr.maisvendas.tela.interfaces;

import lr.maisvendas.modelo.ItemTabelaPreco;
import lr.maisvendas.modelo.Produto;

/**
 * Interface criada para implementar a comunicação entre a activity de detalhes de produto e seus fragmentos.
 * Dando assim, uma possíbilidade dos fragmentos se comunicarem com a interface principal.
 */
public interface ComunicadorDetalhesProduto {
    public Produto getProduto();

    public void setProduto(Produto produto);

    public ItemTabelaPreco getItemTabelaPreco();

    public void setItemTabelaPreco(ItemTabelaPreco itemTabelaPreco);
}
