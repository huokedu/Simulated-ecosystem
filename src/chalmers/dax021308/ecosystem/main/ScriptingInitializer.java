package chalmers.dax021308.ecosystem.main;

import javax.swing.UIManager;

import chalmers.dax021308.ecosystem.controller.scripting.ScriptHandler;


/**
 * Special Main method for using the script controller.
 * <p> 
 * Don't forget to copy the run configuration (VM Arguments) from the normal one to ScriptInitializers.
 * 
 * @author Erik Ramqvist
 *
 */
public class ScriptingInitializer {
	
	public static void main(String[] args) {
		try {
	    	UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");
		} catch(Exception e) {
			e.printStackTrace();
		}
		new ScriptHandler();
	}
}
