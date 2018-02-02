package com.wrbug.wkcwallet.entry;


import java.io.File;

/**
 * KeystoreInfoBean
 *
 * @author WrBug
 * @since 2018/1/31
 */
public class KeystoreInfoBean {
    /**
     * address : e6637d20b74b7cce17d8eacf8516e85ee646fe74
     * id : 54f22d53-eb1f-4166-8628-db468c3e8a2a
     * version : 3
     */

    private String address;
    private CryptoBean crypto;
    private String id;
    private double version;
    private File keystoreFile;

    public File getKeystoreFile() {
        return keystoreFile;
    }

    public void setKeystoreFile(File keystoreFile) {
        this.keystoreFile = keystoreFile;
    }

    public String getAddress() {
        return address;
    }
    public String getRealAddress(){
        return "0x"+address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

    public CryptoBean getCrypto() {
        return crypto;
    }

    public void setCrypto(CryptoBean crypto) {
        this.crypto = crypto;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getVersion() {
        return version;
    }

    public void setVersion(double version) {
        this.version = version;
    }

    public static class CryptoBean{
        /**
         * cipher : aes-128-ctr
         * ciphertext : 5684124b8abad0a9f73f7af06bd55aa2fe5e7befa617136971d8b40c1530b7b1
         * cipherparams : {"iv":"c3f86f43e1aee19f11207a7a75254136"}
         * kdf : scrypt
         * kdfparams : {"dklen":32,"n":4096,"p":6,"r":8,"salt":"a97fb71ffea8dd76d15e6e4b7072dc0e06040d52512c72189866fed9f5ebc53b"}
         * mac : e971e673f95808f286907c930135e4fa9995bc9f168e95a2cc52e11cfedc9a2d
         */

        private String cipher;
        private String ciphertext;
        private CipherparamsBean cipherparams;
        private String kdf;
        private KdfparamsBean kdfparams;
        private String mac;

        public String getCipher() {
            return cipher;
        }

        public void setCipher(String cipher) {
            this.cipher = cipher;
        }

        public String getCiphertext() {
            return ciphertext;
        }

        public void setCiphertext(String ciphertext) {
            this.ciphertext = ciphertext;
        }

        public CipherparamsBean getCipherparams() {
            return cipherparams;
        }

        public void setCipherparams(CipherparamsBean cipherparams) {
            this.cipherparams = cipherparams;
        }

        public String getKdf() {
            return kdf;
        }

        public void setKdf(String kdf) {
            this.kdf = kdf;
        }

        public KdfparamsBean getKdfparams() {
            return kdfparams;
        }

        public void setKdfparams(KdfparamsBean kdfparams) {
            this.kdfparams = kdfparams;
        }

        public String getMac() {
            return mac;
        }

        public void setMac(String mac) {
            this.mac = mac;
        }

        public static class CipherparamsBean {
            /**
             * iv : c3f86f43e1aee19f11207a7a75254136
             */

            private String iv;

            public String getIv() {
                return iv;
            }

            public void setIv(String iv) {
                this.iv = iv;
            }
        }

        public static class KdfparamsBean {
            /**
             * dklen : 32
             * n : 4096
             * p : 6
             * r : 8
             * salt : a97fb71ffea8dd76d15e6e4b7072dc0e06040d52512c72189866fed9f5ebc53b
             */

            private int dklen;
            private int n;
            private int p;
            private int r;
            private String salt;

            public int getDklen() {
                return dklen;
            }

            public void setDklen(int dklen) {
                this.dklen = dklen;
            }

            public int getN() {
                return n;
            }

            public void setN(int n) {
                this.n = n;
            }

            public int getP() {
                return p;
            }

            public void setP(int p) {
                this.p = p;
            }

            public int getR() {
                return r;
            }

            public void setR(int r) {
                this.r = r;
            }

            public String getSalt() {
                return salt;
            }

            public void setSalt(String salt) {
                this.salt = salt;
            }
        }
    }



}
