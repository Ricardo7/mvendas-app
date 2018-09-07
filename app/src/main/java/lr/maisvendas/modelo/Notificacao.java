package lr.maisvendas.modelo;

public class Notificacao {

    private Integer id;
    private String titulo;
    private String mensagem;
    private String dtSinc;

    public Notificacao () {}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getDtSinc() {
        return dtSinc;
    }

    public void setDtSinc(String dtSinc) {
        this.dtSinc = dtSinc;
    }
}
