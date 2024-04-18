package com.example.financetracker.controller;

import com.example.financetracker.models.Transaction;
import com.example.financetracker.models.User;
import com.example.financetracker.repos.UserRepository;
import com.example.financetracker.service.TrackerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class TrackerControllerTest {

    @InjectMocks
    private TrackerController controller;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TrackerService trackerService;

    @InjectMocks
    private TrackerController trackerController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testHello() {
        assertEquals("Hello world", trackerController.hello());
    }

    @Test
    void testGetSummaryOperations() {
        when(trackerService.getSummaryOperations()).thenReturn("Summary");
        assertEquals("Summary", trackerController.getSummaryOperations());
    }

    @Test
    void testCreateTransaction() {
        User user = new User();
        user.setUsername("testUser");

        when(trackerService.getUserByUsername("testUser")).thenReturn(user);

        ResponseEntity<Void> response = trackerController.createTransaction("Test description", 100.0, "testUser", "Test category");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(trackerService, times(1)).createTransaction(any());
    }

    @Test
    public void testCreateTransaction_UserNotFound() {
        // Mocking the behavior of the service method
        when(trackerService.getUserByUsername(anyString())).thenReturn(null);

        // Asserting that the controller method throws EntityNotFoundException
        assertThrows(EntityNotFoundException.class, () -> {
            controller.createTransaction("Test description", 100.0, "nonExistingUser", "TestCategory");
        });

        // Verifying that the service method was called with the expected parameters
        verify(trackerService).getUserByUsername("nonExistingUser");
    }

    @Test
    void testGetTransactionById() {
        Long id = 1L;
        Transaction transaction = new Transaction();
        when(trackerService.getTransactionById(id)).thenReturn(transaction);

        assertEquals(transaction, trackerController.getTransactionById(id));
    }

    @Test
    void testGetTransactionsByUser() {
        String username = "testUser";
        List<Transaction> transactions = Collections.singletonList(new Transaction());
        when(trackerService.findTransactionsByUserUsername(username)).thenReturn(transactions);

        assertEquals(transactions, trackerController.getTransactionsByUser(username));
    }

    @Test
    void testUpdateTransaction() {
        Long id = 1L;
        Transaction existingTransaction = new Transaction();
        when(trackerService.getTransactionById(id)).thenReturn(existingTransaction);

        Transaction updatedTransaction = new Transaction();
        updatedTransaction.setDescription("Updated description");

        trackerController.updateTransaction(id, updatedTransaction);

        assertEquals("Updated description", existingTransaction.getDescription());
        verify(trackerService, times(1)).updateTransaction(existingTransaction);
    }

    @Test
    void testUpdateTransaction_NotFound() {
        Long id = 1L;
        when(trackerService.getTransactionById(id)).thenReturn(null);

        Transaction updatedTransaction = new Transaction();
        updatedTransaction.setDescription("Updated description");

        try {
            trackerController.updateTransaction(id, updatedTransaction);
        } catch (EntityNotFoundException e) {
            assertEquals("Transaction with ID 1 not found", e.getMessage());
        }
    }

    @Test
    void testDeleteTransaction() {
        Long id = 1L;
        trackerController.deleteTransaction(id);
        verify(trackerService, times(1)).deleteTransaction(id);
    }

    @Test
    void testCreateUser() {
        ResponseEntity<Void> response = trackerController.createUser("testUser", "test@example.com");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(trackerService, times(1)).createUser(any());
    }

    @Test
    void testDeleteUser() {
        Long id = 1L;
        trackerController.deleteUser(id);
        verify(trackerService, times(1)).deleteUser(id);
    }

    @Test
    public void testCreateBulkTransactions() throws Exception {
        List<Transaction> transactions = new ArrayList<>();
        Transaction transaction1 = new Transaction();
        transaction1.setDescription("Transaction 1");
        transaction1.setAmount(100.00);
        transactions.add(transaction1);

        doNothing().when(trackerService).createBulkTransactions(anyList());

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        mockMvc.perform(MockMvcRequestBuilders.post("/transactions/bulk")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(transactions)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(""));
    }

    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testCreateBulkTransactions_EmptyList() throws Exception {
        List<Transaction> emptyList = Collections.emptyList();

        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        mockMvc.perform(MockMvcRequestBuilders.post("/transactions/bulk")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(emptyList)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(""));
    }
}

