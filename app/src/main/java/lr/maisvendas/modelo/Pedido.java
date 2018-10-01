package lr.maisvendas.modelo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Pedido implements Serializable{

    @JsonProperty("IDAP")
    private Integer id;
    @JsonProperty("IDWS")
    private String idWS;
    @JsonProperty("Numero")
    private Integer numero;
    @JsonProperty("Situacao")
    private Integer situacao;
    @JsonProperty("Status")
    private Integer status;
    @JsonProperty("Observacao")
    private String observacao;
    @JsonProperty("DtCriacao")
    private String dtCriacao;
    @JsonProperty("DtAtualizacao")
    private String dtAtualizacao;
    @JsonProperty("Cliente")
    private Cliente cliente;
    @JsonProperty("CondicaoPgto")
    private CondicaoPgto condicaoPgto;
    @JsonProperty("TabelaPreco")
    private TabelaPreco tabelaPreco;
    @JsonProperty("ItensPedido")
    private List<ItemPedido> itensPedido;

    //public Pedido () {}

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

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public Integer getSituacao() {
        return situacao;
    }

    public void setSituacao(Integer situacao) {
        this.situacao = situacao;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
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

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public CondicaoPgto getCondicaoPgto() {
        return condicaoPgto;
    }

    public void setCondicaoPgto(CondicaoPgto condicaoPgto) {
        this.condicaoPgto = condicaoPgto;
    }

    public TabelaPreco getTabelaPreco() {
        return tabelaPreco;
    }

    public void setTabelaPreco(TabelaPreco tabelaPreco) {
        this.tabelaPreco = tabelaPreco;
    }

    public List<ItemPedido> getItensPedido() {
        return itensPedido;
    }

    public void setItensPedido(List<ItemPedido> itensPedido) {
        this.itensPedido = itensPedido;
    }
}
