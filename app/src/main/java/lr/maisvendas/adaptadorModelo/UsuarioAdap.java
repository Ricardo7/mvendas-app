package lr.maisvendas.adaptadorModelo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import lr.maisvendas.modelo.Usuario;

public class UsuarioAdap {

    private Context context;

    public void UsuarioAdap(Context context) {
        this.context = context;
    }

    public Usuario sqlToUsuario(Cursor cursor) {
        Usuario usuario = new Usuario();
        usuario.setId(cursor.getInt(0));
        usuario.setNome(cursor.getString(1));
        usuario.setEmail(cursor.getString(2));
        usuario.setSenha(cursor.getString(3));
        usuario.setAtivo(cursor.getInt(4));
        usuario.setToken(cursor.getString(5));

        return usuario;
    }

    public ContentValues usuarioToContentValue(Usuario usuario) {

        ContentValues content = new ContentValues();
        content.put("ID", usuario.getId());
        content.put("NOME", usuario.getNome());
        content.put("EMAIL", usuario.getEmail());
        content.put("SENHA", usuario.getSenha());
        content.put("ATIVO", usuario.getAtivo());
        content.put("TOKEN", usuario.getToken());

        return content;
    }

}
