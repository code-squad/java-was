package controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import db.DataBase;
import model.RequestPath;
import model.User;
import util.SplitUtils;

public class PathController {
	String rootDir = "./";
	UserController userController = new UserController();

	private static final Logger log = LoggerFactory.getLogger(PathController.class);

	public void setRootDir(String rootDir) {
		this.rootDir = rootDir;
	}

	public String changeFinalUrl(RequestPath path) {
		String url = path.toString();
		if (isWebFile(url)) {
			return url;
		}
		return runController(path);
	}

	private String runController(RequestPath path) {
		if(path.getOnlyUrl().equals("/")) {
			return "/";
		}
		String firstTurningPoint = SplitUtils.getSplitedValue(path.getOnlyUrl(), "/", 1);
		if ("user".equals(firstTurningPoint)) {
			log.debug("user turning point 진입");
			return runUserController(path);
		}
		return "/";
	}

	private String runUserController(RequestPath path) {
		String secondTurningPoint = SplitUtils.getSplitedValue(path.getOnlyUrl(), "/", 2);
		if ("create".equals(secondTurningPoint)) {
			log.debug("create page 진입");
			User joinUser = userController.saveUser(path.getInputValue());
			DataBase.addUser(joinUser);
			return "/index.html";
		}
		return "/";
	}

	private boolean isWebFile(String url) {
		String extension = SplitUtils.getSplitedExtension(url).toUpperCase();
		return "HTML".equals(extension) || "JS".equals(extension) || "CSS".equals(extension);

	}
}