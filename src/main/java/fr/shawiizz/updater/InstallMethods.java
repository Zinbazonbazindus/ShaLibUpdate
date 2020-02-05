package fr.shawiizz.updater;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.UIManager;

public class InstallMethods
{
    public static File mcDir = MinecraftPath.getMinecraftDefaultDir();
    public static File otherDir = ShaMain.pathOTHER;
    public static Path mcpath = Paths.get(mcDir.getAbsolutePath());
    public static Path otherpath = Paths.get(otherDir.getAbsolutePath());
   

    public static void ShaLibMC() throws IOException
    {
    	
    	readerLoad();
        if (GetInformations.instance.getStatus().toString().equals("true")) {
        	if (MinecraftPath.gamefolder.equals("")) {
        		System.out.println("Error, your don't specify the name of your game's folder !");
        		System.exit(0);
    		}
        	ShaMain.updaterenabled = true;
            Files.createDirectories(mcpath);
        	if (!mcDir.exists() || !mcDir.isDirectory()) {
    			
    			System.out.println("Error, your folder doesn't exists !");
    			return;
    		}
        	
        	/*INSTALL*/
    		FileChecker checker = new FileChecker(new File(mcDir, ""));
    		ProcessInstall install = new ProcessInstall(checker, mcDir);
    		install.startDownload();
		} else {
			ShaMain.updaterenabled = false;
			System.out.println("Download server is disabled, exiting...");
			System.exit(0);
		}
    	
    }

    

    @SuppressWarnings("unlikely-arg-type")
	public static void ShaLibOTHER() throws IOException
    {
    	readerLoad();
    	if (GetInformations.instance.getStatus().toString().equals("true")) {
    		otherDir = ShaMain.pathOTHER;
            
        	if (ShaMain.pathOTHER.equals("")) {
        		System.out.println("Error, you don't specify the path of your folder !");
        		System.exit(0);
    		}
        	ShaMain.updaterenabled = true;
            Files.createDirectories(otherpath);
            System.out.println(otherpath);
        	if (!otherDir.exists() || !otherDir.isDirectory()) {
    			
    			System.out.println("Error, your folder doesn't exists !");
    			return;
    		}
    		FileChecker checker = new FileChecker(new File(otherDir, ""));
    		ProcessInstall install = new ProcessInstall(checker, otherDir);
    		install.startDownload();
    	} else {
			ShaMain.updaterenabled = false;
			System.out.println("Download server is disabled, exiting...");
			System.exit(0);
		}
        
    }

    static void readerLoad() {
    	try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        GetInformations.instance = new GetInformations();
        if(!GetInformations.instance().init())
        {
            return;
        }
    }
   
    
}
