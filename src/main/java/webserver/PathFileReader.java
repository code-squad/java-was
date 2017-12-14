package webserver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class PathFileReader {
	String rootPath;
	String indexUrl = "/index.html";
	
	public PathFileReader(String rootPath) {
		this.rootPath = rootPath;
	}
	
	public byte[] getReadAllBytes(String url) throws IOException {
		if("/".equals(url)) {
			return Files.readAllBytes(new File(rootPath + indexUrl).toPath());
		}
		return Files.readAllBytes(new File(rootPath + url).toPath());
	}
}