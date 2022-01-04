package co.greetings.model.user;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Users {
    List<User> listUsers;
    
    public Users(List<User> listUsers) {
        this.listUsers = listUsers;
    }

    public int getSize() {
        return listUsers.size();
    }

    public <T> List<T> getListUsers(Function<? super User, T> funcMap) {
        return listUsers.stream()
            .map(funcMap)
            .collect(Collectors.toUnmodifiableList());
    }
}
