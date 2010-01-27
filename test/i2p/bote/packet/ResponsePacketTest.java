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

package i2p.bote.packet;

import static junit.framework.Assert.assertTrue;
import i2p.bote.UniqueId;
import i2p.bote.email.EmailDestination;

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import net.i2p.I2PAppContext;
import net.i2p.data.DataFormatException;

import org.junit.Before;
import org.junit.Test;

public class ResponsePacketTest {
    private ResponsePacket responsePacket;
    private I2PAppContext appContext = new I2PAppContext();

    @Before
    public void setUp() throws Exception {
        byte[] packetIdBytes = new byte[] {120, 120, -8, -88, 21, 126, 46, -61, 18, -101, 15, 53, 20, -44, -112, 42, 86, -117, 30, -96, -66, 33, 71, -55, -102, -78, 78, -82, -105, 66, -116, 43};
        UniqueId packetId = new UniqueId(packetIdBytes, 0);
        
        responsePacket = new ResponsePacket(createDataPacket(), StatusCode.OK, packetId);
    }

    private DataPacket createDataPacket() throws NoSuchAlgorithmException, DataFormatException {
        byte[] dataToStore = new byte[] {2, 109, -80, -37, -106, 83, -33, -39, -94, -76, -112, -98, 99, 25, -61, 44, -92, -85, 1, 10, -128, -2, -27, -86, -126, -33, -11, 42, 56, 3, -97, -101, 111, 7, -96, 25, 121, 74, 89, -40, -33, 82, -50, -18, 49, 106, 13, -121, 53, -83, -2, 35, -7, 71, -71, 26, 90, 1};
        
        UniqueId deletionKeyVerify = new UniqueId(new byte[] {-62, -112, 99, -65, 13, 44, -117, -111, 96, 45, -6, 64, 78, 57, 117, 103, -24, 101, 106, -116, -18, 62, 99, -49, 60, -81, 8, 64, 27, -41, -104, 58}, 0);
        byte[] messageIdBytes = new byte[] {-69, -24, -109, 1, 69, -122, -69, 113, -68, -90, 55, -28, 105, 97, 125, 70, 51, 58, 14, 2, -13, -53, 90, -29, 36, 67, 36, -94, -108, -125, 11, 123};
        UniqueId messageId = new UniqueId(messageIdBytes, 0);
        
        UnencryptedEmailPacket unencryptedPacket = new UnencryptedEmailPacket(messageId, 0, 1, dataToStore, deletionKeyVerify);
        EmailDestination destination = new EmailDestination("0XuJjhgp58aOhvHHgpaxoQYsCUfDS6BECMEoVxFGEFPdk3y8lbzIsq9eUyeizFleMacYwoscCir8nQLlW34lxfRmirkNpD9vU1XnmjnZ5hGdnor1qIDqz3KJ040dVQ617MwyG97xxYLT0FsH907vBXgdc4RCHwKd1~9siagA5CSMaA~wM8ymKXLypiZGYexENLmim7nMzJTQYoOM~fVS99UaGJleDBN3pgZ2EvRYDQV2VqKH7Gee07R3y7b~c0tAKVHS0IbPQfTVJigrIHjTl~ZczxpaeTM04T8IgxKnO~lSmR1w7Ik8TpEkETwT9PDwUqQsjmlSY8E~WwwGMRJVyIRZUkHeRZ0aFq7us8W9EKzYtjjiU1z0QFpZrTfJE8oqCbnH5Lqv5Q86UdTPpriJC1N99E77TpCTnNzcBnpp6ko2JCy2IJUveaigKxS6EmS9KarkkkBRsckOKZZ6UNTOqPZsBCsx0Q9WvDF-Uc3dtouXWyenxRptaQsdkZyYlEQv");
        return new EncryptedEmailPacket(unencryptedPacket, destination, appContext);
    }
    
    @Test
    public void toByteArrayAndBack() throws Exception {
        byte[] arrayA = responsePacket.toByteArray();
        byte[] arrayB = new ResponsePacket(arrayA).toByteArray();
        assertTrue("The two arrays differ!", Arrays.equals(arrayA, arrayB));
    }
}