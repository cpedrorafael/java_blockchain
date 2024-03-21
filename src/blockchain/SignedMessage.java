package blockchain;

import java.security.PublicKey;
import java.security.Signature;

public class SignedMessage {
    private final int id;
    private final String text;
    private final byte[] signature;
    private final PublicKey publicKey;

    public SignedMessage(int id, String text, byte[] signature, PublicKey publicKey) {
        this.id = id;
        this.text = text;
        this.signature = signature;
        this.publicKey = publicKey;
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public byte[] getSignature() {
        return signature;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    /**
     * Verifies the signature of this message.
     *
     * @return true if the signature is valid, false otherwise.
     */
    public boolean verifySignature() {
        try {
            Signature sig = Signature.getInstance("SHA256withRSA");
            sig.initVerify(publicKey);
            sig.update((id + text).getBytes());
            return sig.verify(signature);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public String toString() {
        return text;
    }

    /**
     * Converts a byte array to a hexadecimal string.
     *
     * @param bytes The byte array.
     * @return The hexadecimal string representation of the byte array.
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
