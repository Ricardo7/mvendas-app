package lr.maisvendas.utilitarios;

/**
 * Created by Ronaldo on 13/08/2018.
 */

public class Exceptions extends Exception {

    private static final long serialVersionUID = 1149241039409861914L;

    // constrói um objeto com a mensagem passada por parâmetro
    public Exceptions(String msg){
        super(msg);
    }
}
