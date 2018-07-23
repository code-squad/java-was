package annotation;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Parameter;

import org.junit.Before;
import org.junit.Test;

import controller.CreateUserController;
import dto.UserDto;
import model.HttpRequest;
import util.ReflectionUtils;

public class ReflectionUtilsTest {
	private String testDirectory = "./src/test/java/";

	private Parameter[] parameters;
	private ReflectionUtils reflectionUtils;
	
	@Before
	public void setUp() throws Exception {
		parameters = CreateUserController.class.getMethod("create", UserDto.class).getParameters();
		reflectionUtils = new ReflectionUtils(parameters);
	}
	
	@Test
	public void checkDefaultConstructer() throws Exception {
		assertThat(reflectionUtils.checkDefaultConstructer(UserDto.class), is(true));
	}

	@Test
	public void checkSetter() throws Exception {
		assertThat(reflectionUtils.checkSetter(UserDto.class.getMethods()), is(true));
	}

	@Test
	public void createBeanObject() throws Exception {
		InputStream inputSteram = new FileInputStream(new File(testDirectory + "model/Http_POST.txt"));
		HttpRequest request = HttpRequest.of(inputSteram);

		assertThat(reflectionUtils.createBeanObject(UserDto.class, request), is(new UserDto("lsc109", "1234", "gram")));
	}
	
	@Test
	public void obtainParameterName() {
		assertThat(reflectionUtils.obtainParameterName("setUserId"), is("userId"));
		assertThat(reflectionUtils.obtainParameterName("setPassword"), is("password"));
	}
	
}
