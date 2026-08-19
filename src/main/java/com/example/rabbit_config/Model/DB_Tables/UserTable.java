package com.example.rabbit_config.Model.DB_Tables;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Data;

@Data
@Entity
@Table(name = "userdata_table", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email")
})
public class UserTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String email;

    private int receivedEmails;

    private int sent;

    private int failure;

    private int noReply;

}
