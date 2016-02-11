package at.chrl.nutils;

import at.chrl.nutils.configuration.ConfigUtil;
import at.chrl.nutils.configuration.listener.ConfigEventListener;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * JUnit Test for Configuration Listener
 *
 * Created by vinzynth on 21.12.15.
 */
@RunWith(MockitoJUnitRunner.class)
public class ConfigListenerTest {

    @Test
    public void testClassListeners() throws Exception {
        ConfigEventListener cel = Mockito.mock(ConfigEventListener.class);

        ConfigUtil.getInstance().addConfigEventListener(cel);

        ConfigUtil.getInstance().load(TestConfig.class);

        Mockito.verify(cel).onLoadedConfigClass(TestConfig.class);
        Mockito.verify(cel, Mockito.never()).onLoadedConfigObject(new TestConfig());
    }

    @Test
    public void testObjectListeners() throws Exception {
        ConfigEventListener cel = Mockito.mock(ConfigEventListener.class);

        ConfigUtil.getInstance().addConfigEventListener(cel);

        TestConfig tc = new TestConfig();
        ConfigUtil.getInstance().load(tc);

        Mockito.verify(cel, Mockito.never()).onLoadedConfigClass(TestConfig.class);
        Mockito.verify(cel).onLoadedConfigObject(tc);
    }

}
