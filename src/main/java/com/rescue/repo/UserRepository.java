package com.rescue.repo;

import com.rescue.model.DataStore;
import com.rescue.model.User;

public class UserRepository extends LookupTable<User> {
    public UserRepository() {
        super(DataStore.getInstance().getUsers());
    }
}