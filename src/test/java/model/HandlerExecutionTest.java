package model;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import controller.CreateUserController;
import dto.UserDto;

public class HandlerExecutionTest {

	private HandlerExecution handlerExecution;
	private Method[] methods;
	
	@Before
	public void setUp() throws Exception{
		methods = CreateUserController.class.getMethods();
		handlerExecution = new HandlerExecution(methods[0], CreateUserController.class.newInstance());
	}
	
	@Test
	public void pullClassRequest() throws Exception {
		System.out.println(handlerExecution.pullClassRequest()+"앗");
		assertThat(	handlerExecution.pullClassRequest(), is("/create"));
	}
	@Test
	public void pullMethodRequest() throws Exception {
		assertThat(	handlerExecution.pullMethodRequest(), is("/create"));
	}

	@Test
	public void execute() throws Exception{
		List<Object> params = new ArrayList<>();
		params.add(new UserDto("pobi", "1234", "포비"));
		assertThat(handlerExecution.execute(params), is("redirect:/index.html"));
	}
	
}
