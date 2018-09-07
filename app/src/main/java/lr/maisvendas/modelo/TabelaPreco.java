package lr.maisvendas.modelo;

import java.io.Serializable;
import java.util.List;

public class TabelaPreco implements Serializable {

    private Integer id;
    private String cod;
    private String descricao;
    private List<ItemTabelaPreco> itensTabelaPreco;

    public TabelaPreco() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public List<ItemTabelaPreco> getItensTabelaPreco() {
        return itensTabelaPreco;
    }

    public void setItensTabelaPreco(List<ItemTabelaPreco> itensTabelaPreco) {
        this.itensTabelaPreco = itensTabelaPreco;
    }
}
