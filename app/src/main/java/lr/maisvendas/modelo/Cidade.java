package lr.maisvendas.modelo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Cidade implements Serializable {

    @JsonIgnore
    private Integer id;
    @JsonProperty("IDWS")
    private String idWS;
    @JsonProperty("Descricao")
    private String descricao;
    @JsonProperty("Sigla")
    private String sigla;
    @JsonProperty("Estado")
    private Estado estado;

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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cidade)) return false;
        Cidade cidade = (Cidade) o;
        return Objects.equals(getId(), cidade.getId()) &&
                Objects.equals(getIdWS(), cidade.getIdWS()) &&
                Objects.equals(getDescricao(), cidade.getDescricao()) &&
                Objects.equals(getSigla(), cidade.getSigla()) &&
                Objects.equals(getEstado(), cidade.getEstado());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId(), getIdWS(), getDescricao(), getSigla(), getEstado());
    }
}
