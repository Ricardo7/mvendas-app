package lr.maisvendas.modelo;

import java.io.Serializable;

public class CondicaoPgto implements Serializable {

    private Integer id;
    private String cod;
    private String descricao;
    private Double descAcr;

    public CondicaoPgto() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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
