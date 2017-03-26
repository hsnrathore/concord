package com.walmartlabs.concord.server.cfg;

import javax.inject.Named;
import javax.inject.Singleton;
import java.io.Serializable;
import java.nio.charset.Charset;

@Named
@Singleton
public class SecretStoreConfiguration implements Serializable {

    public static final String SECRET_STORE_SALT_KEY = "SECRET_STORE_SALT";

    // obligatory https://xkcd.com/221/
    private static final byte[] DEFAULT_SALT = {0x48, 0x29, 0x38, 0x2a, 0x60, 0x65, 0x6b, 0x33, 0x22};

    public static final Charset DEFAULT_PASSWORD_CHARSET = Charset.forName("US-ASCII");

    private final byte[] salt;

    public SecretStoreConfiguration() {
        byte[] salt = DEFAULT_SALT;
        String s = System.getenv(SECRET_STORE_SALT_KEY);
        if (s != null) {
            salt = s.getBytes(SecretStoreConfiguration.DEFAULT_PASSWORD_CHARSET);
        }
        this.salt = salt;
    }

    public SecretStoreConfiguration(byte[] salt) {
        this.salt = salt;
    }

    public byte[] getSalt() {
        return salt;
    }
}
