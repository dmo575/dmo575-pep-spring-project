package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.exception.DuplicateUsernameException;
import com.example.exception.InvalidAccountCredentialsException;
import com.example.exception.InvalidMessageException;
import com.example.exception.RegistrationException;
import com.example.service.AccountService;
import com.example.service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
public class SocialMediaController {
    @Autowired
    private AccountService account_service =  null;
    @Autowired
    private MessageService message_service =  null;


    // CREATE ENDPOINTS HERE
    @PostMapping("/register")
    public Account register(@RequestBody Account account){
        try {
            return account_service.save(account);
        }
        catch(DuplicateUsernameException ex) {
            throw ex;
        }
        catch(RegistrationException ex) {
            throw ex;
        }
    }

    @PostMapping("/login")
    public Account login(@RequestBody Account account) {
        try {
            return account_service.login(account);
        }
        catch(InvalidAccountCredentialsException ex) {
            throw ex;
        }
    }

    @PostMapping("/messages")
    public Message message(@RequestBody Message message) {
        try {
            return message_service.addMessage(message);
        }
        catch(InvalidMessageException ex) {
            throw ex;
        }
    }

    @GetMapping("/messages")
    public List<Message> getMessages() {
        return message_service.getMessages();
    }

    @GetMapping("/messages/{messageId}")
    public Message getMessages(@PathVariable int messageId) {
        return message_service.getMessageById(messageId);
    }

    @DeleteMapping("/messages/{messageId}")
    public Integer deleteMessage(@PathVariable int messageId) {
        // Since we are getting an Integer instead of an int, we trust Spring here to properly pass the Integer as a number in the response body in case
        // rows were affected, and also to properly translated null to nothing in the response body in case rows were not affected.
        return message_service.deleteMessageById(messageId);
    }

    @PatchMapping("/messages/{messageId}")
    public Integer editMessage(@RequestBody Message messageText, @PathVariable int messageId) {
        try{
            return message_service.editMessage(messageId, messageText.getMessageText());
        }
        catch(InvalidMessageException ex) {
            throw ex;
        }
    }

    @GetMapping("/accounts/{accountId}/messages")
    public List<Message> getAllMessagesById(@PathVariable int accountId) {
        return message_service.getAllMessagesById(accountId);
    }


    // EXCEPTION HANDLERS:

    // duplicate username exception
    @ExceptionHandler(DuplicateUsernameException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleDuplicateUsername(DuplicateUsernameException ex) {
        return ex.getMessage();
    }

    // generic registration exception (client side)
    @ExceptionHandler(RegistrationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleRegistrationException(DuplicateUsernameException ex) {
        return ex.getMessage();
    }

    // invalid account credentials exception
    @ExceptionHandler(InvalidAccountCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String handleInvalidAccountCredentialsException(InvalidAccountCredentialsException ex) {
        return ex.getMessage();
    }

    // invalid message
    @ExceptionHandler(InvalidMessageException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleInvalidMessageException(InvalidMessageException ex) {
        return ex.getMessage();
    }
}
