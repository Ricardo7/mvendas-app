package lr.maisvendas.modelo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SegmentoMercado implements Serializable {

    @JsonProperty("ID")
    private Integer id;
    @JsonProperty("IDWS")
    private Integer idWS;
    @JsonProperty("Descricao")
    private String descricao;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdWS() {
        return idWS;
    }

    public void setIdWS(Integer idWS) {
        this.idWS = idWS;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
