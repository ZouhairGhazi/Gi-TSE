package fr.tse.fise2.info9.services;

import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

/**
 * Cette classe nous aide à ouvrir les images sans se baser sur un chemin absolu, et pour garder les resources dans le projet
 * et pas seulement en local.
 */

public class OpenImage {
	public static Image openImage(String path) {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream input = classLoader.getResourceAsStream(path);
		try {
		    Image image = ImageIO.read(input);
		    return image;
		} catch (IOException e) {
		    e.printStackTrace();
		}
		return null;
	}
}
