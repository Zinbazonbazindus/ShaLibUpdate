package fr.shawiizz.updater;

import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.zip.GZIPInputStream;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import com.google.common.io.Files;

import argo.jdom.JdomParser;
import argo.jdom.JsonNode;
import argo.jdom.JsonRootNode;

public class DownloadUtils
{
	public static int totalsize=0;
	public static int filesize=0;
	public static int currentFileCount=0;
public static double progressfull = 0.0;
public static double progressfile = 0.0;
public static int calcprogressfull = 0;
    public static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat();
    {
        DECIMAL_FORMAT.setMaximumFractionDigits(2);
    }

    public static void readRemoteList(List<FileEntry> files, List<String> dirs)
    {
        try
        {
            URL resourceUrl = new URL(GetInformations.instance().getSyncUrl());
            URLConnection connection = resourceUrl.openConnection();
            connection.setRequestProperty("Accept-Encoding", "gzip");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:10.0) Gecko/20100101 Firefox/55.0");

            JdomParser parser = new JdomParser();
            InputStreamReader reader = null;
            if("gzip".equals(connection.getContentEncoding()))
            {
                reader = new InputStreamReader(new GZIPInputStream(connection.getInputStream()), Charsets.UTF_8);
            }
            else
            {
                reader = new InputStreamReader(connection.getInputStream(), Charsets.UTF_8);
            }
            JsonRootNode data = parser.parse(reader);

            for(int i = 0; i < data.getElements().size(); i++)
            {
                JsonNode node = data.getElements().get(i);
                String key = node.getStringValue("name");
                long size = Long.parseLong(node.getStringValue("size"));
                String md5 = node.getStringValue("md5");

                if(size > 0L)
                {
                    String link = GetInformations.instance().getSyncUrl() + DownloadUtils.escapeURIPathParam(key);
                    files.add(new FileEntry(new URL(link), md5, key, size));
                }
                
                else if(key.split("/").length == 1)
                {
                    // only add the folder if it's in modpack root folder
                    dirs.add(key.substring(0, key.length() - 1));
                }
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            System.out.println("Erreur réseau, veuillez vérifier votre connexion internet");
         
        }
    }

    public static boolean downloadFile(final URL url, final File dest)
    {
      

        FileOutputStream fos = null;
        BufferedReader reader = null;

        try
        {
            URLConnection connection = url.openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:10.0) Gecko/20100101 Firefox/55.0");

            filesize = connection.getContentLength();
            currentFileCount++;
            

            InputStream in = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(in));
            fos = new FileOutputStream(dest);

            long downloadStartTime = System.currentTimeMillis();
            int downloadedAmount = 0;
            byte[] buff = new byte[1024];

            int progress = 0;
            while((progress = in.read(buff)) != -1)
            {
                fos.write(buff, 0, progress);
                addProgress(progress);
                downloadedAmount += progress;
                long timeLapse = System.currentTimeMillis() - downloadStartTime;
                if(timeLapse >= 250L)
                {
                    final float downloadSpeed = downloadedAmount / (float)timeLapse;

                    downloadedAmount = 0;
                    downloadStartTime += 250L;
                    if(downloadSpeed > 1024F)
                    {
                        changeDownloadSpeed(String.valueOf(DECIMAL_FORMAT.format(downloadSpeed / 1024F)));

                    }
                    else
                    {
                        changeDownloadSpeed(String.valueOf(DECIMAL_FORMAT.format(downloadSpeed)));
                    }
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            System.out.println("Invalid URL");
            return false;
        }
        finally
        {
            try
            {
                if(fos != null)
                {
                    fos.flush();
                    fos.close();
                }
                if(reader != null)
                {
                    reader.close();
                }
            }
            catch(IOException e)
            {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    private static void addProgress(final int progress)
    {
        EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
            	/*CHECK IF FILE IS A NEW FILE*/
            	if (currentFileCount == 2) {
            		progressfile = 0;
            		currentFileCount = 1;
				}
            	/*############################*/
            	/*CALCULATE FULL PERCENTAGE*/
            	progressfull=progress+progressfull; 
            	ShaMain.globalprogress =(int) ((double) progressfull/totalsize*100);
            	
            	/*############################*/
            	/*CALCULATE FILE PERCENTAGE*/
            	progressfile=progress+progressfile;
            	ShaMain.fileprogress =(int) ((double) progressfile/filesize*100);
            	/*############################*/
            }
        });
    }

