package lr.maisvendas.modelo;

import java.io.Serializable;

public class Pais implements Serializable {

    private Integer id;
    private String descricao;
    private String sigla;

    public Pais () {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }
}
