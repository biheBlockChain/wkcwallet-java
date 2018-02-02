package org.ethereum.core;


import org.ethereum.crypto.ECKey;
import org.ethereum.crypto.HashUtil;
import org.ethereum.util.ByteUtil;
import org.ethereum.util.RLP;
import org.ethereum.util.RLPElement;
import org.ethereum.util.RLPList;
import org.ethereum.wallet.Wallet;

import java.math.BigInteger;
import java.security.SignatureException;
import java.util.Arrays;
import org.spongycastle.util.BigIntegers;
import org.spongycastle.util.encoders.Hex;

public class Transaction {
    private static final BigInteger DEFAULT_GAS_PRICE = new BigInteger("20000000000000");
    private static final BigInteger DEFAULT_BALANCE_GAS = new BigInteger("21000");
    private byte[] hash;
    protected byte[] nonce;
    protected byte[] value;
    protected byte[] receiveAddress;
    protected byte[] gasPrice;
    protected byte[] gasLimit;
    protected byte[] data;
    private ECKey.ECDSASignature signature;
    protected byte[] sendAddress;
    protected byte[] rlpEncoded;
    private byte[] rlpRaw;
    protected boolean parsed;

    public Transaction(byte[] rawData) {
        this.parsed = false;
        this.rlpEncoded = rawData;
        this.parsed = false;
    }

    public Transaction(byte[] nonce, byte[] gasPrice, byte[] gasLimit, byte[] receiveAddress, byte[] value, byte[] data) {
        this.parsed = false;
        this.nonce = nonce;
        this.gasPrice = gasPrice;
        this.gasLimit = gasLimit;
        this.receiveAddress = receiveAddress;
        this.value = value;
        this.data = data;
        if(receiveAddress == null) {
            this.receiveAddress = ByteUtil.EMPTY_BYTE_ARRAY;
        }

        this.parsed = true;
    }

    public Transaction(byte[] nonce, byte[] gasPrice, byte[] gasLimit, byte[] receiveAddress, byte[] value, byte[] data, byte[] r, byte[] s, byte v) {
        this(nonce, gasPrice, gasLimit, receiveAddress, value, data);
        this.signature = ECKey.ECDSASignature.fromComponents(r, s, v);
    }

    public void rlpParse() {
        RLPList decodedTxList = RLP.decode2(this.rlpEncoded);
        RLPList transaction = (RLPList)decodedTxList.get(0);
        this.nonce = ((RLPElement)transaction.get(0)).getRLPData();
        this.gasPrice = ((RLPElement)transaction.get(1)).getRLPData();
        this.gasLimit = ((RLPElement)transaction.get(2)).getRLPData();
        this.receiveAddress = ((RLPElement)transaction.get(3)).getRLPData();
        this.value = ((RLPElement)transaction.get(4)).getRLPData();
        this.data = ((RLPElement)transaction.get(5)).getRLPData();
        if(((RLPElement)transaction.get(6)).getRLPData() != null) {
            byte v = ((RLPElement)transaction.get(6)).getRLPData()[0];
            byte[] r = ((RLPElement)transaction.get(7)).getRLPData();
            byte[] s = ((RLPElement)transaction.get(8)).getRLPData();
            this.signature = ECKey.ECDSASignature.fromComponents(r, s, v);
        } else {
        }

        this.parsed = true;
        this.hash = this.getHash();
    }

    public boolean isParsed() {
        return this.parsed;
    }

    public byte[] getHash() {
        if(!this.parsed) {
            this.rlpParse();
        }

        byte[] plainMsg = this.getEncoded();
        return HashUtil.sha3(plainMsg);
    }

    public byte[] getRawHash() {
        if(!this.parsed) {
            this.rlpParse();
        }

        byte[] plainMsg = this.getEncodedRaw();
        return HashUtil.sha3(plainMsg);
    }

    public byte[] getNonce() {
        if(!this.parsed) {
            this.rlpParse();
        }

        return this.nonce == null?ByteUtil.ZERO_BYTE_ARRAY:this.nonce;
    }

    public boolean isValueTx() {
        if(!this.parsed) {
            this.rlpParse();
        }

        return this.value != null;
    }

    public byte[] getValue() {
        if(!this.parsed) {
            this.rlpParse();
        }

        return this.value == null?ByteUtil.ZERO_BYTE_ARRAY:this.value;
    }

    public byte[] getReceiveAddress() {
        if(!this.parsed) {
            this.rlpParse();
        }

        return this.receiveAddress;
    }

