package com.example.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.exception.InvalidMessageException;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

@Service
public class MessageService {

    @Autowired
    MessageRepository message_repo = null;
    @Autowired
    AccountRepository account_repo = null;

    public Message addMessage(Message message) throws InvalidMessageException {

        // get an optional of the owner
        Optional<Account> owner = account_repo.findById(message.getPostedBy());

        // if the optional is empty, the owner is invalid. Throw InvalidMessageException
        if(owner.isEmpty()) throw new InvalidMessageException("Invalid message's owner.");

        // if the message length doesnt meet criteria, throw InvalidMessageException
        if(message.getMessageText().trim().length() == 0 || message.getMessageText().length() > 255) throw new InvalidMessageException("Message length invaid.");

        // Persist the messsage and return it, including the ID.
        return message_repo.save(message);
    }

    public List<Message> getMessages() {
        return message_repo.findAll();
    }

    public Message getMessageById(int id){
        Optional<Message> msg = message_repo.findById(id);

        if(msg.isEmpty()) return null;

        return msg.get();
    }

    public Integer deleteMessageById(int id) {
        // get rows affected
        int rows_affected = message_repo.deleteAndReportRows(id);
        // if rows were affected, return their count
        if(rows_affected != 0) return rows_affected;
        // else return null (since its an Integer and not an int)
        return null;
    }

    public Integer editMessage(int id, String messageText) throws InvalidMessageException{
        // get optional
        Optional<Message> msgOptional = message_repo.findById(id);

        // if the message doesnt exist or if the new text length is invalid, throw InvalidMessageException
        if(msgOptional.isEmpty()) throw new InvalidMessageException("Message id is invalid");
        if(messageText.trim().length() == 0 || messageText.length() > 255) throw new InvalidMessageException("Message lenght is invalid.");

        // get message from optional
        Message msg = msgOptional.get();

        // edit text
        msg.setMessageText(messageText);

        // get affected rows
        int affected_rows = message_repo.editAndReportRows(id, messageText);

        // if no affected rows, then return null, which converts to an empty HTTP response body
        if(affected_rows == 0) return null;

        return affected_rows;
    }

    public List<Message> getAllMessagesById(int id) {
        return message_repo.findByPostedBy(id);
    }
}
