package org.ethereum.wallet;

import org.ethereum.crypto.ECKey.ECDSASignature;

import javax.crypto.NoSuchPaddingException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
 
/**
 * 钱包接口
 * 
 * @author tongsh
 *
 */
public interface Wallet {

    /**
     * Get Private Key
     * 
     * @return Private Key
     */
    public byte[] getPrivateKey();

    /**
     * Get HEX Format Private Key
     * 
     * @return Private Key with HEX Format
     */
    public String getPrivateKeyString();

    /**
     * Get Private Key
     * 
     * @return Public Key
     */
    public byte[] getPublicKey();

    /**
     * Get HEX Format PublicKey Key
     * 
     * @return PublicKey Key with HEX Format
     */
    public String getPublicKeyString();

    /**
     * Get Private Key
     * 
     * @return PublicKey Key
     */
    public byte[] getAddress();

    /**
     * Get HEX Format Address
     * 
     * @return Address with HEX Format
     */
    public String getAddressString();

    /**
     * Make Signature
     * 
     * @param messageHash
     *            the message hash to be signed
     * @return Signature
     * @throws SignatureException 
     */
    public ECDSASignature sign(byte[] messageHash) throws SignatureException;

    /**
     * Verify Signature
     * @param messageHash
     * @param signature
     * @return
     * @throws SignatureException 
     */
    public boolean verify(byte[] messageHash, ECDSASignature signature) throws SignatureException;

    /**
     * Convert to V3 Keyfile format
     * @param password	protected password
     * @return
     * @throws NoSuchAlgorithmException 
     */
    public String toV3(String password) ;

    public String toV3(String password, int n, int p, int r) throws NoSuchAlgorithmException, NoSuchPaddingException;
}
