package br.com.sosescolar.key;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Encoders;
import javax.crypto.SecretKey;

public class KeyGenerator {
    public static void main(String[] args) {
        // Cria uma nova chave segura de 256 bits, garantida para funcionar com HS256
        SecretKey key = Jwts.SIG.HS256.key().build();

        // Codifica a chave como uma string Base64 para que você possa colocá-la no seu arquivo de propriedades
        String base64Key = Encoders.BASE64.encode(key.getEncoded());

        System.out.println("--- Sua nova jwt.secret segura ---");
        System.out.println(base64Key);
    }
}