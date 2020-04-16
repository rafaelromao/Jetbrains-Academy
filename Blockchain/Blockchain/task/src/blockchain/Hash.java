package blockchain;

import java.security.MessageDigest;

public class Hash {
    private String value;

    public Hash(String value) {
        this.value = hash(value);
    }

    private String hash(String input){
        try {
            var digest = MessageDigest.getInstance("SHA-256");
            var hash = digest.digest(input.getBytes("UTF-8"));
            var hexString = new StringBuilder();
            for (var elem: hash) {
                var hex = Integer.toHexString(0xff & elem);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        }
        catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return value;
    }
}
