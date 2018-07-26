package annotation;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Method;

import org.junit.Test;

import controller.CreateUserController;
import controller.ListUserController;
import controller.LoginController;
import model.HandlerExecution;
import model.HttpRequest;
import model.HttpResponse;

public class AnnotationHandlerTest {
	private String testDirectory = "./src/test/java/";

	@Test
	public void parameterHandle() throws Exception {
        InputStream inputSteram = new FileInputStream(new File( testDirectory + "model/Http_GET.txt"));
    	HttpRequest request = HttpRequest.of(inputSteram);
        HttpResponse response = new HttpResponse(new FileOutputStream(new File(testDirectory + "Http_Forward.txt")));


		AnnotationHandler handler = AnnotationHandler.of(request, response);
		Method[] methods = CreateUserController.class.getMethods();
		
		assertThat(handler.parameterHandle(new HandlerExecution(methods[0], CreateUserController.class.newInstance())), is("redirect:/index.html"));
	}

	@Test
	public void parameterHandleNoRedirect() throws Exception {
		InputStream inputSteram = new FileInputStream(new File( testDirectory + "model/Http_GET.txt"));
		HttpRequest request = HttpRequest.of(inputSteram);
		HttpResponse response = new HttpResponse(new FileOutputStream(new File(testDirectory + "Http_Forward.txt")));
		
		
		AnnotationHandler handler = AnnotationHandler.of(request, response);
		Method[] methods = ListUserController.class.getMethods();
		
		assertThat(handler.parameterHandle(new HandlerExecution(methods[0], ListUserController.class.newInstance()))==null, is(true));
	}

	@Test
	public void parameterHandleRedirect2() throws Exception {
		InputStream inputSteram = new FileInputStream(new File( testDirectory + "model/Http_GET.txt"));
		HttpRequest request = HttpRequest.of(inputSteram);
		HttpResponse response = new HttpResponse(new FileOutputStream(new File(testDirectory + "Http_Forward.txt")));
		
		
		AnnotationHandler handler = AnnotationHandler.of(request, response);
		Method[] methods = LoginController.class.getMethods();
		
		assertThat(handler.parameterHandle(new HandlerExecution(methods[0], LoginController.class.newInstance())), is("redirect:/user/login_failed.html"));
	}
	
}
