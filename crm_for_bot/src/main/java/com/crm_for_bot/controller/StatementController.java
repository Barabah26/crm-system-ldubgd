package com.crm_for_bot.controller;

import com.crm_for_bot.dto.StatementDto;
import com.crm_for_bot.exception.RecourseNotFoundException;
import com.crm_for_bot.service.StatementService;
import com.crm_for_bot.util.StatementStatus;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller for handling endpoints related to statements.
 * Provides methods for retrieving all statements, retrieving statements by faculty, and updating statement status.
 */
@AllArgsConstructor
@RestController
@RequestMapping("/api/statements")
@Slf4j
public class StatementController {

    /**
     * Service for handling statement operations.
     */
    private final StatementService statementService;

    /**
     * Retrieves all statements with a status of false.
     *
     * @return ResponseEntity<List<StatementDto>> - the response containing a list of statements with status false, or no content if none found.
     */
    @GetMapping
    public ResponseEntity<List<StatementDto>> getAllStatements() {
        log.info("Fetching all statements with status false");
        List<StatementDto> statements = statementService.getStatementsInfoWithStatusPending();
        if (statements.isEmpty()) {
            log.warn("No statements found with status false");
            return ResponseEntity.noContent().build();
        }
        log.info("Retrieved {} statements", statements.size());
        return ResponseEntity.ok(statements);
    }


    /**
     * Marks a statement as "IN_PROGRESS" based on its ID.
     *
     * @param statementId the ID of the statement to update.
     * @return ResponseEntity<String> - the response indicating the result of the update operation.
     */
    @PutMapping("{id}/in-progress") //
    public ResponseEntity<String> markStatementInProgress(@PathVariable("id") Long statementId) {
        log.info("Marking statement ID: {} as IN_PROGRESS", statementId);
        try {
            statementService.updateStatementStatus(statementId, StatementStatus.IN_PROGRESS);
        } catch (RecourseNotFoundException e) {
            log.error("Statement with ID {} not found", statementId, e);
            return ResponseEntity.status(404).body("Statement not found!");
        } catch (Exception e) {
            log.error("Failed to mark statement as IN_PROGRESS for ID: {}", statementId, e);
            return ResponseEntity.status(500).body("Failed to update statement status!");
        }
        log.info("Statement ID: {} marked as IN_PROGRESS successfully", statementId);
        return ResponseEntity.ok("Statement marked as IN_PROGRESS successfully!");
    }

    /**
     * Marks a statement as "READY" based on its ID.
     *
     * @param statementId the ID of the statement to update.
     * @return ResponseEntity<String> - the response indicating the result of the update operation.
     */
    @PutMapping("{id}/ready")
    public ResponseEntity<String> markStatementReady(@PathVariable("id") Long statementId) { //
        log.info("Marking statement ID: {} as READY", statementId);
        try {
            statementService.updateStatementStatus(statementId, StatementStatus.READY);
        } catch (RecourseNotFoundException e) {
            log.error("Statement with ID {} not found", statementId, e);
            return ResponseEntity.status(404).body("Statement not found!");
        } catch (Exception e) {
            log.error("Failed to mark statement as READY for ID: {}", statementId, e);
            return ResponseEntity.status(500).body("Failed to update statement status!");
        }
        log.info("Statement ID: {} marked as READY successfully", statementId);
        return ResponseEntity.ok("Statement marked as READY successfully!");
    }

    @GetMapping("/statusAndFaculty") //
    public ResponseEntity<List<StatementDto>> getStatementsByStatusAndFaculty(
            @RequestParam(value = "status", required = false) StatementStatus status,
            @RequestParam(value = "faculty", required = false) String faculty) {

        log.info("Received request to get statements with status: {} and faculty: {}", status, faculty);

        List<StatementDto> statements = new ArrayList<>();

        if (status != null) {
            log.info("Fetching statements for status: {} and faculty: {}", status, faculty);
            statements = statementService.getStatementsInfoByStatusAndFaculty(status, faculty);
        }

        if (statements.isEmpty()) {
            log.warn("No statements found for status: {} and faculty: {}", status, faculty);
            return ResponseEntity.ok(new ArrayList<>());
        }

        log.info("Successfully retrieved {} statements.", statements.size());
        return ResponseEntity.ok(statements);
    }

    @DeleteMapping("/ready") //
    public ResponseEntity<Void> completeStatementIfReady(
            @RequestParam Long statementId,
            @RequestParam(value = "status", required = false) StatementStatus status,
            @RequestParam(value = "faculty", required = false) String faculty) {

        log.info("Received request to delete statements with status: {} and faculty: {}", status, faculty);

        if (status == null || faculty == null || !status.equals(StatementStatus.READY)) {
            log.warn("Bad request: status or faculty is null or status is not READY");
            return ResponseEntity.badRequest().build();
        }

        try {
            log.info("Attempting to delete statements with status: {} and faculty: {}", status, faculty);
            statementService.deleteStatementIfReady(statementId, status, faculty);
            log.info("Successfully deleted statements with status: {} and faculty: {}", status, faculty);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error occurred while deleting statements with status: {} and faculty: {}. Error: {}", status, faculty, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/searchByName") //
    public ResponseEntity<?> searchUsersByName(@RequestParam String name) {
        try {
            List<StatementDto> users = statementService.searchByName(name);
            if (!users.isEmpty()) {
                return ResponseEntity.ok(users);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No users found");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while searching for users");
        }
    }





}
