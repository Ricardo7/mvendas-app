package lr.maisvendas.servico;

import lr.maisvendas.modelo.Response;
import lr.maisvendas.modelo.Usuario;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface UsuarioServico {

    //@FormUrlEncoded
    @GET("Usuario/ValidarUsuario")
    public Call<Response<Usuario>> logarApp(@Query("email") String email, @Query("senha") String password);

    //Eventos
    @GET("Usuario/ValidarUsuarioToken")
    public Call<Response<Boolean>> verificaToken(@Header("Authorization")  String token);
}
