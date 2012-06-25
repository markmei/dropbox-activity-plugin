package at.markmei.xeneo.plugin;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xeneo.core.activity.Activity;
import org.xeneo.core.activity.Actor;
import org.xeneo.core.activity.Object;
import org.xeneo.core.plugin.PluginProperty;
import org.xeneo.core.plugin.activity.AbstractActivityPlugin;
import org.xeneo.core.plugin.activity.AbstractActivityPlugin;
import org.xeneo.core.plugin.PluginConfiguration;

/*
 * @author Markus Meingassner
 *
 */
public class DropboxActivityPlugin extends AbstractActivityPlugin {

    private static Logger logger = LoggerFactory.getLogger(DropboxActivityPlugin.class);
        
    private String url;
    private String activityUri;
    
    

    public void init() {        
        logger.info("Dropbox Activity Plugin initializing...");
        PluginProperty[] pps = getPluginProperties();
        logger.info(pps.length + " Parameter available...");
        for (PluginProperty p : pps) {
            if (p.getName().equalsIgnoreCase("feed-url")) {
                logger.info("Parameter feed-url is set to: " + p.getValue());
                url = p.getValue();
            }
        }
    }    

    public List<Activity> getActivities() {

        List<Activity> acts = new ArrayList<Activity>();

        logger.info("Dropbox Activity Plugin tries to retrive Activities...");         

        try {
            URL feed = new URL(url);
            
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed sf = input.build(new XmlReader(feed));
            List entries = sf.getEntries();
            Iterator it = entries.iterator();
            while (it.hasNext()) {
                SyndEntry entry = (SyndEntry) it.next();
                assembleActivity(entry);
            }           
            
        } catch (IOException ex) {
            logger.error("Stream with URL: "+ url +" is unloadable:" + ex.getMessage());
        } catch (IllegalArgumentException ex) {
            logger.error(ex.getMessage());
        } catch (FeedException ex) {
            logger.error(ex.getMessage());
        } 
        
        return acts;

    }

    public Activity assembleActivity(SyndEntry se) {
        Activity a = new Activity();
        a.setActivityURI(se.getUri());
        a.setCreationDate(se.getPublishedDate());
        String document = se.getDescription().getValue().substring(1);
        String input = document;
        String action = "";
        String actor = "";
        String object = "";
        String objectURI = "http://www.dropbox.com";
        String objectType = "";
        String target = "";
        String targetURI = "http://www.dropbox.com";
        String targetType = "Folder";
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
            while ((document.startsWith("deleted") | document.startsWith("added") | document.startsWith("renamed") | document.startsWith("removed") | document.startsWith("moved") | document.startsWith("edited")) == false) {
                targetendindex++;
                document = input_target.substring(targetendindex);
            }
            actor = input_target.substring(0, targetendindex - 1);

            //set action
            if (document.startsWith("deleted") | document.startsWith("removed")) {
                action = "DELETE";
            }
            if (document.startsWith("added")) {
                action = "ADD";
            }
            if (document.startsWith("moved") | document.startsWith("renamed") | document.startsWith("edited")) {
                action = "UPDATE";
            }

            //set object uri and object type
            input_target = document.substring(document.indexOf('\'') + 1);
            objectURI = objectURI.concat(input_target.substring(0, input_target.indexOf('\'')));
            if (objectURI.startsWith("http://www.dropbox.comhttps")) {
                objectURI = objectURI.substring(22);
                objectType = "File";
            } else {
                objectType = "Folder";
            }

            //set ONE object
            document = input_target.substring(input_target.indexOf("&#47;") + 5);
            input_target = document.substring(document.indexOf("&#47;") + 5);
            targetendindex = input_target.indexOf('\'');
            object = input_target.substring(0, targetendindex);

            //set more than one Object
            if (document.indexOf("a href='") != -1) {
                objectType = objectType.concat("s");
                //targetendindex = input_target.indexOf('\'');
                //object = input_target.substring(input_target.indexOf("&#47;") + 5, targetendindex);
                object = objectType;
                document = input_target.substring(input_target.indexOf("a href='") + 9);
                targetendindex = document.indexOf('\'');
                objectURI = "http://www.dropbox.com/" + document.substring(0, targetendindex);
            }
            //end if - join folder or invite to folder or leave folder
        } else {
            //set Actor and Action
            int index = 0;
            while ((document.startsWith("left") | document.startsWith("joined") | document.startsWith("invited") | document.startsWith("added")) == false) {
                index++;
                
                document = input.substring(index);
                //check for useless Dropbox announcments
                if(index >40 == true){
                    return null;}
            }
            
            
            actor = input.substring(0, index - 1);
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
                targetType = "Folder";

            } 
            if ((document.startsWith("joined") | document.startsWith("left")) == true)
            {
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
                
                //Set ObjectType
                objectType = "Folder";
               
                //no target
                target = "";
                targetURI = "";
                targetType = "";
            }
            if (document.startsWith("added")){
                //set object uri and object type
                action = "ADD";
            input = document.substring(document.indexOf('\'') + 1);
            objectURI = objectURI.concat(input.substring(0, input.indexOf('\'')));
            if (objectURI.startsWith("http://www.dropbox.comhttps")) {
                objectURI = objectURI.substring(22);
                objectType = "File";
            } else {
                objectType = "Folder";
            }

            //set ONE object
            document = input.substring(input.indexOf("&#47;") + 5);
            input = document;
            int ix = input.indexOf('\'');
            object = input.substring(0, ix);
            //set more than one Object
            if (document.indexOf("a href='") != -1) {
                objectType = objectType.concat("s");
                //targetendindex = input_target.indexOf('\'');
                //object = input_target.substring(input_target.indexOf("&#47;") + 5, targetendindex);
                object = objectType;
                document = input.substring(input.indexOf("a href='") + 9);
                ix = document.indexOf('\'');
                objectURI = "http://www.dropbox.com/" + document.substring(0, ix);
            }
             //no target
                target = "";
                targetURI = "";
                targetType = "";
            }
                
            
        }
        Object o = new Object();
        Object t = new Object();
        o.setObjectName(object);
        o.setObjectTypeURI(objectType);
        o.setObjectURI(objectURI);
        t.setObjectName(target);
        t.setObjectTypeURI(targetType);
        t.setObjectURI(targetURI);
        a.setActionURI(action);
        Actor act = new Actor();
        act.setActorName(actor);
        act.setActorURI("www.dropbox.com/"+actor);
        act.setActivityProviderURI("http://www.dropbox.com");
        a.setActor(act);
        a.setDescription(actor + " " + action + " " + o.getObjectName() + " " +o.getObjectURI()+" "+o.getObjectTypeURI()+ " " + t.getObjectName()+ " " + t.getObjectURI()+" "+t.getObjectTypeURI() );
        a.setObject(o);
        a.setTarget(t);
        logger.info(a.getActivityURI()+" "+a.getCreationDate()+" "+a.getDescription());

        return a;

    }
}
