package com.crm_for_bot.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "file_data")
@Data
public class FileData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private byte[] data;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "file_info_id", unique = true)
    private FileInfo fileinfo;
}