    public byte[] getGasPrice() {
        if(!this.parsed) {
            this.rlpParse();
        }

        return this.gasPrice == null?ByteUtil.ZERO_BYTE_ARRAY:this.gasPrice;
    }

    public byte[] getGasLimit() {
        if(!this.parsed) {
            this.rlpParse();
        }

        return this.gasLimit;
    }

    public long nonZeroDataBytes() {
        if(this.data == null) {
            return 0L;
        } else {
            int counter = 0;
            byte[] var5 = this.data;
            int var4 = this.data.length;

            for(int var3 = 0; var3 < var4; ++var3) {
                byte aData = var5[var3];
                if(aData != 0) {
                    ++counter;
                }
            }

            return (long)counter;
        }
    }

    public long zeroDataBytes() {
        if(this.data == null) {
            return 0L;
        } else {
            int counter = 0;
            byte[] var5 = this.data;
            int var4 = this.data.length;

            for(int var3 = 0; var3 < var4; ++var3) {
                byte aData = var5[var3];
                if(aData == 0) {
                    ++counter;
                }
            }

            return (long)counter;
        }
    }

    public byte[] getData() {
        if(!this.parsed) {
            this.rlpParse();
        }

        return this.data;
    }

    public ECKey.ECDSASignature getSignature() {
        if(!this.parsed) {
            this.rlpParse();
        }

        return this.signature;
    }

    public byte[] getContractAddress() {
        return !this.isContractCreation()?null:HashUtil.calcNewAddr(this.getSenderAddress(), this.getNonce());
    }

    public boolean isContractCreation() {
        if(!this.parsed) {
            this.rlpParse();
        }

        return this.receiveAddress == null || Arrays.equals(this.receiveAddress, ByteUtil.EMPTY_BYTE_ARRAY);
    }

    public ECKey getKey() {
        byte[] hash = this.getRawHash();
        return ECKey.recoverFromSignature(this.signature.v, this.signature, hash);
    }

    public synchronized byte[] getSenderAddress() {
        try {
            if(this.sendAddress == null) {
                this.sendAddress = ECKey.signatureToAddress(this.getRawHash(), this.getSignature());
            }

            return this.sendAddress;
        } catch (SignatureException var2) {
            return null;
        }
    }

    public void sign(Wallet wallet) throws ECKey.MissingPrivateKeyException, SignatureException {
        this.signature = wallet.sign(this.getRawHash());
        this.rlpEncoded = null;
    }

    public String toString() {
        return this.toString(2147483647);
    }

    public String toString(int maxDataSize) {
        if(!this.parsed) {
            this.rlpParse();
        }

        String dataS;
        if(this.data == null) {
            dataS = "";
        } else if(this.data.length < maxDataSize) {
            dataS = ByteUtil.toHexString(this.data);
        } else {
            dataS = ByteUtil.toHexString(Arrays.copyOfRange(this.data, 0, maxDataSize)) + "... (" + this.data.length + " bytes)";
        }

        return "TransactionData [hash=" + ByteUtil.toHexString(this.hash) + "  nonce=" + ByteUtil.toHexString(this.nonce) + ", gasPrice=" + ByteUtil.toHexString(this.gasPrice) + ", gas=" + ByteUtil.toHexString(this.gasLimit) + ", receiveAddress=" + ByteUtil.toHexString(this.receiveAddress) + ", value=" + ByteUtil.toHexString(this.value) + ", data=" + dataS + ", signatureV=" + (this.signature == null?"":Byte.valueOf(this.signature.v)) + ", signatureR=" + (this.signature == null?"":ByteUtil.toHexString(BigIntegers.asUnsignedByteArray(this.signature.r))) + ", signatureS=" + (this.signature == null?"":ByteUtil.toHexString(BigIntegers.asUnsignedByteArray(this.signature.s))) + "]";
    }

    public byte[] getEncodedRaw() {
        if(!this.parsed) {
            this.rlpParse();
        }

        if(this.rlpRaw != null) {
            return this.rlpRaw;
        } else {
            byte[] nonce = null;
            if(this.nonce != null && (this.nonce.length != 1 || this.nonce[0] != 0)) {
                nonce = RLP.encodeElement(this.nonce);
            } else {
                nonce = RLP.encodeElement((byte[])null);
            }

            byte[] gasPrice = RLP.encodeElement(this.gasPrice);
            byte[] gasLimit = RLP.encodeElement(this.gasLimit);
            byte[] receiveAddress = RLP.encodeElement(this.receiveAddress);
            byte[] value = RLP.encodeElement(this.value);
            byte[] data = RLP.encodeElement(this.data);
            this.rlpRaw = RLP.encodeList(new byte[][]{nonce, gasPrice, gasLimit, receiveAddress, value, data});
            return this.rlpRaw;
        }
    }

