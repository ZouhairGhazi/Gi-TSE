//Taken and adapted from jackrutorial

package fr.tse.fise2.info9.services;

import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.SecretKeySpec;

/**
 * Permet de chiffrer les données de login (pseudo, mot de passe et ip du git)
 * Les données sont stockées dans le fichier data.enc
 * 
 *
 */
public class Encrypter {

	public static String key = "3s6v9y/B?E(H+MbQ"; // Générée aléatoirement

	/**
	 * Une méthode qui permet de chiffrer un fichier
	 * 
	 * @param secretKey     La clef de chiffrement utilisé pour chiffrer le fichier
	 *                      data.enc
	 * @param fileInputPath Path vers le fichier à chiffrer
	 * @param fileOutPath   Path du fichier en sortie de chiffrement
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeyException
	 * @throws IOException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	public static void encryptedFile(String secretKey, String fileInputPath, String fileOutPath)
			throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IOException,
			IllegalBlockSizeException, BadPaddingException {
		SecretKeySpec key = new SecretKeySpec(secretKey.getBytes(), "AES");
		Cipher cipher = Cipher.getInstance("AES"); // On choisie un chiffrement de type AES
		cipher.init(Cipher.ENCRYPT_MODE, key); // On se met en mode chiffrement

		// On ouvre un flux sur le fichier à chiffrer
		File fileInput = new File(fileInputPath);
		FileInputStream inputStream = new FileInputStream(fileInput);
		byte[] inputBytes = new byte[(int) fileInput.length()];
		inputStream.read(inputBytes);

		// On chiffre
		byte[] outputBytes = cipher.doFinal(inputBytes);

		// On écrit dans le fichier de sortie
		File fileEncryptOut = new File(fileOutPath);
		FileOutputStream outputStream = new FileOutputStream(fileEncryptOut);
		outputStream.write(outputBytes);

		// On ferme les flux
		inputStream.close();
		outputStream.close();
	}

	/**
	 * Une méthode qui permet de déchiffrer un fichier
	 * 
	 * @param secretKey     La clef de chiffrement utilisé pour déchiffrer le
	 *                      fichier data.enc
	 * @param fileInputPath Path vers le fichier à déchiffrer
	 * @param fileOutPath   Path du fichier en sortie de chiffrement
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeyException
	 * @throws IOException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	public static void decryptedFile(String secretKey, String fileInputPath, String fileOutPath)
			throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IOException,
			IllegalBlockSizeException, BadPaddingException {
		SecretKeySpec key = new SecretKeySpec(secretKey.getBytes(), "AES");
		Cipher cipher = Cipher.getInstance("AES"); // On choisie un chiffrement de type AES
		cipher.init(Cipher.DECRYPT_MODE, key); // On se met en mode déchiffrement

		// On ouvre un flux sur le fichier à déchiffrer
		File fileInput = new File(fileInputPath);
		FileInputStream inputStream = new FileInputStream(fileInput);
		byte[] inputBytes = new byte[(int) fileInput.length()];
		inputStream.read(inputBytes);

		// On déchiffre
		byte[] outputBytes = cipher.doFinal(inputBytes);

		// On écrit dans le fichier de sortie
		File fileEncryptOut = new File(fileOutPath);
		FileOutputStream outputStream = new FileOutputStream(fileEncryptOut);
		outputStream.write(outputBytes);

		// On ferme les flux
		inputStream.close();
		outputStream.close();

	}
}
