package lr.maisvendas.modelo;

import java.io.Serializable;

public class ItemTabelaPreco implements Serializable {

    private Integer id;
    private Double vlrUnitario;
    private Double maxDesc;
    private Produto produto;

    public ItemTabelaPreco() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getVlrUnitario() {
        return vlrUnitario;
    }

    public void setVlrUnitario(Double vlrUnitario) {
        this.vlrUnitario = vlrUnitario;
    }

    public Double getMaxDesc() {
        return maxDesc;
    }

    public void setMaxDesc(Double maxDesc) {
        this.maxDesc = maxDesc;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }
}
