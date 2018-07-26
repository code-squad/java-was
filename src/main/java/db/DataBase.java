package db;

import java.util.Collection;
import java.util.Map;

import com.google.common.collect.Maps;

import model.Board;
import model.User;

public class DataBase {
    private static Map<String, User> users = Maps.newHashMap();
    private static Map<Long, Board> boards = Maps.newHashMap();

    public static void addUser(User user) {
        users.put(user.getUserId(), user);
    }
    public static void addBoard(Board board) {
    	boards.put(board.getId(), board);
    }

    public static User findUserById(String userId) {
        return users.get(userId);
    }

    public static Collection<User> findAll() {
        return users.values();
    }
    
    public static Board findBoardById(Long id) {
    	return boards.get(id);
    }
    
    public static Collection<Board> findAllBoards() {
    	return boards.values();
    }
	public static void deleteBoard(Long id) {
		boards.remove(id);
	}
}
