package com.nideas.api.userservice.service.auth;

import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import org.springframework.stereotype.Service;

/** Created by Nanugonda on 8/14/2018. */
@Service
public class CryptoService {

  private PrivateKey privateKey;
  private PublicKey publicKey;
  private static final String ALG = "RSA";

  public CryptoService() throws NoSuchAlgorithmException {
    KeyPairGenerator generator = KeyPairGenerator.getInstance(ALG);
    generator.initialize(2048);
    KeyPair keyPair = generator.generateKeyPair();
    this.privateKey = keyPair.getPrivate();
    this.publicKey = keyPair.getPublic();
  }

  public String encrypt(String text)
      throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException,
          BadPaddingException, IllegalBlockSizeException {
    Cipher cipher = Cipher.getInstance(ALG);
    cipher.init(Cipher.ENCRYPT_MODE, privateKey);
    return new String(cipher.doFinal(text.getBytes()));
  }

  public String decrypt(String text)
      throws NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException,
          IllegalBlockSizeException, InvalidKeyException {
    Cipher cipher = Cipher.getInstance(ALG);
    cipher.init(Cipher.DECRYPT_MODE, publicKey);
    return new String(cipher.doFinal(text.getBytes()));
  }
}
