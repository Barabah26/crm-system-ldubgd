package com.crm_for_bot.service;

import com.crm_for_bot.entity.StatementInfo;
import com.crm_for_bot.repository.FileDataRepository;
import com.crm_for_bot.repository.FileInfoRepository;
import com.crm_for_bot.repository.StatementRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@AllArgsConstructor
public class FileServiceImpl implements FileService {

    private final FileDataRepository fileDataRepository;
    private final FileInfoRepository fileInfoRepository;
    private final StatementRepository statementInfoRepository;

    @Override
    public void saveFile(MultipartFile file, Long statementId) {
        StatementInfo statement = statementInfoRepository.findById(statementId)
                .orElseThrow(() -> new RuntimeException("Statement not found with id: " + statementId));

        Long savedFileInfoId = fileInfoRepository.saveOrUpdateFileInfo(
                file.getOriginalFilename(),
                file.getContentType(),
                statementId
        );

        try {
            byte[] fileDataBytes = file.getBytes();
            fileDataRepository.saveFileData(fileDataBytes, savedFileInfoId);
        } catch (IOException e) {
            throw new RuntimeException("Error while processing file data", e);
        }
    }
}

