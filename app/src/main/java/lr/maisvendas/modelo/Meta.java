package lr.maisvendas.modelo;

public class Meta {

    private Integer id;
    private Double estimado;
    private Double realizado;

    public Meta() {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getEstimado() {
        return estimado;
    }

    public void setEstimado(Double estimado) {
        this.estimado = estimado;
    }

    public Double getRealizado() {
        return realizado;
    }

    public void setRealizado(Double realizado) {
        this.realizado = realizado;
    }
}
