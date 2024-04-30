package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.exception.DuplicateUsernameException;
import com.example.exception.InvalidAccountCredentialsException;
import com.example.exception.RegistrationException;
import com.example.repository.AccountRepository;

@Service
public class AccountService {

    @Autowired
    AccountRepository account_repo = null;

    // attempt to register the account
    public Account save(Account account) throws DuplicateUsernameException, RegistrationException{

        // get account by username, save it inside the "exists" var
        Account exists = account_repo.findByUsername(account.getUsername());

        // if "exists" is not null (meaning the username has been taken), throw DuplicateUsernameException
        if(exists != null) 
            throw new DuplicateUsernameException("Username already taken.");

        // if username/password do not meet criteria, throw RegistrationException
        if(account.getUsername().length() == 0 || account.getPassword().length() < 4)
            throw new RegistrationException("Username and/or password do not meet the criteria.");

        // persist the data and return the Account with its ID back
        return account_repo.save(account);
    }

    // attempt to login an account (check if username and password are correct)
    public Account login(Account account) throws InvalidAccountCredentialsException {

        // get account by username, save it inside the "exists" var
        Account exists = account_repo.findByUsername(account.getUsername());

        // if the account username doesnt exist or the provided password does not match the password of the given username, throw InvalidAccountCredentialsException
        if(exists == null || exists.getPassword().compareTo(account.getPassword()) != 0)
            throw new InvalidAccountCredentialsException("Unautorized access.");

        // return the validated account, including its ID
        return exists;
    }
}
