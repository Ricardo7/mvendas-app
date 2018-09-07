package lr.maisvendas.modelo;

import java.io.Serializable;
import java.util.List;


public class Pedido implements Serializable{

    private Integer id;
    private Integer numero;
    private Integer situacao;
    private Integer Status;
    private String dtCriacao;
    private String dtAtualizacao;
    private Cliente cliente;
    private CondicaoPgto condicaoPgto;
    private TabelaPreco tabelaPreco;
    private List<ItemPedido> itensPedido;

    //public Pedido () {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
        return Status;
    }

    public void setStatus(Integer status) {
        Status = status;
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
