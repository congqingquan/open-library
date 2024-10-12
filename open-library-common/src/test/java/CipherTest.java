import org.cqq.openlibrary.common.security.asymmetric.AsymmetricAlgorithm;
import org.cqq.openlibrary.common.security.asymmetric.SignAlgorithm;
import org.cqq.openlibrary.common.security.crypto.spec.key.PKCS8;
import org.cqq.openlibrary.common.security.crypto.spec.key.SecretKey;
import org.cqq.openlibrary.common.security.crypto.spec.key.X509;
import org.cqq.openlibrary.common.security.digest.DigestAlgorithm;
import org.cqq.openlibrary.common.security.symmetric.SymmetricAlgorithm;
import org.junit.jupiter.api.Test;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.util.Base64;

public class CipherTest {
    
    public static final String RSA_BASE64_PUBLIC_KEY = """
            MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmq8acKot/81To8mK58d6ouMeUuBnJPbFOh38\
            MafQL00WIHf0S+LnF5JXxm/zbJsr/Wk35hOIeWBJkH+HBFpoEnBfXn9Fhe7pmtGk3gHtg5widvvDuj3r\
            gA/9O2lAdBLiF5zDntF7/drybrAgk1+GIs3N794F9Ru71Y3tMdz9BdHd2oA4P5K+8LUcVv6LaN1w0dwh\
            E5ZNNn+svd4zW0HpgMdEueRTh+9voP0fAon09MakI5JFWTv1qHhYo2VmkVp+oVI+dDoZnHKl808hspSa\
            S/E/Ncc6IwAqMAqpqw61c9aqPBjh0/MY0vpb/EEGtmw01YEILTWUmXZWNkp11OEKcQIDAQAB\
            """;

    public static final String RSA_BASE64_PRIVATE_KEY = """
            MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCarxpwqi3/zVOjyYrnx3qi4x5S4Gc\
            k9sU6Hfwxp9AvTRYgd/RL4ucXklfGb/Nsmyv9aTfmE4h5YEmQf4cEWmgScF9ef0WF7uma0aTeAe2DnC\
            J2+8O6PeuAD/07aUB0EuIXnMOe0Xv92vJusCCTX4Yizc3v3gX1G7vVje0x3P0F0d3agDg/kr7wtRxW/\
            oto3XDR3CETlk02f6y93jNbQemAx0S55FOH72+g/R8CifT0xqQjkkVZO/WoeFijZWaRWn6hUj50Ohmc\
            cqXzTyGylJpL8T81xzojACowCqmrDrVz1qo8GOHT8xjS+lv8QQa2bDTVgQgtNZSZdlY2SnXU4QpxAgM\
            BAAECggEAMr9x4Dak4sB9SUXs8X/LZzc2EqCWllTzfc6ql6vjrDERKlPQbTki8ePMMkk2RpJeC0SC11\
            PfOVryp5p/NFlovWvqjfkZWQh50ZYoDcH6J2yJDhopK6f25Evam6w7PhmX3YFL2IYHtNApK1FTTqZPP\
            ongn2pq7/BKXpSyleJXjJm8KbMga1g7BVqUqB1qjgUKqSKg9GyhUOg5hNejajucuZ5jvaWaHdDJAbqH\
            ZKrp1U+W3mZDEW97PBw0Z0bVLePRS3CpWXy6gijJvqTLgLspYB+42L14jT8io+xBlx581o2zdzvV/lW\
            SF2jv+whfOgeo9crr+Xr6YE/dc3zTN3fDMQKBgQDHiqDWKDIp9ygLY6t+5ZVZzy1sbdI8Q2zH+/PaSx\
            E9BdPbUkwj0HOkt6pCyJ3Ik6ek/CE7K7l5dahLtecBHPQXwjXT81QuCEvhsSzpD2cn0Kh9V+yuGThOX\
            gcpkMiEL7p/vzm/v1shL7JF0lnAtyVClavOc+a1NMpaqaqYiIozZQKBgQDGc1CHSxRB5ZCdPE9Wh6Tn\
            fWYEEA84/w6gQk/6YojdePL9oJYhIFtEMi9CM9Kj2IN/JfOiwBI8Pfa6dUWSzDKwpw5bZ4ZMv5Ug343\
            RXeLaKMiPqePp9Z1s72ZjmBy8GzqWLqGDwjrs7BnWXpK/I+cTbVpCO28X1pDWuv8nsqzYHQKBgGwTQZ\
            m8m67vFtlUWoaYgpPqCog30y6gtkJ6zFDnZ6bNiTm5EVQ0iu4kLCuPzSXeE2bgyH7r58FJYKXhGAUaU\
            XqszcrGeEmWZMMQ22o/1XdTJG7/OEnzjvOjKe3xZSlzUGfwgq7ATfNjigMXM1WV3LB7sFpZnBN5TEBe\
            tO94xKGhAoGAHAZW3fRNVQz9QictMRQQxpwf1v0JmUvzndAfiUV2/IfDEOwnp1/xY40OvXOiEH9X+pG\
            6eUZUjOtXN3euLo/lrKq6zU+uWJJJlg3b8jAnBBX0VM1yIS0NYT/hnrjxfeL+gAzvxDKerayd7XcHlD\
            SWwliUAXxxMNWdOKXJuKpz3EUCgYBBsyZS5nkJNbzXaOiJEfA6nIQuk23Gn2j5mSnkAMJq+0jcerGUN\
            wV1g4zLugcXKxvM0YTL8JuDY6eiTHcyIF9Lq5lh0DlBo4bKuPu8QisezQ7E0OjR7oMFB3kcOmFwotm3\
            PnsKPxn5mxhGgPfuwLdtpetgkXuhl3EthMnYCqdAMw==\
            """;
    
