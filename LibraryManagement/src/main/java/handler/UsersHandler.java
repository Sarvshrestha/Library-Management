package handler;

import models.Book;
import models.User;

import java.util.*;

public class UsersHandler {
    // Use Singleton
    private HashMap<UUID, User> userHashMap = new HashMap<>();

    public void addUser(User user){
        userHashMap.put(user.getId(), user);
    }

    public User getUser(UUID user_id){
        return userHashMap.getOrDefault(user_id, null);
    }

    public void removeUser(UUID user_id){
        userHashMap.remove(user_id);
    }

    // Waste
    public List<User> getUsersSortedOnFine(){
        List<User> userList = new ArrayList<>();
        for (UUID key: userHashMap.keySet()){
            userList.add(userHashMap.get(key));
        }
        for (int i = 0 ; i < userList.size() - 1; i++) {
            User u = userList.get(i);
            User next =  userList.get(i+1);
            if(u.getFine() < next.getFine()) {
                Collections.swap(userList, i, i+1);
            }
        }
        return userList;
    }

    public boolean eligibleForBookIssue(UUID user_id, int MaxSize){
        if (checkForFine(user_id) > 0){
            return false;
        }
        if (booksIssuedCurrently(user_id) >= MaxSize){
            return false;
        }
        return true;
    }

    private int checkForFine(UUID user_id){
        User user = getUser(user_id);
        return user.getFine();
    }

    public int booksIssuedCurrently(UUID user_id){
        User user = getUser(user_id);
        return user.getBooks().size();
    }

    public boolean isUserIssuedThisBook(UUID user_id, UUID book_id){
        User user = getUser(user_id);
        List<Book> bookList = user.getBooks();
        if (bookList.size() == 0){
            return false;
        }

        for (Book book: bookList){
            if (book_id == book.getId()){
                return true;
            }
        }
        return false;
    }
}
