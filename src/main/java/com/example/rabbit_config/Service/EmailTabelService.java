package com.example.rabbit_config.Service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.rabbit_config.Model.DB_Tables.EmailTable;
import com.example.rabbit_config.Repository.EmailTableRepo;

@Service
public class EmailTabelService {
    private Logger log = LoggerFactory.getLogger(EmailTabelService.class);

    private EmailTableRepo emailTableRepo;

    EmailTabelService(EmailTableRepo emailTableRepo) {
        this.emailTableRepo = emailTableRepo;
    }

    public boolean isMsgIdPresent(String messageId) {
        Optional<EmailTable> emailTabel = emailTableRepo.findByMessageId(messageId);

        return !emailTabel.isEmpty();
    }

    public void save(EmailTable emailTable) {
        try {
            emailTable.setTimeStamp(LocalDateTime.now());
            emailTableRepo.save(emailTable);
        } catch (Exception e) {
            log.error("Error saving Emails to Email Table");
        }
    }

}
