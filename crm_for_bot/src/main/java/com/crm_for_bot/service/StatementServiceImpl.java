package com.crm_for_bot.service;

import com.crm_for_bot.dto.StatementDto;
import com.crm_for_bot.entity.StatementInfo;
import com.crm_for_bot.exception.RecourseNotFoundException;
import com.crm_for_bot.repository.StatementRepository;
import com.crm_for_bot.util.StatementStatus;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of the StatementService interface.
 */
@Service
@AllArgsConstructor
@Slf4j
public class StatementServiceImpl implements StatementService {

    private final StatementRepository statementRepository;

    @Override
    public List<StatementDto> getStatementsInfoWithStatusPending() {
        List<Object[]> results = statementRepository.findStatementsInfoWithStatusPending();
        if (results.isEmpty()) {
            log.warn("No statements found with status PENDING");
            return Collections.emptyList();
        }
        return results.stream().map(this::mapToStatementDto).collect(Collectors.toList());
    }


    private StatementDto mapToStatementDto(Object[] result) {
        StatementDto dto = new StatementDto();
        dto.setId((Long) result[0]);
        dto.setFullName((String) result[1]);
        dto.setGroupName((String) result[2]);
        dto.setPhoneNumber((String) result[3]);
        dto.setTypeOfStatement((String) result[4]);
        dto.setFaculty((String) result[5]);
        dto.setYearBirthday((String) result[6]);

        Object statusObject = result[7];
        String statusString;

        if (statusObject instanceof Boolean) {
            statusString = (Boolean) statusObject ? "READY" : "PENDING";
        } else {
            statusString = (String) statusObject;
        }

        StatementStatus status = StatementStatus.valueOf(statusString);
        switch (status) {
            case IN_PROGRESS:
                dto.setStatus("В процесі");
                break;
            case READY:
                dto.setStatus("Готово");
                break;
            case PENDING:
                dto.setStatus("В очікуванні");
                break;
            default:
                dto.setStatus("Невідомий статус");
                break;
        }

        return dto;
    }



    @Override
    public void updateStatementStatus(Long statementId, StatementStatus status) {
        StatementInfo statement = statementRepository.findById(statementId).orElseThrow(
                () -> new RecourseNotFoundException("Statement is not found with id: " + statementId)
        );
        statement.setStatementStatus(StatementStatus.valueOf(status.name()));

        statementRepository.save(statement);
    }

    @Override
    public List<StatementDto> getStatementsInfoByStatusAndFaculty(StatementStatus status, String faculty) {
        List<Object[]> results = statementRepository.findStatementInfoByStatusAndFaculty(status.name(), faculty);
        return results.stream().map(this::mapToStatementDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteStatementIfReady(Long statementId, StatementStatus status, String faculty) {
        List<Object[]> results = statementRepository.findStatementInfoByStatusAndFaculty(status.name(), faculty);
        if (!results.isEmpty()){
            statementRepository.deleteStatementIfReady(status.name(), statementId, faculty);
        } else {
            throw new RecourseNotFoundException("Statements are not found");
        }
    }

    @Override
    public List<StatementDto> searchByName(String name) {
        return statementRepository.findByNameContaining(name).stream().map(this::mapToStatementDto).collect(Collectors.toList());
    }

}
