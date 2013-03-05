/**
 * Copyright (C) 2009  HungryHobo@mail.i2p
 * 
 * The GPG fingerprint for HungryHobo@mail.i2p is:
 * 6DD3 EAA2 9990 29BC 4AD2 7486 1E2C 7B61 76DC DC12
 * 
 * This file is part of I2P-Bote.
 * I2P-Bote is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * I2P-Bote is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with I2P-Bote.  If not, see <http://www.gnu.org/licenses/>.
 */

package i2p.bote.email;

import i2p.bote.Util;
import i2p.bote.fileencryption.SCryptParameters;
import i2p.bote.packet.dht.Contact;

import java.nio.ByteBuffer;
import java.security.GeneralSecurityException;

import net.i2p.I2PAppContext;
import net.i2p.data.Hash;
import net.i2p.util.RandomSource;

import com.lambdaworks.crypto.SCrypt;

/**
 * A cryptographic fingerprint of an {@link EmailIdentity}.
 */
public class Fingerprint {
    public final static int NUM_SALT_BYTES = 4;
    private final static SCryptParameters SCRYPT_PARAMETERS = new SCryptParameters(1<<14, 8, 1);
    private final static int NUM_FINGERPRINT_BYTES = 32;   // length of the raw fingerprint
    private final static int NUM_WORDS_IN_FINGERPRINT = 7;   // #words generated from the raw fingerprint
    private final static int NUM_WORDS_IN_LIST = 1 << 13;   // must be a power of 2
    
    private Hash nameHash;
    private EmailDestination destination;
    private byte[] salt;
    private byte[] rawFingerprint;

    public Fingerprint(Hash nameHash, EmailDestination destination, byte[] salt) throws GeneralSecurityException {
        this.nameHash = nameHash;
        this.destination = destination;
        this.salt = salt;
    }
    
    /** Finds a salt value such that scrypt(nameHash|destination, salt) starts with a zero byte */
    public static Fingerprint generate(Contact contact) throws GeneralSecurityException {
        EmailDestination destination = contact.getDestination();
        Hash nameHash = EmailIdentity.calculateHash(contact.getName());
        return generate(nameHash, destination);
    }
    
    /** Finds a salt value such that scrypt(nameHash|destination, salt) starts with a zero byte */
    public static Fingerprint generate(EmailIdentity identity) throws GeneralSecurityException {
        Hash nameHash = EmailIdentity.calculateHash(identity.getPublicName());
        return generate(nameHash, identity);
    }
    
    private static Fingerprint generate(Hash nameHash, EmailDestination destination) throws GeneralSecurityException {
        byte[] input = Util.concat(nameHash.toByteArray(), destination.toByteArray());
        RandomSource randomSource = I2PAppContext.getGlobalContext().random();
        while (true) {
            byte[] salt = new byte[NUM_SALT_BYTES];
            randomSource.nextBytes(salt);
            byte[] fingerprint = SCrypt.scrypt(input, salt, SCRYPT_PARAMETERS.N, SCRYPT_PARAMETERS.r, SCRYPT_PARAMETERS.p, NUM_FINGERPRINT_BYTES);
            if (fingerprint[31] == 0)
                return new Fingerprint(nameHash, destination, salt);
        }
    }
    
    public byte[] getSalt() {
        return salt;
    }
    
    /**
     * Returns a string representation of this <code>Fingerprint</code> based on a word list.<br/>
     * It doesn't contain the full <code>NUM_FINGERPRINT_BYTES</code> bytes
     * worth of information from the raw fingerprint, but it is more recognizable to users.
     * @param wordList a word list for some locale
     * @throws GeneralSecurityException
     */
    public String getWords(String[] wordList) throws GeneralSecurityException {
        if (rawFingerprint == null)
            rawFingerprint = getRawFingerprint();
        
        ByteBuffer fingerprintBuffer = ByteBuffer.wrap(rawFingerprint);
        StringBuilder fingerprintWords = new StringBuilder();
        for (int i=0; i<NUM_WORDS_IN_FINGERPRINT; i++) {
            if (i > 0)
                fingerprintWords.append(" - ");
            int index = fingerprintBuffer.getShort() & (NUM_WORDS_IN_LIST - 1);
            String word = wordList[index];
            fingerprintWords.append(word);
        }
        
        return fingerprintWords.toString();
    }
    
    /**
     * Returns a byte array of length <code>NUM_FINGERPRINT_BYTES</code>.
     * @throws GeneralSecurityException
     */
    private byte[] getRawFingerprint() throws GeneralSecurityException {
        byte[] input = Util.concat(nameHash.toByteArray(), destination.toByteArray());
        byte[] fingerprint = SCrypt.scrypt(input, salt, SCRYPT_PARAMETERS.N, SCRYPT_PARAMETERS.r, SCRYPT_PARAMETERS.p, NUM_FINGERPRINT_BYTES);
        return fingerprint;
    }
    
    /** Checks that the 31th byte of the fingerprint is zero. */
    public boolean isValid() throws GeneralSecurityException {
        return getRawFingerprint()[NUM_FINGERPRINT_BYTES-1] == 0;
    }
}