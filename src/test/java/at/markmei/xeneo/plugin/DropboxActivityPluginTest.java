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
import org.junit.*;
import static org.junit.Assert.*;
import org.xeneo.core.plugin.PluginConfiguration;
import org.xeneo.core.activity.Activity;

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
            
            assertTrue(true);
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
            assertEquals(a.getCreationDate(),"Mon Apr 30 11:08:35 CEST 2012");
            assertEquals(a.getActivityURI(),"DROPBOX_DFC922733D30EDEB008B59422A446A2D");
            assertEquals(a.getActionURI(),"LEAVE");
            assertEquals(a.getActorURI(),"John Connor");
            assertEquals(a.getDescription(),"John Connor LEAVE XENEOTEST");
            assertEquals(a.getObject(),"XENEOTEST");
            assertEquals(a.getTarget(),"");
            assertEquals(a.getSummary(),"");
            return;
        }
        
        
        
        
        
        
        
    }
}