package fr.shawiizz.updater;


import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;

import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

import argo.jdom.JdomParser;
import argo.jdom.JsonRootNode;
import argo.saj.InvalidSyntaxException;

public class GetInformations
{
    public static GetInformations instance;
    public JsonRootNode data;
    private final JdomParser parser = new JdomParser();

    public boolean init()
    {
        try
        {
            InputStreamReader reader = getRemoteStream(ShaMain.remoteUrl.toString());
            this.data = this.parser.parse(reader);
            return true;
        }
        catch(InvalidSyntaxException e)
        {
        	System.out.println("An error occured on the json file.");
            return false;
        }
        catch(IOException e)
        {
        	System.out.println("Cannot read remote_info.json file.");
            return false;
        }
        catch(URISyntaxException e)
        {
        	System.out.println("Cannot read remote_info.json file.");
            return false;
        }
    }

    public static GetInformations instance()
    {
        return instance;
    }


    

    public ArrayList<String> getSyncDir()
    {
        return Lists.newArrayList(Splitter.on(',').trimResults().omitEmptyStrings().split(this.data.getStringValue("updater", "syncFolders")));
    }

    public String getSyncUrl()
    {
        return this.data.getStringValue("updater", "filesFolderUrl");
    }
    
    public String getStatus()
    {
        return this.data.getStringValue("updater", "enabled");
    }



    private InputStreamReader getRemoteStream(String str) throws MalformedURLException, IOException, URISyntaxException
    {
        URI uri = new URI(str);
        URLConnection connection = uri.toURL().openConnection();
        connection.setRequestProperty("Accept-Encoding", "gzip");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:10.0) Gecko/20100101 Firefox/55.0");
        InputStreamReader reader = null;
        if("gzip".equals(connection.getContentEncoding()))
        {
            reader = new InputStreamReader(new GZIPInputStream(connection.getInputStream()), Charsets.UTF_8);
        }
        else
        {
            reader = new InputStreamReader(connection.getInputStream(), Charsets.UTF_8);
        }
        return reader;
    }
}
