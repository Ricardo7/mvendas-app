package lr.maisvendas.modelo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Cliente implements Serializable {

    @JsonProperty("ID")
    private Integer id;
    @JsonProperty("IDWS")
    private Integer idWS;
    @JsonProperty("Cod")
    private String cod;
    @JsonProperty("Cnpj")
    private String cnpj;
    @JsonProperty("RazaoSocil")
    private String razaoSocial;
    @JsonProperty("NomeFantasia")
    private String nomeFantasia;
    @JsonProperty("InscricaoEstadual")
    private String inscricaoEstadual;
    @JsonProperty("Email")
    private String email;
    @JsonProperty("Fone")
    private String fone;
    @JsonProperty("Cep")
    private Integer cep;
    @JsonProperty("Bairro")
    private String bairro;
    @JsonProperty("Logradouro")
    private String logradouro;
    @JsonProperty("Numero")
    private Integer numero;
    @JsonProperty("Status")
    private Integer status;
    @JsonProperty("Ativo")
    private Integer ativo;
    @JsonProperty("DtCadastro")
    private String dtCadastro;
    @JsonProperty("DtAtualizacao")
    private String dtAtualizacao;
    @JsonProperty("Cidade")
    private Cidade cidade;
    @JsonProperty("SegmentoMercado")
    private SegmentoMercado segmentoMercado;

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

    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public String getNomeFantasia() {
        return nomeFantasia;
    }

    public void setNomeFantasia(String nomeFantasia) {
        this.nomeFantasia = nomeFantasia;
    }

    public String getInscricaoEstadual() {
        return inscricaoEstadual;
    }

    public void setInscricaoEstadual(String inscricaoEstadual) {
        this.inscricaoEstadual = inscricaoEstadual;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFone() {
        return fone;
    }

    public void setFone(String fone) {
        this.fone = fone;
    }

    public Integer getCep() {
        return cep;
    }

    public void setCep(Integer cep) {
        this.cep = cep;
    }

    public Cidade getCidade() {
        return cidade;
    }

    public void setCidade(Cidade cidade) {
        this.cidade = cidade;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getAtivo() {
        return ativo;
    }

    public void setAtivo(Integer ativo) {
        this.ativo = ativo;
    }

    public SegmentoMercado getSegmentoMercado() {
        return segmentoMercado;
    }

    public void setSegmentoMercado(SegmentoMercado segmentoMercado) {
        this.segmentoMercado = segmentoMercado;
    }

    public String getDtCadastro() {
        return dtCadastro;
    }

    public void setDtCadastro(String dtCadastro) {
        this.dtCadastro = dtCadastro;
    }

    public String getDtAtualizacao() {
        return dtAtualizacao;
    }

    public void setDtAtualizacao(String dtAtualizacao) {
        this.dtAtualizacao = dtAtualizacao;
    }
}
