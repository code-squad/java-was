package Controller;

import java.util.function.Supplier;
import java.util.stream.Stream;

public class enumTest {
	public static void main(String[] args) {
		
		String test = "/user/create";
		Controller controller = Stream.of(RqstUriTest.values()).filter(s -> s.getUri().equals(test)).findFirst().get().createController();
	}
}

enum RqstUriTest {
	userCreate("/user/create", () -> new CreateUserController()), 
	userLogin("/user/login", () -> new ListUserController()), 
	userList("/user/list", () -> new LoginController());

	final private String uri;
	final private Supplier<Controller> expression;

	private RqstUriTest(String uri, Supplier<Controller> expression) {
		this.uri = uri;
		this.expression = expression;
	}

	public Controller createController() {
		return expression.get();
	}
	
	public String getUri() {
		return uri;
	}
}