    private static void changeDownloadSpeed(final String text)
    {
        EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
            	ShaMain.downloadSpeed = text;
            }
        });
    }

    public static boolean checksumValid(File libPath, List<String> checksums)
    {
        try
        {
            byte[] fileData = Files.toByteArray(libPath);
            boolean valid = checksums == null || checksums.isEmpty() || checksums.contains(Hashing.sha1().hashBytes(fileData).toString());
            if(!valid && libPath.getName().endsWith(".jar"))
            {
                valid = validateJar(libPath, fileData, checksums);
            }
            return valid;
        }
        catch(IOException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean validateJar(File libPath, byte[] data, List<String> checksums) throws IOException
    {
    	//Checking %s internal checksums


        HashMap<String, String> files = new HashMap<String, String>();
        String[] hashes = null;
        JarInputStream jar = new JarInputStream(new ByteArrayInputStream(data));
        JarEntry entry = jar.getNextJarEntry();
        while(entry != null)
        {
            byte[] eData = readFully(jar);

            if(entry.getName().equals("checksums.sha1"))
            {
                hashes = new String(eData, Charset.forName("UTF-8")).split("\n");
            }

            if(!entry.isDirectory())
            {
                files.put(entry.getName(), Hashing.sha1().hashBytes(eData).toString());
            }
            entry = jar.getNextJarEntry();
        }
        jar.close();

        if(hashes != null)
        {
            boolean failed = !checksums.contains(files.get("checksums.sha1"));
            if(failed)
            {
                System.err.println("Failed checksums.sha1 validation!");
            }
            else
            {
            	//Successfully validated checksums.sha1

                for(String hash : hashes)
                {
                    if(hash.trim().equals("") || !hash.contains(" "))
                        continue;
                    String[] e = hash.split(" ");
                    String validChecksum = e[0];
                    String target = e[1];
                    String checksum = files.get(target);

                    if(!files.containsKey(target) || checksum == null)
                    {
                        System.err.println("    " + target + " : " + "Not here");
                        failed = true;
                    }
                    else if(!checksum.equals(validChecksum))
                    {
                        System.err.println("    " + target + " : " + "Failed" + " (" + checksum + ", " + validChecksum + ")");
                        failed = true;
                    }
                }
            }

            if(!failed)
            {
            	//Jar contents validated successfully
                
            }

            return !failed;
        }
        else
        {
        	//checksums.sha1 was not found, validation failed
            return false; 
        }
    }

    public static byte[] readFully(InputStream stream) throws IOException
    {
        byte[] data = new byte[4096];
        ByteArrayOutputStream entryBuffer = new ByteArrayOutputStream();
        int len;
        do
        {
            len = stream.read(data);
            if(len > 0)
            {
                entryBuffer.write(data, 0, len);
            }
        }
        while(len != -1);

        return entryBuffer.toByteArray();
    }



    public static String escapeURIPathParam(String input)
    {
        StringBuilder resultStr = new StringBuilder();
        for(char ch : input.toCharArray())
        {
            if(isUnsafe(ch))
            {
                resultStr.append('%');
                resultStr.append(toHex(ch / 16));
                resultStr.append(toHex(ch % 16));
            }
            else
            {
                resultStr.append(ch);
            }
        }
        return resultStr.toString();
    }

    private static char toHex(int ch)
    {
        return (char)(ch < 10 ? '0' + ch : 'A' + ch - 10);
    }

    private static boolean isUnsafe(char ch)
    {
        if(ch > 128 || ch < 0)
            return true;
        return " %$&+,:;=?@<>#%".indexOf(ch) >= 0;
    }
}