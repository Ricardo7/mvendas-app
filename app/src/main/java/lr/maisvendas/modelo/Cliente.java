package lr.maisvendas.modelo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Cliente implements Serializable {

    @JsonProperty("IDAP")
    private Integer id;
    @JsonProperty("IDWS")
    private String idWS;
    @JsonProperty("Cod")
    private String cod;
    @JsonProperty("Cnpj")
    private String cnpj;
    @JsonProperty("RazaoSocial")
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cliente)) return false;
        Cliente cliente = (Cliente) o;
        if( getId() 				!= null ? !getId().equals(cliente.getId()) : cliente.getId() != null)
            return false;
        if( getIdWS()				!= null ? !getIdWS().equals(cliente.getIdWS()) : cliente.getIdWS() != null)
            return false;
        if( getCod()				!= null ? !getCod().equals(cliente.getCod()) : cliente.getCod() != null)
            return false;
        if( getCnpj()				!= null ? !getCnpj().equals(cliente.getCnpj()) : cliente.getCnpj() != null)
            return false;
        if( getRazaoSocial()		!= null ? !getRazaoSocial().equals(cliente.getRazaoSocial()) : cliente.getRazaoSocial() != null)
            return false;
        if( getNomeFantasia()		!= null ? !getNomeFantasia().equals(cliente.getNomeFantasia()) : cliente.getNomeFantasia() != null)
            return false;
        if( getInscricaoEstadual()	!= null ? !getInscricaoEstadual().equals(cliente.getInscricaoEstadual()) : cliente.getInscricaoEstadual() != null)
            return false;
        if( getEmail()				!= null ? !getEmail().equals(cliente.getEmail()) : cliente.getEmail() != null)
            return false;
        if( getFone()				!= null ? !getFone().equals(cliente.getFone()) : cliente.getFone() != null)
            return false;
        if( getCep()				!= null ? !getCep().equals(cliente.getCep()) : cliente.getCep() != null)
            return false;
        if( getBairro()				!= null ? !getBairro().equals(cliente.getBairro()) : cliente.getBairro() != null)
            return false;
        if( getLogradouro()			!= null ? !getLogradouro().equals(cliente.getLogradouro()) : cliente.getLogradouro() != null)
            return false;
        if( getNumero()				!= null ? !getNumero().equals(cliente.getNumero()) : cliente.getNumero() != null)
            return false;
        if( getStatus()				!= null ? !getStatus().equals(cliente.getStatus()) : cliente.getStatus() != null)
            return false;
        if( getAtivo()				!= null ? !getAtivo().equals(cliente.getAtivo()) : cliente.getAtivo() != null)
            return false;
        if( getDtCadastro()			!= null ? !getDtCadastro().equals(cliente.getDtCadastro()) : cliente.getDtCadastro() != null)
            return false;
        if( getDtAtualizacao()		!= null ? !getDtAtualizacao().equals(cliente.getDtAtualizacao()) : cliente.getDtAtualizacao() != null)
            return false;
        if( getCidade()				!= null ? !getCidade().equals(cliente.getCidade()) : cliente.getCidade() != null)
            return false;
        return getSegmentoMercado()	!= null ? getSegmentoMercado().equals(cliente.getSegmentoMercado()) : cliente.getSegmentoMercado() == null;

    }

    @Override
    public int hashCode() {

        int result = getId()                            != null ? getId().hashCode() : 0;
        result = 31 * result + (getIdWS()				!= null ? getIdWS().hashCode() : 0);
        result = 31 * result + (getCod()                != null ? getCod().hashCode() : 0);
        result = 31 * result + (getCnpj()               != null ? getCnpj().hashCode() : 0);
        result = 31 * result + (getRazaoSocial()        != null ? getRazaoSocial().hashCode() : 0);
        result = 31 * result + (getNomeFantasia()       != null ? getNomeFantasia().hashCode() : 0);
        result = 31 * result + (getInscricaoEstadual()  != null ? getInscricaoEstadual().hashCode() : 0);
        result = 31 * result + (getEmail()              != null ? getEmail().hashCode() : 0);
        result = 31 * result + (getFone()               != null ? getFone().hashCode() : 0);
        result = 31 * result + (getCep()                != null ? getCep().hashCode() : 0);
        result = 31 * result + (getBairro()             != null ? getBairro().hashCode() : 0);
        result = 31 * result + (getLogradouro()         != null ? getLogradouro().hashCode() : 0);
        result = 31 * result + (getNumero()             != null ? getNumero().hashCode() : 0);
        result = 31 * result + (getStatus()             != null ? getStatus().hashCode() : 0);
        result = 31 * result + (getAtivo()              != null ? getAtivo().hashCode() : 0);
        result = 31 * result + (getDtCadastro()         != null ? getDtCadastro().hashCode() : 0);
        result = 31 * result + (getDtAtualizacao()      != null ? getDtAtualizacao().hashCode() : 0);
        result = 31 * result + (getCidade()             != null ? getCidade().hashCode() : 0);
        result = 31 * result + (getSegmentoMercado()    != null ? getSegmentoMercado().hashCode() : 0);

        return result;
    }

}
