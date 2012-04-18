package at.testingtest.dropboxactivityplugin;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import java.net.URL;
import java.util.Iterator;
/*
 *
 * It Reads and prints any RSS/Atom feed type. <p>
 *
 * @author Alejandro Abdelnur
 *
 */
public class App {

    public static void main(String[] args) throws Exception {
        URL url = new URL("https://www.dropbox.com/13081712/20374930/96070ae/events.xml");

        XmlReader reader = null;
        try {
            reader = new XmlReader(url);
            SyndFeed feed = new SyndFeedInput().build(reader);
            for (Iterator i = feed.getEntries().iterator(); i.hasNext();) {
                SyndEntry entry = (SyndEntry) i.next();
                //Set Time
                System.out.println("\nTime: " + entry.getPublishedDate());
                //Get Activities
                activities(entry.getDescription().getValue().toString());
            }
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    private static void activities(String input) {
        String document = input.substring(1);
        String action = "";
        String actor = "";
        String object = "";
        String objectURI = "http://www.drobox.com";
        String objectType = "";
        String target = "";
        String targetURI = "http://www.dropbox.com";
        String targetType = "";

        // delete, add, rename, remove, move folders or files
        if (document.startsWith("In") == true) {
            //set targetURI
            String input_new = document.substring(12);
            int targetendindex = input_new.indexOf('\'');
            System.out.println("Target URI: " + targetURI.concat(input_new.substring(0, targetendindex)));

            //set target
            int targetbeginindex = targetendindex + 2;
            String input_target = input_new.substring(targetbeginindex);
            targetendindex = input_target.indexOf("/a") - 1;
            target = input_target.substring(0, targetendindex);
            System.out.println("Target: " + target);

            //set action
            document = input_target.substring(input_target.indexOf(',') + 2);
            input_target = document;
            targetendindex = 0;
            while ((document.startsWith("deleted") | document.startsWith("added") | document.startsWith("renamed") | document.startsWith("removed") | document.startsWith("moved")) == false) {
                targetendindex++;
                document = input_target.substring(targetendindex);
            }
            actor = input_target.substring(0, targetendindex - 1);
            System.out.println("Actor: " + actor);

            //set actor
            action = document.substring(0, document.indexOf('\'') - 9);
            System.out.println("Action: " + action);


            //set object uri and object type
            input_target = document.substring(document.indexOf('\'') + 1);
            objectURI = objectURI.concat(input_target.substring(0, input_target.indexOf('\'')));
            if (objectURI.startsWith("http://www.drobox.comhttps")) {
                System.out.println("ObjectURI: " + objectURI.substring(21));
                objectType = "Document";
            } else {
                System.out.println("ObjectURI: " + objectURI);
                objectType = "Folder";
            }
            System.out.println("Object Type: " + objectType);

            //set ONE object
            document = input_target.substring(input_target.indexOf("&#47;") + 5);
            input_target = document.substring(document.indexOf("&#47;") + 5);
            targetendindex = input_target.indexOf('\'');
            object = input_target.substring(0, targetendindex);
            System.out.println("Object: " + object);
            //end if - join folder or invite to folder or leave folder
        } else {
            //set Actor and Verb
            int index = 0;
            while ((document.startsWith("left") | document.startsWith("joined") | document.startsWith("invited")) == false) {
                index++;
                document = input.substring(index);
            }
            actor = input.substring(1, index - 1);
            System.out.println("Actor: " + actor);

            if (document.startsWith("invited") | actor.matches("invited") == true)
            {
                //set action
                action = document.substring(0, 7);
                
                //set object
                object = document.substring(8, document.indexOf("to the shared folder") - 1);
                System.out.println("Action: " + action);
                System.out.println("Object: " + object);
                objectURI = object;
                objectType = "email";
                System.out.println("Object URI: " + objectURI);
                System.out.println("Object Type: " + objectType);
                
                //set targetURI
                input = document.substring(document.indexOf('"') + 1);
                targetURI=targetURI.concat(input.substring(0, input.indexOf('"')));
                System.out.println("Target URI: " + targetURI);
                
                //set target
                target = input.substring(input.indexOf('"') + 2, input.indexOf('\'') - 4);
                System.out.println("Target: " + target);
                
                //set target type
                targetType = "folder";
                System.out.println("Target Type:" + targetType);
            } else {
                action = document.substring(0, document.indexOf('\''));
                System.out.println("Action: " + action);

                //set ObjectURI
                input = document.substring(document.indexOf('"') + 1);
                objectURI = objectURI.concat(input.substring(0, input.indexOf('"')));
                System.out.println("Object URI: " + objectURI);

                //set Object
                object = input.substring(input.indexOf('"') + 2, input.indexOf('\'') - 4);
                System.out.println("Object: " + object);

                //set Object Type
                objectType = "folder";
                System.out.println("Object Type: " + objectType);
            }
        }
    }
}
