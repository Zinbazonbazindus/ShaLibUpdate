package fr.shawiizz.updater;

import java.io.File;
import java.io.IOException;

public class ShaMain {
	/* TOUT CE QUI EST ACCESSIBLE AUX UTILISATEURS*/
	
	public static int globalprogress=0;
	public static int fileprogress=0;
	
	public static File pathOTHER;
	public static String currentDownloadLink;
	public static String downloadSpeed;
	public static String remoteUrl;
	public static Boolean downloadFinish = false;
    public static Boolean downloadStarted = false;
    public static Boolean updaterenabled;
	
	public static void ShaUpdaterMC(String foldername, String url) {
		MinecraftPath.gamefolder = foldername;
		remoteUrl = url;
		try {
			InstallMethods.ShaLibMC();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void ShaUpdaterOTHER(String path, String url) {
		File pathfile = new File(path);
		pathOTHER = pathfile;
		remoteUrl = url;
		try {
			InstallMethods.ShaLibOTHER();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
