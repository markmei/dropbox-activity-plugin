package at.markmei.xeneo.plugin;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import java.net.URL;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import org.xeneo.core.plugin.AbstractActivityPlugin;
import org.xeneo.core.plugin.PluginConfiguration;

/*
 * @author Markus Meingassner
 *
 */

public class DropboxActivityPlugin extends AbstractActivityPlugin {
    
    private String folder;
    private String url;
    

    public static void main(String[] args) throws Exception {
        URL url = new URL("https://www.dropbox.com/13081712/20374930/96070ae/events.xml");

        XmlReader reader = null;
        try {
            reader = new XmlReader(url);
            SyndFeed feed = new SyndFeedInput().build(reader);
            for (Iterator i = feed.getEntries().iterator(); i.hasNext();) {
                SyndEntry entry = (SyndEntry) i.next();
                //Set Time
                Date time = entry.getPublishedDate();
                System.out.println("\nTime: " + time);
                //Get Activities
                activities(entry.getDescription().getValue().toString());
            }
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    public List<Activity> getActivities(String input) {
        String document = input.substring(1);
        String action = "";
        String actor = "";
        String object = "";
        String objectURI = "http://www.drobox.com";
        String objectType = "";
        String target = "";
        String targetURI = "http://www.dropbox.com";
        String targetType = "Folder";

        // delete, add, rename, remove, move folders or files
        if (document.startsWith("In") == true) {
            //set targetURI
            String input_new = document.substring(12);
            int targetendindex = input_new.indexOf('\'');
            targetURI = targetURI.concat(input_new.substring(0, targetendindex));

            //set target
            int targetbeginindex = targetendindex + 2;
            String input_target = input_new.substring(targetbeginindex);
            targetendindex = input_target.indexOf("/a") - 1;
            target = input_target.substring(0, targetendindex);

            //set actor
            document = input_target.substring(input_target.indexOf(',') + 2);
            input_target = document;
            targetendindex = 0;
            while ((document.startsWith("deleted") | document.startsWith("added") | document.startsWith("renamed") | document.startsWith("removed") | document.startsWith("moved")) == false) {
                targetendindex++;
                document = input_target.substring(targetendindex);
            }
            actor = input_target.substring(0, targetendindex - 1);

            //set actor
            if (document.startsWith("deleted") | document.startsWith("removed")) {
                action = "DELETE";
            }
            if (document.startsWith("added")) {
                action = "ADD";
            }
            if (document.startsWith("moved") | document.startsWith("renamed")) {
                action = "UPDATE";
            }

            //set object uri and object type
            input_target = document.substring(document.indexOf('\'') + 1);
            objectURI = objectURI.concat(input_target.substring(0, input_target.indexOf('\'')));
            if (objectURI.startsWith("http://www.drobox.comhttps")) {
                objectURI = objectURI.substring(21);
                objectType = "Document";
            } else {
                objectType = "Folder";
            }

            //set ONE object
            document = input_target.substring(input_target.indexOf("&#47;") + 5);
            input_target = document.substring(document.indexOf("&#47;") + 5);
            targetendindex = input_target.indexOf('\'');
            object = input_target.substring(0, targetendindex);

            //set more than one Object
            if (input_target.indexOf("&#47;") > -1) {
                objectType = objectType.concat("s");
                targetendindex = input_target.indexOf('\'');
                object = input_target.substring(input_target.indexOf("&#47;") + 5, targetendindex);
                document = input_target.substring(input_target.indexOf("a href='") + 9);
                targetendindex = document.indexOf('\'');
                objectURI = objectURI.concat(" and http://www.dropbox.com/" + document.substring(0, targetendindex));
            }
            //end if - join folder or invite to folder or leave folder
        } else {
            //set Actor and Verb
            int index = 0;
            while ((document.startsWith("left") | document.startsWith("joined") | document.startsWith("invited")) == false) {
                index++;
                document = input.substring(index);
            }
            actor = input.substring(1, index - 1);
            if (document.startsWith("invited") == true) {
                //set action
                action = "INVITE";

                //set object, objectURI, objectType
                object = document.substring(8, document.indexOf("to the shared folder") - 1);
                objectURI = object;
                objectType = "email";

                //set targetURI
                input = document.substring(document.indexOf('"') + 1);
                targetURI = targetURI.concat(input.substring(0, input.indexOf('"')));

                //set target
                target = input.substring(input.indexOf('"') + 2, input.indexOf('\'') - 4);

                //set target type
                targetType = "folder";

            } else {
                //set action JOIN or LEAVE
                if (document.startsWith("joined")) {
                    action = "JOIN";
                }
                if (document.startsWith("left")) {
                    action = "LEAVE";
                }

                //set ObjectURI
                input = document.substring(document.indexOf('"') + 1);
                objectURI = objectURI.concat(input.substring(0, input.indexOf('"')));

                //set Object
                object = input.substring(input.indexOf('"') + 2, input.indexOf('\'') - 4);

                //no target
                target = "not available";
                targetURI = "not available";
                targetType = "not available";
            }
        }
        System.out.println("Actor: " + actor);
        System.out.println("Action: " + action);
        System.out.println("Object Type: " + objectType);
        System.out.println("Object: " + object);
        System.out.println("Object URI: " + objectURI);
        System.out.println("Target Type: " + targetType);
        System.out.println("Target: " + target);
        System.out.println("Target URI: " + targetURI);
    }

    public void init() {
        
        PluginConfiguration pc = this.getPluginConfiguration();
        Properties ps = pc.getProperties();
        
        if (ps.containsKey("folder")) {
            folder = ps.getProperty("folder");
        }
        
        if (ps.containsKey("url")) {
            url = ps.getProperty("url");
        }        
        
    }

    public void run() {
        
        
        
        
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
