import com.alexecollins.maven.plugins.vbox.schema.VirtualBox;
import org.junit.Test;

import javax.xml.bind.JAXB;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
public class XsdTest {
	@Test
	public void test() throws Exception {

		JAXB.unmarshal(getClass().getResource("/UbuntuServer/VirtualBox.xml"), VirtualBox.class);
	}
}
