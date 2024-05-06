package com.example.financetracker.service;

import com.example.financetracker.component.Cache;
import com.example.financetracker.models.Transaction;
import com.example.financetracker.models.TransactionCategory;
import com.example.financetracker.models.User;
import com.example.financetracker.repos.TransactionCategoryRepository;
import com.example.financetracker.repos.TransactionRepository;
import com.example.financetracker.repos.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TrackerServiceTests {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private TransactionCategoryRepository transactionCategoryRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private Cache cache;

    @InjectMocks
    private TrackerService trackerService;

//    @Test
//    public void testGetSummaryOperations() {
//        when(transactionRepository.findAll()).thenReturn(Collections.emptyList());
//        String summary = trackerService.getSummaryOperations();
//        assertEquals("[]", summary);
//    }

    @Test
    public void testCreateTransaction() {
        Transaction transaction = new Transaction();
        trackerService.createTransaction(transaction);
        verify(transactionRepository, times(1)).save(transaction);
    }

    @Test
    public void testGetTransactionById() {
        Transaction transaction = new Transaction();
        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));
        assertEquals(transaction, trackerService.getTransactionById(1L));
    }

    @Test
    public void testUpdateTransaction() {
        Transaction transaction = new Transaction();
        trackerService.updateTransaction(transaction);
        verify(transactionRepository, times(1)).save(transaction);
    }

    @Test
    public void testDeleteTransaction() {
        trackerService.deleteTransaction(1L);
        verify(transactionRepository, times(1)).deleteById(1L);
        verify(cache, times(1)).removeFromCache("1");
    }

    @Test
    public void testCreateUser() {
        User user = new User();
        trackerService.createUser(user);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testGetUserByUsername() {
        User user = new User();
        when(userRepository.findByUsername("username")).thenReturn(user);
        assertEquals(user, trackerService.getUserByUsername("username"));
    }

    @Test
    public void testGetCategoryByName() {
        TransactionCategory category = new TransactionCategory();
        when(transactionCategoryRepository.findByName("category")).thenReturn(category);
        assertEquals(category, trackerService.getCategoryByName("category"));
    }

    @Test
    public void testCreateCategory() {
        TransactionCategory category = new TransactionCategory();
        trackerService.createCategory(category);
        verify(transactionCategoryRepository, times(1)).save(category);
    }

    @Test
    public void testDeleteUser() {
        trackerService.deleteUser(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testFindTransactionsByUserUsername() {
        List<Transaction> transactions = Collections.singletonList(new Transaction());
        when(transactionRepository.findTransactionsByUserUsername("username")).thenReturn(transactions);
        assertEquals(transactions, trackerService.findTransactionsByUserUsername("username"));
    }

    @Test
    public void testGetTransactionById_NotFound() {
        when(transactionRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> trackerService.getTransactionById(1L));
    }

    @Test
    public void testGetTransactionById_Cached() {
        Transaction transaction = new Transaction();
        when(cache.getFromCache("1")).thenReturn(transaction);

        assertEquals(transaction, trackerService.getTransactionById(1L));
        verify(transactionRepository, never()).findById(1L);
    }

    @Test
    public void testDeleteTransaction_CacheRemoval() {
        trackerService.deleteTransaction(1L);

        verify(transactionRepository, times(1)).deleteById(1L);
        verify(cache, times(1)).removeFromCache("1");
    }

    @Test
    public void testGetTransactionsByUserUsername_Cached() {
        List<Transaction> transactions = Collections.singletonList(new Transaction());
        when(cache.getFromCache("username")).thenReturn(transactions);

        assertEquals(transactions, trackerService.findTransactionsByUserUsername("username"));
        verify(transactionRepository, never()).findTransactionsByUserUsername("username");
    }

    @Test
    public void testGetTransactionsByUserUsername_CacheUpdate() {
        List<Transaction> transactions = Collections.singletonList(new Transaction());
        when(transactionRepository.findTransactionsByUserUsername("username")).thenReturn(transactions);

        assertEquals(transactions, trackerService.findTransactionsByUserUsername("username"));
        verify(cache, times(1)).addToCache(eq("username"), eq(transactions));
    }
}
