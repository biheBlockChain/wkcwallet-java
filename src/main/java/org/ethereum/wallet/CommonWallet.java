package org.ethereum.wallet;

import com.wrbug.wkcwallet.util.JsonHelper;
import org.ethereum.crypto.ECKey;
import org.ethereum.crypto.ECKey.ECDSASignature;
import org.ethereum.crypto.HashUtil;
import org.spongycastle.crypto.RuntimeCryptoException;
import org.spongycastle.crypto.generators.SCrypt;
import org.spongycastle.util.Arrays;
import org.spongycastle.util.encoders.Hex;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Map;
import java.util.UUID;

/**
 * Ethereum Wallet Wrapper
 *
 * @author tongsh
 */
public class CommonWallet implements Wallet {
    /**
     * 在android中 n设置太大了，变小点
     * 之前是 262144
     */
    private static Integer mN = 4096;
    private ECKey _eckey; // Key Store instance

    private CommonWallet(ECKey keystore) {
        // Forbidden create instance directly
        this._eckey = keystore;
    }

    /**
     * Generate a new Wallet
     *
     * @return the Wallet Instance
     */
    public static Wallet generate() {
        ECKey keystore = new ECKey();
        CommonWallet wallet = new CommonWallet(keystore);
        return wallet;
    }

    /**
     * Generate a wallet from private key
     *
     * @param privKeyBytes with byte array format
     * @return the Wallet Instance
     */
    public static Wallet fromPrivateKey(byte[] privKeyBytes) {
        ECKey keystore = ECKey.fromPrivate(privKeyBytes);
        CommonWallet wallet = new CommonWallet(keystore);
        return wallet;
    }

    /**
     * Generate a wallet from public key
     * <p/>
     * <strong>The wallet from public key only be able to verify signature only
     * and cannot do sign</strong>
     *
     * @param publicKeyBytes with byte array format
     * @return the Wallet Instance
     */
    public static Wallet fromPublicKey(byte[] publicKeyBytes) {
        ECKey keystore = ECKey.fromPublicOnly(publicKeyBytes);
        CommonWallet wallet = new CommonWallet(keystore);
        return wallet;
    }

    /**
     * Recover from the key file with version 3
     *
     * @param keyfile_json key file with JSON format
     * @param password     protect password
     * @return recovered wallet instance
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws IOException
     */
    public static Wallet fromV3(String keyfile_json, String password) throws GeneralSecurityException {
        // 1. Parser JSON
        Map json;
        json = JsonHelper.fromJson(keyfile_json, Map.class);
        if (json == null) {
            throw new GeneralSecurityException("Invalid keyfile format");
        }
        // 3. Check Version
        if (!"3.0".equals(json.get("version").toString())) {
            throw new GeneralSecurityException("Invalid Keyfile Version");
        }

        // 4. Extract Values
        String address = json.get("address").toString();
        Map crypto = (Map) (json.containsKey("crypto") ? json.get("crypto") : json.get("Crypto"));
        String cipher = crypto.get("cipher").toString();
        if (!"aes-128-ctr".equalsIgnoreCase(cipher)) {
            throw new NoSuchAlgorithmException("Invalid Algorithm");
        }
        String ciphertext = crypto.get("ciphertext").toString();
        Map cipherparams = (Map) (crypto.get("cipherparams"));
        String iv = cipherparams.get("iv").toString();
        String kdf = crypto.get("kdf").toString();
        Map kdfparams = (Map) crypto.get("kdfparams");

        // 5. Revise derived key
        byte[] dk = null;
        byte[] vk = null;
        if ("scrypt".equalsIgnoreCase(kdf)) {
            String salt = (String) kdfparams.get("salt");
            int p = kdfparams.containsKey("p") ? ((Number) kdfparams.get("p")).intValue() : 1;
            int dkLen = kdfparams.containsKey("dklen") ? ((Number) kdfparams.get("dklen")).intValue() : 32;
            int r = kdfparams.containsKey("r") ? ((Number) kdfparams.get("r")).intValue() : 8;
            int n = kdfparams.containsKey(mN.toString()) ? ((Number) kdfparams.get(mN.toString())).intValue() : mN;
            byte[] derivedkey = SCrypt.generate(password.getBytes(), Hex.decode(salt), n, r, p, dkLen);
            dk = Arrays.copyOf(derivedkey, 16);
            vk = Arrays.copyOfRange(derivedkey, 16, 32);
        } else if ("pbkdf2".equalsIgnoreCase(kdf)) {
            if ("hmac-sha256".equalsIgnoreCase(kdfparams.get("prf").toString())) {
                throw new GeneralSecurityException(new NoSuchAlgorithmException("Invalid Algorithm"));
            }

            int dkLen = kdfparams.containsKey("dklen") ? ((Number) kdfparams.get("dklen")).intValue() : 256;
            int c = kdfparams.containsKey("c") ? ((Number) kdfparams.get("c")).intValue() : mN;
            SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            String salt = (String) kdfparams.get("salt");
            KeySpec ks = new PBEKeySpec(password.toCharArray(), salt.getBytes(), c, dkLen);
            try {
                SecretKey s = f.generateSecret(ks);
                byte[] derivedkey = s.getEncoded();
                dk = Arrays.copyOf(derivedkey, 16);
                vk = Arrays.copyOfRange(derivedkey, 16, 32);

            } catch (InvalidKeySpecException e) {
                throw new GeneralSecurityException(e);
            }

        } else {
            throw new GeneralSecurityException("Invalid Algorithm");
        }

        // 6. Verify MAC
        String mac = Hex.toHexString(HashUtil.sha3(Arrays.concatenate(vk, Hex.decode(ciphertext))));
        if (mac == null || !mac.equals(crypto.get("mac"))) {
            throw new GeneralSecurityException("Bad Password");
        }

        // 7. Prepare Cipher
        Cipher ci;
        try {
            ci = Cipher.getInstance("AES/CTR/NoPadding");
            SecretKey aesKey = new SecretKeySpec(dk, "AES");
            IvParameterSpec iv_spec = new IvParameterSpec(Hex.decode(iv));
            ci.init(Cipher.DECRYPT_MODE, aesKey, iv_spec);

            // 8. Decrypt
            byte[] pk = ci.doFinal(Hex.decode(ciphertext));
            return fromPrivateKey(pk);
        } catch (NoSuchPaddingException e) {
            throw new GeneralSecurityException(e);
        } catch (InvalidAlgorithmParameterException e) {
            throw new GeneralSecurityException(e);
        }
    }

