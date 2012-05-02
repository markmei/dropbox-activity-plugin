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
        ps.setProperty("url", "C:/Users/XENEO/Documents/NetBeansProjects/DropboxActivityPlugin/src/test/resources/testDropboxActivities.xml");
        //ps.setProperty("folder", "xeneo");

        pc.setProperties(ps);

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

        FileInputStream fis = new FileInputStream("C:/Users/XENEO/Documents/NetBeansProjects/DropboxActivityPlugin/src/test/resources/testDropboxActivities.xml");

        SyndFeedInput input = new SyndFeedInput();
        SyndFeed sf = input.build(new XmlReader(fis));
        List entries = sf.getEntries();
        Iterator it = entries.iterator();
        while (it.hasNext()) {
            SyndEntry se = (SyndEntry) it.next();
            Activity a = dap.assembleActivity(se);
            assertEquals(a.getCreationDate().toString(), "Mon Apr 30 11:08:35 CEST 2012");
            assertEquals(a.getActivityURI(), "DROPBOX_DFC922733D30EDEB008B59422A446A2D");
            assertEquals(a.getActionURI(), "LEAVE");
            assertEquals(a.getActorURI(), "John Connor");
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
        while (it.hasNext()) {
            SyndEntry se = (SyndEntry) it.next();
            Activity a = dap.assembleActivity(se);
            assertEquals(a.getCreationDate().toString(), "Mon Apr 30 11:07:25 CEST 2012");
            assertEquals(a.getActivityURI(), "DROPBOX_739BC65934B7B7C40747CE76371B0D22");
            assertEquals(a.getActionURI(), "DELETE");
            assertEquals(a.getActorURI(), "John Connor");
            assertEquals(a.getDescription(), "John Connor" + " " + "DELETE" + " " + "drdrdrdrdrdrdrdr" + " " + "XENEOTEST");
            assertEquals(a.getObject().getObjectName(), "drdrdrdrdrdrdrdr");
            assertEquals(a.getObject().getObjectTypeURI(), "Document");
            assertEquals(a.getObject().getObjectURI(), "https://dl-web.dropbox.com/get/XENEOTEST/drdrdrdrdrdrdrdr?w=db418234");
            assertEquals(a.getTarget().getObjectName(), "XENEOTEST");
            assertEquals(a.getTarget().getObjectTypeURI(), "Folder");
            assertEquals(a.getTarget().getObjectURI(), "http://www.dropbox.com/home/XENEOTEST");
            assertEquals(a.getSummary(), null);
            break;
        }

        while (it.hasNext()) {
            SyndEntry se = (SyndEntry) it.next();
            Activity a = dap.assembleActivity(se);
            assertEquals(a.getCreationDate().toString(), "Mon Apr 30 11:07:22 CEST 2012");
            assertEquals(a.getActivityURI(), "DROPBOX_A404B3C0E6ED88F46FB361793AF65D70");
            assertEquals(a.getActionURI(), "DELETE");
            assertEquals(a.getActorURI(), "John Connor");
            assertEquals(a.getDescription(), "John Connor DELETE asdfdfasdfasdf XENEOTEST");
            assertEquals(a.getObject().getObjectName(), "asdfdfasdfasdf");
            assertEquals(a.getObject().getObjectTypeURI(), "Folder");
            assertEquals(a.getObject().getObjectURI(), "http://www.dropbox.com/c/browse/asdfdfasdfasdf?ns_id=119834745");
            assertEquals(a.getTarget().getObjectName(), "XENEOTEST");
            assertEquals(a.getTarget().getObjectTypeURI(), "Folder");
            assertEquals(a.getTarget().getObjectURI(), "http://www.dropbox.com/home/XENEOTEST");
            assertEquals(a.getSummary(), null);
            return;
        }



    }
}