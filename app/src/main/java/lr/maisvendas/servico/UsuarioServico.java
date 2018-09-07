package lr.maisvendas.servico;

import lr.maisvendas.modelo.Response;
import lr.maisvendas.modelo.Usuario;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface UsuarioServico {

    @FormUrlEncoded
    @POST("login")
    public Call<Response<Usuario>> logarApp(@Field("email") String email, @Field("password") String password);

    //Eventos
    @POST("mantemLogin")
    public Call<Response<Boolean>> verificaToken(@Header("X-Auth-Token")  String token);
}
