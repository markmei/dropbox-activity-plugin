/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package at.markmei.xeneo.plugin;

import java.util.List;
import java.util.Properties;
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
        ps.setProperty("url", "https://www.dropbox.com/13081712/20374930/96070ae/events.xml");
        ps.setProperty("folder", "xeneo");
        
        pc.setProperties(ps);
        
        dap.setPluginConfiguration(pc);
        dap.init();
        
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testGetActivities() {
    
        List<Activity> acts = dap.getActivities("");
        
        assertTrue(true);
        
    }
}
