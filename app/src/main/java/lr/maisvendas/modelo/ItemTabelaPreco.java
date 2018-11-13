package lr.maisvendas.modelo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ItemTabelaPreco implements Serializable {

    @JsonIgnore
    private Integer id;
    @JsonProperty("VlrUnitario")
    private Double vlrUnitario;
    @JsonProperty("MaxDesconto")
    private Double maxDesc;
    @JsonProperty("Produto")
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ItemTabelaPreco)) return false;
        ItemTabelaPreco that = (ItemTabelaPreco) o;
        return Objects.equals(getId(), that.getId()) &&
                Objects.equals(getVlrUnitario(), that.getVlrUnitario()) &&
                Objects.equals(getMaxDesc(), that.getMaxDesc()) &&
                Objects.equals(getProduto(), that.getProduto());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId(), getVlrUnitario(), getMaxDesc(), getProduto());
    }
}
