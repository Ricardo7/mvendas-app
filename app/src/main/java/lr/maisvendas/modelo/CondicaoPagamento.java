package lr.maisvendas.modelo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CondicaoPagamento implements Serializable {

    @JsonIgnore
    private Integer id;
    @JsonProperty("IDWS")
    private String idWS;
    @JsonProperty("Cod")
    private String cod;
    @JsonProperty("Descricao")
    private String descricao;
    @JsonProperty("DescAcr")
    private Double descAcr;

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

    public Double getDescAcr() {
        return descAcr;
    }

    public void setDescAcr(Double descAcr) {
        this.descAcr = descAcr;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CondicaoPagamento)) return false;
        CondicaoPagamento that = (CondicaoPagamento) o;
        return Objects.equals(getId(), that.getId()) &&
                Objects.equals(getIdWS(), that.getIdWS()) &&
                Objects.equals(getCod(), that.getCod()) &&
                Objects.equals(getDescricao(), that.getDescricao()) &&
                Objects.equals(getDescAcr(), that.getDescAcr());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId(), getIdWS(), getCod(), getDescricao(), getDescAcr());
    }
}
