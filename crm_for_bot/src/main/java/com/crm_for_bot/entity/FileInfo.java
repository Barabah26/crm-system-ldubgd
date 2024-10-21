package com.crm_for_bot.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "file_info")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;

    private String fileType;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "statement_id", unique = true)
    private StatementInfo statementInfo;

    @OneToOne(mappedBy = "fileinfo", cascade = CascadeType.ALL, orphanRemoval = true)
    private FileData fileData;

    public FileInfo(Long id) {
        this.id = id;
    }
}

