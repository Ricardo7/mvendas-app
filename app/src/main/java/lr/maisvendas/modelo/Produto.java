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
public class Produto implements Serializable {

    @JsonIgnore
    private Integer id;
    @JsonProperty("IDWS")
    private String idWS;
    @JsonProperty("Cod")
    private String cod;
    @JsonProperty("Descricao")
    private String descricao;
    @JsonProperty("Observacao")
    private String observacao;
    @JsonIgnore
    private List<Imagem> imagens;

    public Produto () {}

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

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public List<Imagem> getImagens() {
        return imagens;
    }

    public void setImagens(List<Imagem> imagens) {
        this.imagens = imagens;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Produto)) return false;
        Produto produto = (Produto) o;
        return Objects.equals(getId(), produto.getId()) &&
                Objects.equals(getIdWS(), produto.getIdWS()) &&
                Objects.equals(getCod(), produto.getCod()) &&
                Objects.equals(getDescricao(), produto.getDescricao()) &&
                Objects.equals(getObservacao(), produto.getObservacao()) &&
                Objects.equals(getImagens(), produto.getImagens());
    }

    @Override
    public int hashCode() {

        return Objects.hash(getId(), getIdWS(), getCod(), getDescricao(), getObservacao(), getImagens());
    }
}
