package com.crm_for_bot.service;

import com.crm_for_bot.dto.StatementDto;
import com.crm_for_bot.util.StatementStatus;

import java.util.List;

/**
 * Service interface for managing statements.
 */
public interface StatementService {

    /**
     * Retrieves statements with a status of false.
     *
     * @return a list of StatementDto objects
     */
    List<StatementDto> getStatementsInfoWithStatusPending();

    /**
     * Updates the status of a statement to true.
     *
     * @param statementId the ID of the statement to update
     */
    void updateStatementStatus(Long statementId, StatementStatus status);

    List<StatementDto> getStatementsInfoByStatusAndFaculty(StatementStatus status, String faculty);

    void deleteStatementIfReady(Long statementId, StatementStatus status, String faculty);

    List<StatementDto> searchByName(String name);

}