    /**
     * Get Private Key
     *
     * @return Private Key
     */
    public byte[] getPrivateKey() {
        return this._eckey.getPrivKeyBytes();
    }

    /**
     * Get HEX Format Private Key
     *
     * @return Private Key with HEX Format
     */
    public String getPrivateKeyString() {
        byte[] privateKey = this._eckey.getPrivKeyBytes();
        if (privateKey == null) {
            return null;
        } else {
            return Hex.toHexString(privateKey);
        }
    }

    /**
     * Get Private Key
     *
     * @return Public Key
     */
    public byte[] getPublicKey() {
        return this._eckey.getPubKey();
    }

    /**
     * Get HEX Format PublicKey Key
     *
     * @return PublicKey Key with HEX Format
     */
    public String getPublicKeyString() {
        return Hex.toHexString(this._eckey.getPubKey());
    }

    /**
     * Get Private Key
     *
     * @return PublicKey Key
     */
    public byte[] getAddress() {
        return this._eckey.getAddress();
    }

    /**
     * Get HEX Format Address
     *
     * @return Address with HEX Format
     */
    public String getAddressString() {
        return Hex.toHexString(this._eckey.getAddress());
    }

    @Override
    public ECDSASignature sign(byte[] messageHash) throws SignatureException {
        if (this._eckey.isPubKeyOnly()) {
            throw new ECKey.MissingPrivateKeyException();
        }
        return this._eckey.sign(messageHash);
    }

    @Override
    public boolean verify(byte[] sigHash, ECDSASignature signature) throws SignatureException {
        return this._eckey.verify(sigHash, signature);
    }

    @Override
    public String toV3(String password) {
        return toV3(password, mN, 6, 8);
    }

    @Override
    public String toV3(String password, int n, int p, int r) {

        // derived key
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[64];
        random.nextBytes(salt);
        int dkLen = 32;
        byte[] derivedkey = SCrypt.generate(password.getBytes(), salt, n, r, p, dkLen);
        byte[] dk = Arrays.copyOf(derivedkey, 16);
        byte[] vk = Arrays.copyOfRange(derivedkey, 16, 32);

        // Encrypt
        Cipher cipher;
        try {
            cipher = Cipher.getInstance("AES/CTR/NoPadding");
            SecretKey aesKey = new SecretKeySpec(dk, "AES");
            byte[] iv = new byte[16];
            random.nextBytes(iv);
            IvParameterSpec iv_spec = new IvParameterSpec(iv);
            cipher.init(Cipher.ENCRYPT_MODE, aesKey, iv_spec);
            byte[] ciphertext = cipher.doFinal(getPrivateKey());

            // Calc MAC
            byte[] mac = HashUtil.sha3(Arrays.concatenate(vk, ciphertext));

            // Output
            StringBuilder sb = new StringBuilder();
            sb.append("{\"address\":\"").append(getAddressString()).append('"');
            sb.append(",\"crypto\":{\"cipher\":\"aes-128-ctr\"");
            sb.append(",\"ciphertext\":\"").append(Hex.toHexString(ciphertext)).append('"');
            sb.append(",\"cipherparams\":{");
            sb.append("\"iv\":\"").append(Hex.toHexString(iv)).append('"');
            sb.append("}");
            sb.append(",\"kdf\":\"").append("scrypt").append('"');
            sb.append(",\"kdfparams\":{");
            sb.append("\"dklen\":").append(dkLen);
            sb.append(",\"n\":").append(n);
            sb.append(",\"r\":").append(r);
            sb.append(",\"p\":").append(p);
            sb.append(",\"salt\":\"").append(Hex.toHexString(salt)).append('"');
            sb.append('}');
            sb.append(",\"mac\":\"").append(Hex.toHexString(mac)).append('"');
            sb.append('}');
            sb.append(",\"id\":\"").append(UUID.randomUUID()).append('"');
            sb.append(",\"version\":3}");
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeCryptoException();
        }

    }

}
