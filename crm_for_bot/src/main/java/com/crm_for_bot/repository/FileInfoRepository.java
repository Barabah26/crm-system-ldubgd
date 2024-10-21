package com.crm_for_bot.repository;

import com.crm_for_bot.entity.FileInfo;
import com.crm_for_bot.entity.StatementInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FileInfoRepository extends JpaRepository<FileInfo, Long> {

    @Query(value = "INSERT INTO file_info (file_name, file_type, statement_id) " +
            "VALUES (:fileName, :fileType, :statementId) " +
            "ON CONFLICT (statement_id) DO UPDATE SET " +
            "file_name = EXCLUDED.file_name, file_type = EXCLUDED.file_type " +
            "RETURNING id", nativeQuery = true)
    Long saveOrUpdateFileInfo(@Param("fileName") String fileName,
                              @Param("fileType") String fileType,
                              @Param("statementId") Long statementId);
}
