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
    @JsonProperty("CondicaoPagamento")
    private CondicaoPagamento condicaoPagamento;
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

    public CondicaoPagamento getCondicaoPagamento() {
        return condicaoPagamento;
    }

    public void setCondicaoPagamento(CondicaoPagamento condicaoPagamento) {
        this.condicaoPagamento = condicaoPagamento;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pedido)) return false;
        Pedido pedido = (Pedido) o;

        if (getId() 				!= null ? !getId().equals(pedido.getId()) : pedido.getId() != null)
            return false;
        if (getIdWS()				!= null ? !getIdWS().equals(pedido.getIdWS()) : pedido.getIdWS() != null)
            return false;
        if (getNumero()				!= null ? !getNumero().equals(pedido.getNumero()) : pedido.getNumero() != null)
            return false;
        if (getSituacao()			!= null ? !getSituacao().equals(pedido.getSituacao()) : pedido.getSituacao() != null)
            return false;
        if (getStatus()				!= null ? !getStatus().equals(pedido.getStatus()) : pedido.getStatus() != null)
            return false;
        if (getObservacao()			!= null ? !getObservacao().equals(pedido.getObservacao()) : pedido.getObservacao() != null)
            return false;
        if (getDtCriacao()			!= null ? !getDtCriacao().equals(pedido.getDtCriacao()) : pedido.getDtCriacao() != null)
            return false;
        if (getDtAtualizacao()		!= null ? !getDtAtualizacao().equals(pedido.getDtAtualizacao()) : pedido.getDtAtualizacao() != null)
            return false;
        if (getCliente()			!= null ? !getCliente().equals(pedido.getCliente()) : pedido.getCliente() != null)
            return false;
        if (getCondicaoPagamento()	!= null ? !getCondicaoPagamento().equals(pedido.getCondicaoPagamento()) : pedido.getCondicaoPagamento() != null)
            return false;
        if (getTabelaPreco()		!= null ? !getTabelaPreco().equals(pedido.getTabelaPreco()) : pedido.getTabelaPreco() != null)
            return false;
        return getItensPedido()		!= null ? getItensPedido().equals(pedido.getItensPedido()) : pedido.getItensPedido() == null;

    }

    @Override
    public int hashCode() {

        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getIdWS() != null ? getIdWS().hashCode() : 0);
        result = 31 * result + (getNumero()	!= null ? getNumero().hashCode() : 0);
        result = 31 * result + (getSituacao() != null ? getSituacao().hashCode() : 0);
        result = 31 * result + (getStatus() != null ? getStatus().hashCode() : 0);
        result = 31 * result + (getObservacao() != null ? getObservacao().hashCode() : 0);
        result = 31 * result + (getDtCriacao() != null ? getDtCriacao().hashCode() : 0);
        result = 31 * result + (getDtAtualizacao() != null ? getDtAtualizacao().hashCode() : 0);
        result = 31 * result + (getCliente() != null ? getCliente().hashCode() : 0);
        result = 31 * result + (getCondicaoPagamento() != null ? getCondicaoPagamento().hashCode() : 0);
        result = 31 * result + (getTabelaPreco() != null ? getTabelaPreco().hashCode() : 0);
        result = 31 * result + (getItensPedido() != null ? getItensPedido().hashCode() : 0);

        return result;
    }
}