    public byte[] getEncoded() {
        if(this.rlpEncoded != null) {
            return this.rlpEncoded;
        } else {
            byte[] nonce = null;
            if(this.nonce != null && (this.nonce.length != 1 || this.nonce[0] != 0)) {
                nonce = RLP.encodeElement(this.nonce);
            } else {
                nonce = RLP.encodeElement((byte[])null);
            }

            byte[] gasPrice = RLP.encodeElement(this.gasPrice);
            byte[] gasLimit = RLP.encodeElement(this.gasLimit);
            byte[] receiveAddress = RLP.encodeElement(this.receiveAddress);
            byte[] value = RLP.encodeElement(this.value);
            byte[] data = RLP.encodeElement(this.data);
            byte[] v;
            byte[] r;
            byte[] s;
            if(this.signature != null) {
                v = RLP.encodeByte(this.signature.v);
                r = RLP.encodeElement(BigIntegers.asUnsignedByteArray(this.signature.r));
                s = RLP.encodeElement(BigIntegers.asUnsignedByteArray(this.signature.s));
            } else {
                v = RLP.encodeElement(ByteUtil.EMPTY_BYTE_ARRAY);
                r = RLP.encodeElement(ByteUtil.EMPTY_BYTE_ARRAY);
                s = RLP.encodeElement(ByteUtil.EMPTY_BYTE_ARRAY);
            }

            this.rlpEncoded = RLP.encodeList(new byte[][]{nonce, gasPrice, gasLimit, receiveAddress, value, data, v, r, s});
            this.hash = this.getHash();
            return this.rlpEncoded;
        }
    }

    public int hashCode() {
        byte[] hash = this.getHash();
        int hashCode = 0;

        for(int i = 0; i < hash.length; ++i) {
            hashCode += hash[i] * i;
        }

        return hashCode;
    }

    public boolean equals(Object obj) {
        if(!(obj instanceof Transaction)) {
            return false;
        } else {
            Transaction tx = (Transaction)obj;
            return tx.hashCode() == this.hashCode();
        }
    }

    public static Transaction create(String to, BigInteger amount, BigInteger nonce) {
        return create(to, amount, nonce, DEFAULT_GAS_PRICE, DEFAULT_BALANCE_GAS, (byte[])null);
    }

    public static Transaction create(String to, BigInteger amount, BigInteger nonce, byte[] data) {
        return create(to, amount, nonce, DEFAULT_GAS_PRICE, DEFAULT_BALANCE_GAS, data);
    }

    public static Transaction create(String to, BigInteger amount, BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit) {
        return create(to, amount, nonce, gasPrice, gasLimit, (byte[])null);
    }

    public static Transaction create(String to, BigInteger amount, BigInteger nonce, BigInteger gasPrice, BigInteger gasLimit, byte[] data) {
        return new Transaction(BigIntegers.asUnsignedByteArray(nonce), BigIntegers.asUnsignedByteArray(gasPrice), BigIntegers.asUnsignedByteArray(gasLimit), Hex.decode(to), BigIntegers.asUnsignedByteArray(amount), data);
    }

    public byte[] getSenderPublicKey() throws SignatureException {
        byte[] messageHash = this.getRawHash();
//        Preconditions.checkArgument(messageHash.length == 32, "messageHash argument has length %d", new Object[]{Integer.valueOf(messageHash.length)});
        ECKey.ECDSASignature sig = this.getSignature();
        if(sig == null) {
            throw new SignatureException();
        } else {
            int header = sig.v;
            if(header >= 27 && header <= 34) {
                if(header >= 31) {
                    header -= 4;
                }

                int recId = header - 27;
                byte[] key = ECKey.recoverPubBytesFromSignature(recId, sig, messageHash);
                if(key == null) {
                    throw new SignatureException("Could not recover public key from signature");
                } else {
                    return key;
                }
            } else {
                throw new SignatureException("Header byte out of range: " + header);
            }
        }
    }

    public boolean verifySignature() throws SignatureException {
        return ECKey.verify(this.getRawHash(), this.getSignature(), this.getSenderPublicKey());
    }
}