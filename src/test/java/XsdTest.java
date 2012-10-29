import com.alexecollins.maven.plugins.vbox.schema.VirtualBox;
import org.junit.Test;

import javax.xml.bind.JAXB;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
public class XsdTest {
	@Test
	public void test() throws Exception {

		final VirtualBox cfg = JAXB.unmarshal(VirtualBox.class.getResource("/UbuntuServer_12_10/VirtualBox.xml"), VirtualBox.class);

		assert cfg.getMachine() != null;
	}
}
