package lr.maisvendas.modelo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TabelaPreco implements Serializable {

    @JsonIgnore
    private Integer id;
    @JsonProperty("IDWS")
    private String idWS;
    @JsonProperty("Cod")
    private String cod;
    @JsonProperty("Descricao")
    private String descricao;
    @JsonProperty("ItensTabelaPrecos")
    private List<ItemTabelaPreco> itensTabelaPreco;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIdWS() {
        return idWS;
    }

    public void setIdWS(String idWS) {
        this.idWS = idWS;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TabelaPreco)) return false;
        TabelaPreco that = (TabelaPreco) o;
        return Objects.equals(getId(), that.getId()) &&
                Objects.equals(getIdWS(), that.getIdWS()) &&
                Objects.equals(getCod(), that.getCod()) &&
                Objects.equals(getDescricao(), that.getDescricao()) &&
                Objects.equals(getItensTabelaPreco(), that.getItensTabelaPreco());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId(), getIdWS(), getCod(), getDescricao(), getItensTabelaPreco());
    }
}
