package lr.maisvendas.modelo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SegmentoMercado implements Serializable {

    @JsonIgnore
    private Integer id;
    @JsonProperty("IDWS")
    private String idWS;
    @JsonProperty("Descricao")
    private String descricao;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SegmentoMercado)) return false;
        SegmentoMercado that = (SegmentoMercado) o;
        return Objects.equals(getId(), that.getId()) &&
                Objects.equals(getIdWS(), that.getIdWS()) &&
                Objects.equals(getDescricao(), that.getDescricao());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId(), getIdWS(), getDescricao());
    }
}
