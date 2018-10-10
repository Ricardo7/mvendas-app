package lr.maisvendas.modelo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class    Imagem implements Serializable {

    @JsonIgnore
    private Integer id;
    @JsonProperty("IDWS")
    private String idWS;
    @JsonProperty("Nome")
    private String nome;
    @JsonProperty("Principal")
    private Integer principal;
    @JsonIgnore
    private String caminho;
    @JsonProperty("Base64")
    private String imagem;
    @JsonProperty("ProdutoID")
    private String produtoIdWS;
    @JsonProperty("DtCriacao")
    private String dtCriacao;
    @JsonProperty("DtAtualizacao")
    private String dtAtualizacao;

    public Imagem () {}

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

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getPrincipal() {
        return principal;
    }

    public void setPrincipal(Integer principal) {
        this.principal = principal;
    }

    public String getCaminho() {
        return caminho;
    }

    public void setCaminho(String caminho) {
        this.caminho = caminho;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public String getProdutoIdWS() {
        return produtoIdWS;
    }

    public void setProdutoIdWS(String produtoIdWS) {
        this.produtoIdWS = produtoIdWS;
    }

    public String getDtCriacao() {
        return dtCriacao;
    }

    public void setDtCriacao(String dtCriacao) {
        this.dtCriacao = dtCriacao;
    }

    public String getDtAtualizacao() {
        return dtAtualizacao;
    }

    public void setDtAtualizacao(String dtAtualizacao) {
        this.dtAtualizacao = dtAtualizacao;
    }
}
