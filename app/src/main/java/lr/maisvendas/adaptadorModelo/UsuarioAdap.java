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
        usuario.setId(cursor.getInt(cursor.getColumnIndex("ID")));
        usuario.setIdWS(cursor.getString(cursor.getColumnIndex("ID_WS")));
        usuario.setNome(cursor.getString(cursor.getColumnIndex("NOME")));
        usuario.setEmail(cursor.getString(cursor.getColumnIndex("EMAIL")));
        usuario.setSenha(cursor.getString(cursor.getColumnIndex("SENHA")));
        usuario.setAtivo(cursor.getInt(cursor.getColumnIndex("ATIVO")));
        usuario.setToken(cursor.getString(cursor.getColumnIndex("TOKEN")));

        return usuario;
    }

    public ContentValues usuarioToContentValue(Usuario usuario) {

        ContentValues content = new ContentValues();
        content.put("ID", usuario.getId());
        content.put("ID_WS", usuario.getIdWS());
        content.put("NOME", usuario.getNome());
        content.put("EMAIL", usuario.getEmail());
        content.put("SENHA", usuario.getSenha());
        content.put("ATIVO", usuario.getAtivo());
        content.put("TOKEN", usuario.getToken());

        return content;
    }

}
