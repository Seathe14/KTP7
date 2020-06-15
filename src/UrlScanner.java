import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

public class UrlScanner {

    public static void main(String[] args)
    {
        int depth = 0;
        if(args.length!=2)
        {
            System.out.println("usage: java UrlScanner <URL> <depth>");
            System.exit(1);
        }
        else
        {
            try
            {
                depth = Integer.parseInt(args[1]);
            }
            catch(NumberFormatException nfe)
            {
                System.out.println("usage: java UrlScanner <URL> <depth>");
                System.exit(1);
            }
            LinkedList<URLDepthPair> pendingUrls = new LinkedList<URLDepthPair>();
            LinkedList<URLDepthPair> proccessedUrls = new LinkedList<URLDepthPair>();
            URLDepthPair currentDepthPair = new URLDepthPair(args[0],0);
            currentDepthPair.setBaseUrl(args[0]);
            pendingUrls.add(currentDepthPair);
            ArrayList<String> seenUrls = new ArrayList<String>();
            seenUrls.add(currentDepthPair.getUrl());
            while(pendingUrls.size()!=0)
            {
                URLDepthPair depthPair = pendingUrls.pop();
                proccessedUrls.add(depthPair);
                int myDepth = depthPair.getDepth();

                LinkedList<String> linksList = new LinkedList<String>();
                linksList = UrlScanner.getSites(depthPair);
                if(myDepth < depth)
                {
                    for(int i =0 ;i<linksList.size();i++)
                    {
                        String newUrl = linksList.get(i);
                        if(seenUrls.contains(newUrl))
                            continue;
                        else
                        {
                            URLDepthPair newDepthPair = new URLDepthPair(newUrl,myDepth+1);
                            pendingUrls.add(newDepthPair);
                            seenUrls.add(newUrl);
                        }
                    }
                }
            }
            for(int i =0;i<proccessedUrls.size();i++)
            {
                System.out.println(proccessedUrls.get(i));
            }
        }
    }



    private static LinkedList<String> getSites(URLDepthPair depthPair)
    {
        LinkedList<String> Urls = new LinkedList<String>();
        SSLSocket socket;
        SSLSocketFactory factory;

        try {
            factory = (SSLSocketFactory)SSLSocketFactory.getDefault();
            socket = (SSLSocket)factory.createSocket(depthPair.getWebHost(),443);
        }
        catch (UnknownHostException e) {
            System.err.println("UnknownHostException: " + e.getMessage());
            return Urls;
        }
        catch (IOException ex) {
            System.err.println("IOException: C " + ex.getMessage());
            return Urls;
        }

        try {
            socket.setSoTimeout(2000);
        }
        catch (SocketException exc) {
            System.err.println("SocketException: " + exc.getMessage());
            return Urls;
        }

        String docPath = depthPair.getDocPath();
        String webHost = depthPair.getWebHost();

        OutputStream outStream;

        try {
            outStream = socket.getOutputStream();
        }
        catch (IOException iex) {
            System.err.println("IOException: B " + iex.getMessage());
            return Urls;
        }

        PrintWriter myWriter = new PrintWriter(outStream, true);

        myWriter.println("GET " + docPath + " HTTP/1.1");
        myWriter.println("Host: " + webHost);
        myWriter.println("Connection: close");
        myWriter.println();
        int gg=0;
        if(socket.isClosed())
        {
            gg++;
            System.out.println("CLOSED");
        }
        InputStream inStream = null;

        try {

            inStream = socket.getInputStream();
        }

        catch (IOException iex){
            System.err.println("IOException: A " + iex.getMessage());
            return Urls;
        }

        InputStreamReader inStreamReader = new InputStreamReader(inStream);
        BufferedReader buffReader = new BufferedReader(inStreamReader);
        while(true) {
            String line;
            try {
                line = buffReader.readLine();
            } catch (IOException ioe) {
                System.err.println("IOException" + ioe.getMessage());
                return Urls;
            }
            if (line == null)
                break;
            if(socket.isClosed())
                System.out.println("SOMEWHERE HERE");
            //while(line.contains("a href=\"" + depthPair.getWebProtocol()))
            //{
            //    line = line.substring(line.indexOf("a href=\"" + depthPair.getWebProtocol()) + 8);
            //    String link = line.substring(0,line.indexOf('\"'));
            //    Urls.add(link);
            //}
            int beginIndex = 0;
            int endIndex = 0;
            int index = 0;
            try {
                while (true) {
                    String bj = depthPair.getWebProtocol();
                    String URL_INDICATOR = "a href=\"" + depthPair.getWebProtocol();
                    String END_URL = "\"";

                    index = line.indexOf(URL_INDICATOR, index);
                    if (index == -1)
                        break;
                    index += URL_INDICATOR.length() - depthPair.getWebProtocol().length();
                    beginIndex = index;
                    endIndex = line.indexOf(END_URL, index);
                    if(endIndex==-1)
                        break;
                    index = endIndex;
                    String newLink = line.substring(beginIndex, endIndex);
                    Urls.add(newLink);
                }
            }
            catch(StringIndexOutOfBoundsException e)
            {
                e.getCause();
            }
        }
        try {
            socket.close();
        }
        catch(IOException iox)
        {

        }
      //try {
      //    sock.close();
      //}
      //catch(IOException ioe)
      //{
      //    System.err.println("IOException" + ioe.getMessage());
      //}
        return Urls;
    }
}
