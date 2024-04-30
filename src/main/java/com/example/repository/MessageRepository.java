package com.example.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.entity.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
    public Message save(Message message);

    // custom delete that returns number of records affected by the operation
    @Modifying
    @Transactional
    @Query("DELETE FROM Message WHERE messageId=?1")
    public int deleteAndReportRows(Integer id);

    @Modifying
    @Transactional
    @Query("UPDATE Message SET messageText=?2 WHERE messageId=?1")
    public int editAndReportRows(Integer id, String messageText);

    public List<Message> findByPostedBy(int postedById);
}
