package fr.shawiizz.updater;

import java.awt.EventQueue;
import java.io.File;

public class ProcessInstall implements Runnable
{
	private final ProcessInstall installThread;
	private final FileChecker fileChecker;
    public final File mcDir;
    public final File modPackDir;
    private boolean error = false;

    public ProcessInstall(FileChecker file, File mcDir)
    {
    	this.installThread = this;
        this.fileChecker = file;
        this.mcDir = mcDir;
        this.modPackDir = new File(mcDir, "");
    }

    public void startDownload()
    {
    	new Thread(this.installThread).start();
    }

    @Override
    public void run()
    {
        if(!this.fileChecker.remoteList.isEmpty())
        {
            this.deleteDeprecated();
        }
        else
        {
            System.out.println("There is not file to update.");
            return;
        }

        DownloadUtils.totalsize = this.getTotalDownloadSize();
        

        this.downloadMod();
        
        if(!error)
        {
            this.finish();
        }
    }

    public void deleteDeprecated()
    {
        for(FileEntry entry : this.fileChecker.outdatedList)
        {
            File f = new File(this.modPackDir, entry.getPath());
            if(!f.delete())
            {
            	System.out.println("Cannot delete file" + " : " + f.getPath());
                
            }
           
        }
    }

    
    private int getTotalDownloadSize()
    {
        int size = 0;
        for(FileEntry entry : this.fileChecker.missingList)
        {
            size += entry.getSize();
        }

        return size;
    }

    public void downloadMod()
    {
       
        for(FileEntry entry : this.fileChecker.missingList)
        {
            File f = new File(this.modPackDir, entry.getPath());
            if(f.getParentFile() != null && !f.getParentFile().isDirectory())
            {
                f.getParentFile().mkdirs();
            }
            this.changeCurrentDownloadText(entry.getPath());
            ShaMain.downloadStarted = true;
            System.out.println("[ShaLibUpdate] - Downloading " + entry.getUrl().toString() + "at" + f.getPath() + entry.getMd5());
            if(!DownloadUtils.downloadFile(entry.getUrl(), f))
            {
                System.out.println("Can't download " + " : " + entry.getUrl().toString());
    
                Thread.currentThread().interrupt();
                this.error = true;
                return;
            }
        }
    }

    

    

    private void changeCurrentDownloadText(final String text)
    {
        EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
            	ShaMain.currentDownloadLink = text;
            }
        });
    }

    public void finish()
    {
        EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {

                ShaMain.downloadFinish = true;
                
            }
        });
       
    }

    
}