    @Test
    public void encryptDecrypt() throws Exception {
        // 可变测试变量
        String algorithm = SymmetricAlgorithm.Blowfish.getValue();
        Key key = SecretKey.create(algorithm, "CQQ".getBytes(StandardCharsets.UTF_8));
        String content = "CQQ";
        
        // 执行
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encrypt = cipher.doFinal(content.getBytes(StandardCharsets.UTF_8));
        
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decrypt = cipher.doFinal(encrypt);
        System.out.println("blowfish decrypt: " + new String(decrypt, StandardCharsets.UTF_8));
    }
    
    @Test
    public void rsaSignAndVerify() throws Exception {
        String algorithm = AsymmetricAlgorithm.RSA.getValue();
        String signAlgorithm = SignAlgorithm.SHA1withRSA.getValue();
        PublicKey publicKey = X509.publicKey(algorithm, Base64.getDecoder().decode(RSA_BASE64_PUBLIC_KEY.getBytes(StandardCharsets.UTF_8)));
        PrivateKey privateKey = PKCS8.privateKey(algorithm, Base64.getDecoder().decode(RSA_BASE64_PRIVATE_KEY.getBytes(StandardCharsets.UTF_8)));
        String signContent = "CQQ";
        String verifyContent = "CQQ";
        
        // sign
        Signature signature = Signature.getInstance(signAlgorithm);
        signature.initSign(privateKey);
        signature.update(signContent.getBytes(StandardCharsets.UTF_8));
        byte[] sign = signature.sign();
        String signBase64Str = Base64.getEncoder().encodeToString(sign);
        System.out.printf("Sign base64 string: [%s]%n", signBase64Str);
        // verify
        byte[] signBase64Decode = Base64.getDecoder().decode(signBase64Str);
        signature.initVerify(publicKey);
        signature.update(verifyContent.getBytes(StandardCharsets.UTF_8));
        boolean verify = signature.verify(signBase64Decode);
        System.out.printf("Verify > signContent [%s] verifyContent [%s] result [%b]%n", signContent, verifyContent, verify);
    }
    
    @Test
    public void sha1Digest() throws Exception {
        // 可变测试变量
        String digestAlgorithm = DigestAlgorithm.SHA1.getValue();
        String content = "CQQ";
        
        MessageDigest md = MessageDigest.getInstance(digestAlgorithm);
        byte[] messageDigest = md.digest(content.getBytes(StandardCharsets.UTF_8));
        
        StringBuilder hexString = new StringBuilder();
        for (byte b : messageDigest) {
            String hex = Integer.toHexString(0xFF & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        String result = hexString.toString();
        System.out.println(result);
    }
}