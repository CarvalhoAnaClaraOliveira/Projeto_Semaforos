package classes;

import java.util.Random;

public class GeradorPlaca {
    private static final String LETRAS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final Random RANDOM = new Random();

    public static String gerarPlacaMercosul() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 3; i++) sb.append(LETRAS.charAt(RANDOM.nextInt(LETRAS.length())));
        sb.append(RANDOM.nextInt(10));
        sb.append(LETRAS.charAt(RANDOM.nextInt(LETRAS.length())));
        sb.append(RANDOM.nextInt(10));
        sb.append(RANDOM.nextInt(10));
        return sb.toString();
    }
}
