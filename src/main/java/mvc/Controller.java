package mvc;

import java.io.BufferedReader;
import java.io.IOException;

import db.DataBase;
import webserver.HttpRequest;
import webserver.HttpResponse;

public interface Controller {
	void execute(HttpRequest request, HttpResponse response) throws IOException ;

	void inputParameter(HttpRequest request, BufferedReader br) throws IOException;
	
	DataBase syncDataBase();
}
