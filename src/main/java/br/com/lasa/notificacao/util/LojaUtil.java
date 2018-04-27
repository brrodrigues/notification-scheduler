package br.com.lasa.notificacao.util;

/**
 * Formatacao especifica do numero da loja
 */
public class LojaUtil {

    public static String formatarCodigoLoja(final String numeroLoja){
        String prefix = "L000";
        String numeroLojaNew = prefix.substring(0, 4 - numeroLoja.length()) + numeroLoja;
        return numeroLojaNew;
    }
}
