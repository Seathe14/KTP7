import java.net.*;

public class URLDepthPair {
    private String url;
    private static String baseUrl;
    private int depth;
    public URLDepthPair(String url, int depth)
    {

        this.url = url;
        this.depth = depth;
    }
    public String getUrl()
    {
        return url;
    }
    public int getDepth()
    {
        return depth;
    }
    public String toString()
    {
        String stringDepth = Integer.toString(depth);
        return stringDepth + '\t' + url;
    }
    public void setBaseUrl(String Url)
    {
        baseUrl = Url;
    }
    public String getDocPath()
    {
        try
        {
            URL url = new URL(this.url);
            return url.getPath();
        }
        catch (MalformedURLException e)
        {
            try
            {
                String Url = baseUrl.substring(0,baseUrl.length()-1) + this.url;
                URL url = new URL(Url);
                return url.getPath();
            }
            catch(MalformedURLException ex) {
                System.err.println("MalformedURLException " + e.getMessage());
                return null;
            }
            //System.err.println("MalformedURLException" + e.getMessage());
            //return null;
        }
    }
    public String getWebProtocol()
    {
        try
        {
            URL url = new URL(this.url);
            return url.getProtocol();
        }
        catch(MalformedURLException e)
        {
            try
            {
                String Url = baseUrl.substring(0,baseUrl.length()-1) + this.url;
                URL url = new URL(Url);
                return url.getHost();
            }
            catch(MalformedURLException ex) {
                System.err.println("MalformedURLException " + e.getMessage());
                return null;
            }
            //System.err.println("MalformedURLException " + e.getMessage());
            //return null;
        }
    }
    public String getWebHost()
    {
        try
        {
            URL url = new URL(this.url);
            return url.getHost();
        }
        catch(MalformedURLException e)
        {
            try
            {
                String Url = baseUrl.substring(0,baseUrl.length()-1) + this.url;
                URL url = new URL(Url);
                return url.getHost();
            }
            catch(MalformedURLException ex) {
                System.err.println("MalformedURLException " + e.getMessage());
                return null;
            }
            //System.err.println("MalformedURLException " + e.getMessage());
            //return null;
        }

    }
}
