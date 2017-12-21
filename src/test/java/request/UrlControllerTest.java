package request;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import controller.Controller;
import controller.UrlController;
import controller.UserCreateController;

public class UrlControllerTest {
	
	@Test
	public void testName() throws Exception {
		UrlController ctrl = new UrlController();
		ctrl.addController("/user/create", new UserCreateController());
		Controller controller = ctrl.resolveController("/user/create");
		assertEquals(UserCreateController.class, controller.getClass());
	}
}
