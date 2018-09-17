package lr.maisvendas.utilitarios;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public abstract class EditTextMask {
    private static final String maskFone8 = "####-####";
    private static final String maskFone9 = "#####-####";
    private static final String maskFone10 = "(##) ####-####";
    private static final String maskFone11 = "(##) #####-####";
    private static final String maskCnpj14 = "##.###.###/####-##";
    private static final String maskCep8 = "#####-###";

    public static String unmask(String s) {
        return s.replaceAll("[^0-9]*", "");
    }

    public static TextWatcher insert(final EditText editText, final String tipo) {
        return new TextWatcher() {
            boolean isUpdating;
            String old = "";

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String str = EditTextMask.unmask(s.toString());
                String mask = getMask(str, tipo);


                String mascara = "";
                if (isUpdating) {
                    old = str;
                    isUpdating = false;
                    return;
                }
                int i = 0;
                for (char m : mask.toCharArray()) {
                    if ((m != '#' && str.length() > old.length()) || (m != '#' && str.length() < old.length() && str.length() != i)) {
                        mascara += m;
                        continue;
                    }

                    try {
                        mascara += str.charAt(i);
                    } catch (Exception e) {
                        break;
                    }
                    i++;
                }
                isUpdating = true;
                editText.setText(mascara);
                editText.setSelection(mascara.length());
            }

            public void beforeTextChanged(CharSequence s, int start, int count,int after) {}
            public void afterTextChanged(Editable s) {}
        };
    }

    private static String getMask(String str, String tipo) {
        String mask = maskFone8;

        switch (tipo) {
            case TipoMask.CNPJ:
                mask = maskCnpj14;
                break;
            case TipoMask.FONE:
                switch (str.length()){
                    case 11:
                        mask = maskFone11;
                        break;
                    case 10:
                        mask = maskFone10;
                        break;
                    case 9:
                        mask = maskFone9;
                        break;
                    case 8:
                        mask = maskFone8;
                        break;
                }
                break;
            case TipoMask.CEP:
                mask = maskCep8;
                break;
        }
        return mask;
    }


}
