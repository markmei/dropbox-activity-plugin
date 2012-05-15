/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.markmei.xeneo.plugin;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.junit.Assert.assertEquals;
import org.junit.*;
import org.xeneo.core.activity.Activity;
import org.xeneo.core.plugin.PluginConfiguration;
import org.xeneo.core.plugin.Plugin;

/**
 *
 * @author Stefan
 */
public class DropboxActivityPluginTest {

    DropboxActivityPlugin dap = new DropboxActivityPlugin();

    public DropboxActivityPluginTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {

        PluginConfiguration pc = new PluginConfiguration();

        Properties ps = new Properties();
        //ps.setProperty("url", "C:/Users/XENEO/Documents/NetBeansProjects/DropboxActivityPlugin/src/test/resources/testDropboxActivities.xml");
        //ps.setProperty("folder", "xeneo");

        pc.setInstanceProperties(ps);

        dap.setPluginConfiguration(pc);
        dap.init();

    }

    @After
    public void tearDown() {
    }

    @Test
    public void testGetActivities() {
        try {
            List<Activity> acts = dap.getActivities();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(DropboxActivityPluginTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(DropboxActivityPluginTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FeedException ex) {
            Logger.getLogger(DropboxActivityPluginTest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DropboxActivityPluginTest.class.getName()).log(Level.SEVERE, null, ex);
        }


    }

    @Test
    public void testAssembleActivity() throws FileNotFoundException, IOException, IllegalArgumentException, FeedException {

        FileInputStream fis = new FileInputStream("C:/Users/XENEO/Documents/NetBeansProjects/AP_Dropbox/src/test/resources/testDropboxActivities.xml");

        SyndFeedInput input = new SyndFeedInput();
        SyndFeed sf = input.build(new XmlReader(fis));
        List entries = sf.getEntries();
        Iterator it = entries.iterator();

        //case 1: Actor leave Object - no target
        while (it.hasNext()) {
            SyndEntry se = (SyndEntry) it.next();
            Activity a = dap.assembleActivity(se);
            assertEquals(a.getCreationDate().toString(), "Mon Apr 30 11:08:35 CEST 2012");
            assertEquals(a.getActivityURI(), "DROPBOX_DFC922733D30EDEB008B59422A446A2D");
            assertEquals(a.getActionURI(), "LEAVE");
            assertEquals(a.getActor().getActorName(), "John Connor");
            assertEquals(a.getActor().getActorURI(), "www.dropbox.com/John Connor");
            assertEquals(a.getActor().getActivityProviderURI(), "http://www.dropbox.com");
            assertEquals(a.getDescription(), "John Connor" + " " + "LEAVE" + " " + "XENEOTEST" + " " + "");
            assertEquals(a.getObject().getObjectName(), "XENEOTEST");
            assertEquals(a.getObject().getObjectTypeURI(), "Folder");
            assertEquals(a.getObject().getObjectURI(), "http://www.dropbox.com/home/XENEOTEST");
            assertEquals(a.getTarget().getObjectName(), "");
            assertEquals(a.getTarget().getObjectTypeURI(), "");
            assertEquals(a.getTarget().getObjectURI(), "");
            assertEquals(a.getSummary(), null);
            break;
        }

        //case 2: Actor delete Document in Folder
        while (it.hasNext()) {
            SyndEntry se = (SyndEntry) it.next();
            Activity a = dap.assembleActivity(se);
            assertEquals(a.getCreationDate().toString(), "Mon Apr 30 11:07:25 CEST 2012");
            assertEquals(a.getActivityURI(), "DROPBOX_739BC65934B7B7C40747CE76371B0D22");
            assertEquals(a.getActionURI(), "DELETE");
            assertEquals(a.getActor().getActorName(), "John Connor");
            assertEquals(a.getActor().getActorURI(), "www.dropbox.com/John Connor");
            assertEquals(a.getActor().getActivityProviderURI(), "http://www.dropbox.com");
            assertEquals(a.getDescription(), "John Connor" + " " + "DELETE" + " " + "drdrdrdrdrdrdrdr" + " " + "XENEOTEST");
            assertEquals(a.getObject().getObjectName(), "drdrdrdrdrdrdrdr");
            assertEquals(a.getObject().getObjectTypeURI(), "File");
            assertEquals(a.getObject().getObjectURI(), "https://dl-web.dropbox.com/get/XENEOTEST/drdrdrdrdrdrdrdr?w=db418234");
            assertEquals(a.getTarget().getObjectName(), "XENEOTEST");
            assertEquals(a.getTarget().getObjectTypeURI(), "Folder");
            assertEquals(a.getTarget().getObjectURI(), "http://www.dropbox.com/home/XENEOTEST");
            assertEquals(a.getSummary(), null);
            break;
        }

        //case 3: Actor delete Folder in Folder        
        while (it.hasNext()) {
            SyndEntry se = (SyndEntry) it.next();
            Activity a = dap.assembleActivity(se);
            assertEquals(a.getCreationDate().toString(), "Mon Apr 30 11:07:22 CEST 2012");
            assertEquals(a.getActivityURI(), "DROPBOX_A404B3C0E6ED88F46FB361793AF65D70");
            assertEquals(a.getActionURI(), "DELETE");
            assertEquals(a.getActor().getActorName(), "John Connor");
            assertEquals(a.getActor().getActorURI(), "www.dropbox.com/John Connor");
            assertEquals(a.getActor().getActivityProviderURI(), "http://www.dropbox.com");
            assertEquals(a.getDescription(), "John Connor DELETE asdfdfasdfasdf XENEOTEST");
            assertEquals(a.getObject().getObjectName(), "asdfdfasdfasdf");
            assertEquals(a.getObject().getObjectTypeURI(), "Folder");
            assertEquals(a.getObject().getObjectURI(), "http://www.dropbox.com/c/browse/asdfdfasdfasdf?ns_id=119834745");
            assertEquals(a.getTarget().getObjectName(), "XENEOTEST");
            assertEquals(a.getTarget().getObjectTypeURI(), "Folder");
            assertEquals(a.getTarget().getObjectURI(), "http://www.dropbox.com/home/XENEOTEST");
            assertEquals(a.getSummary(), null);
            break;
        }

        //case 4: Actor add Folder in Folder
        while (it.hasNext()) {
            SyndEntry se = (SyndEntry) it.next();
            Activity a = dap.assembleActivity(se);
            assertEquals(a.getCreationDate().toString(), "Mon Apr 30 11:07:18 CEST 2012");
            assertEquals(a.getActivityURI(), "DROPBOX_01A517743BC22D3B9E01140EBA8D1217");
            assertEquals(a.getActionURI(), "ADD");
            assertEquals(a.getActor().getActorName(), "John Connor");
            assertEquals(a.getActor().getActorURI(), "www.dropbox.com/John Connor");
            assertEquals(a.getActor().getActivityProviderURI(), "http://www.dropbox.com");
            assertEquals(a.getDescription(), "John Connor ADD asdfdfasdfasdf XENEOTEST");
            assertEquals(a.getObject().getObjectName(), "asdfdfasdfasdf");
            assertEquals(a.getObject().getObjectTypeURI(), "Folder");
            assertEquals(a.getObject().getObjectURI(), "http://www.dropbox.com/c/browse/asdfdfasdfasdf?ns_id=119834745");
            assertEquals(a.getTarget().getObjectName(), "XENEOTEST");
            assertEquals(a.getTarget().getObjectTypeURI(), "Folder");
            assertEquals(a.getTarget().getObjectURI(), "http://www.dropbox.com/home/XENEOTEST");
            assertEquals(a.getSummary(), null);
            break;
        }


        //case 5: Actor delete Folder in Folder
        while (it.hasNext()) {
            SyndEntry se = (SyndEntry) it.next();
            Activity a = dap.assembleActivity(se);
            assertEquals(a.getCreationDate().toString(), "Mon Apr 30 11:07:18 CEST 2012");
            assertEquals(a.getActivityURI(), "DROPBOX_67E267F1A0ED07037C46E3F2B83AD081");
            assertEquals(a.getActionURI(), "DELETE");
            assertEquals(a.getActor().getActorName(), "John Connor");
            assertEquals(a.getActor().getActorURI(), "www.dropbox.com/John Connor");
            assertEquals(a.getActor().getActivityProviderURI(), "http://www.dropbox.com");
            assertEquals(a.getDescription(), "John Connor DELETE blalbla XENEOTEST");
            assertEquals(a.getObject().getObjectName(), "blalbla");
            assertEquals(a.getObject().getObjectTypeURI(), "Folder");
            assertEquals(a.getObject().getObjectURI(), "http://www.dropbox.com/c/browse/blalbla?ns_id=119834745");
            assertEquals(a.getTarget().getObjectName(), "XENEOTEST");
            assertEquals(a.getTarget().getObjectTypeURI(), "Folder");
            assertEquals(a.getTarget().getObjectURI(), "http://www.dropbox.com/home/XENEOTEST");
            assertEquals(a.getSummary(), null);
            break;
        }


        //case 6: Rename File in Folder
        while (it.hasNext()) {
            SyndEntry se = (SyndEntry) it.next();
            Activity a = dap.assembleActivity(se);
            assertEquals(a.getCreationDate().toString(), "Mon Apr 30 11:07:17 CEST 2012");
            assertEquals(a.getActivityURI(), "DROPBOX_FAB9541F8C5205D2A78CA455FBECC18B");
            assertEquals(a.getActionURI(), "UPDATE");
            assertEquals(a.getActor().getActorName(), "John Connor");
            assertEquals(a.getActor().getActorURI(), "www.dropbox.com/John Connor");
            assertEquals(a.getActor().getActivityProviderURI(), "http://www.dropbox.com");
            assertEquals(a.getDescription(), "John Connor UPDATE drdrdrdrdrdrdrdr XENEOTEST");
            assertEquals(a.getObject().getObjectName(), "drdrdrdrdrdrdrdr");
            assertEquals(a.getObject().getObjectTypeURI(), "File");
            assertEquals(a.getObject().getObjectURI(), "https://dl-web.dropbox.com/get/XENEOTEST/drdrdrdrdrdrdrdr?w=db418234");
            assertEquals(a.getTarget().getObjectName(), "XENEOTEST");
            assertEquals(a.getTarget().getObjectTypeURI(), "Folder");
            assertEquals(a.getTarget().getObjectURI(), "http://www.dropbox.com/home/XENEOTEST");
            assertEquals(a.getSummary(), null);
            break;
        }


        //case 7: Delete Files in Folder

        while (it.hasNext()) {
            SyndEntry se = (SyndEntry) it.next();
            Activity a = dap.assembleActivity(se);
            assertEquals(a.getCreationDate().toString(), "Mon Apr 30 11:07:01 CEST 2012");
            assertEquals(a.getActivityURI(), "DROPBOX_EE1F2603DCC20A794B237B1ACF111FE9");
            assertEquals(a.getActionURI(), "DELETE");
            assertEquals(a.getActor().getActorName(), "John Connor");
            assertEquals(a.getActor().getActorURI(), "www.dropbox.com/John Connor");
            assertEquals(a.getActor().getActivityProviderURI(), "http://www.dropbox.com");
            assertEquals(a.getDescription(), "John Connor DELETE Files XENEOTEST");
            assertEquals(a.getObject().getObjectName(), "Files");
            assertEquals(a.getObject().getObjectTypeURI(), "Files");
            assertEquals(a.getObject().getObjectURI(), "http://www.dropbox.com/events/119834745/226832284");
            assertEquals(a.getTarget().getObjectName(), "XENEOTEST");
            assertEquals(a.getTarget().getObjectTypeURI(), "Folder");
            assertEquals(a.getTarget().getObjectURI(), "http://www.dropbox.com/home/XENEOTEST");
            assertEquals(a.getSummary(), null);
            break;
        }


        //case 8: Join folder
        while (it.hasNext()) {
            SyndEntry se = (SyndEntry) it.next();
            Activity a = dap.assembleActivity(se);
            assertEquals(a.getCreationDate().toString(), "Mon Apr 30 11:05:40 CEST 2012");
            assertEquals(a.getActivityURI(), "DROPBOX_65481590E4F6CF2A44B78DCA60CEFBF5");
            assertEquals(a.getActionURI(), "JOIN");
            assertEquals(a.getActor().getActorName(), "John Connor");
            assertEquals(a.getActor().getActorURI(), "www.dropbox.com/John Connor");
            assertEquals(a.getActor().getActivityProviderURI(), "http://www.dropbox.com");
            assertEquals(a.getDescription(), "John Connor" + " " + "JOIN" + " " + "XENEOTEST" + " " + "");
            assertEquals(a.getObject().getObjectName(), "XENEOTEST");
            assertEquals(a.getObject().getObjectTypeURI(), "Folder");
            assertEquals(a.getObject().getObjectURI(), "http://www.dropbox.com/home/XENEOTEST");
            assertEquals(a.getTarget().getObjectName(), "");
            assertEquals(a.getTarget().getObjectTypeURI(), "");
            assertEquals(a.getTarget().getObjectURI(), "");
            assertEquals(a.getSummary(), null);
            break;
        }
        
        
        //case 9: Invite Object (email) to Folder
        while (it.hasNext()) {
            SyndEntry se = (SyndEntry) it.next();
            Activity a = dap.assembleActivity(se);
            assertEquals(a.getCreationDate().toString(), "Mon Apr 30 11:05:06 CEST 2012");
            assertEquals(a.getActivityURI(), "DROPBOX_DBEEFD33942A55CDE8F380C36DCCBCEB");
            assertEquals(a.getActionURI(), "INVITE");
            assertEquals(a.getActor().getActorName(), "You");
            assertEquals(a.getActor().getActorURI(), "www.dropbox.com/You");
            assertEquals(a.getActor().getActivityProviderURI(), "http://www.dropbox.com");
            assertEquals(a.getDescription(), "You" + " " + "INVITE" + " " + "mmeingassner@yahoo.de" + " " + "XENEOTEST");
            assertEquals(a.getObject().getObjectName(), "mmeingassner@yahoo.de");
            assertEquals(a.getObject().getObjectTypeURI(), "email");
            assertEquals(a.getObject().getObjectURI(), "mmeingassner@yahoo.de");
            assertEquals(a.getTarget().getObjectName(), "XENEOTEST");
            assertEquals(a.getTarget().getObjectTypeURI(), "Folder");
            assertEquals(a.getTarget().getObjectURI(), "http://www.dropbox.com/home/XENEOTEST");
            assertEquals(a.getSummary(), null);
            break;
        }
        
        //case 10: Add Files to Folder
        
            while (it.hasNext()) {
            SyndEntry se = (SyndEntry) it.next();
            Activity a = dap.assembleActivity(se);
            assertEquals(a.getCreationDate().toString(), "Mon Apr 30 11:04:16 CEST 2012");
            assertEquals(a.getActivityURI(), "DROPBOX_E6E8AE5F2842448705737775EE56E8EE");
            assertEquals(a.getActionURI(), "ADD");
            assertEquals(a.getActor().getActorName(), "You");
            assertEquals(a.getActor().getActorURI(), "www.dropbox.com/You");
            assertEquals(a.getActor().getActivityProviderURI(), "http://www.dropbox.com");
            assertEquals(a.getDescription(), "You ADD Files XENEOTEST");
            assertEquals(a.getObject().getObjectName(), "Files");
            assertEquals(a.getObject().getObjectTypeURI(), "Files");
            assertEquals(a.getObject().getObjectURI(), "http://www.dropbox.com/events/119834745/226831705");
            assertEquals(a.getTarget().getObjectName(), "XENEOTEST");
            assertEquals(a.getTarget().getObjectTypeURI(), "Folder");
            assertEquals(a.getTarget().getObjectURI(), "http://www.dropbox.com/home/XENEOTEST");
            assertEquals(a.getSummary(), null);
            return;
        }
        
        
    }
}