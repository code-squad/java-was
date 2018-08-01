package db;

import java.util.Collection;
import java.util.Map;

import com.google.common.collect.Maps;

import annotation.Repository;
import model.Board;
import model.User;


@Repository
public class DataBase {
    private Map<String, User> users = Maps.newHashMap();
    private Map<Long, Board> boards = Maps.newHashMap();

    public DataBase() {
	}
    
    public void addUser(User user) {
        users.put(user.getUserId(), user);
    }
    public void addBoard(Board board) {
    	boards.put(board.getId(), board);
    }

    public User findUserById(String userId) {
        return users.get(userId);
    }

    public Collection<User> findAll() {
        return users.values();
    }
    
    public  Board findBoardById(Long id) {
    	return boards.get(id);
    }
    
    public  Collection<Board> findAllBoards() {
    	return boards.values();
    }
	public  void deleteBoard(Long id) {
		boards.remove(id);
	}
	
	
	
}
