package lr.maisvendas.modelo;

import java.util.List;

public class Localizacao {

    private Integer id;
    private List<Local> locais;

    public Localizacao () {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Local> getLocais() {
        return locais;
    }

    public void setLocais(List<Local> locais) {
        this.locais = locais;
    }
}
