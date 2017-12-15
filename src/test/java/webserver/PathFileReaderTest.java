package webserver;

import org.junit.Before;

//파일IO는 테스트를 작성 못하겠음...
public class PathFileReaderTest {
	PathFileReader pathFileReader;
	
	@Before
	public void setRootPath() {
		pathFileReader = new PathFileReader("./webapp");
	}	 
}