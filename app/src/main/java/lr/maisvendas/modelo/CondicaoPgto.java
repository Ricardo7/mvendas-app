package lr.maisvendas.modelo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CondicaoPgto implements Serializable {

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

    public CondicaoPgto() {}

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
}
