package fr.tse.fise2.info9.services;

import java.awt.Desktop;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Cette classe nous aide à ouvrir un lien facilement que ca soit un URL ou URI.
 */

public class OpenLink {
	public static boolean openWebpage(URI uri) {
	    Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
	    if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
	        try {
	            desktop.browse(uri);
	            return true;
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	    return false;
	}

	public static boolean openWebpage(URL url) {
	    try {
	        return openWebpage(url.toURI());
	    } catch (URISyntaxException e) {
	        e.printStackTrace();
	    }
	    return false;
	}
}
