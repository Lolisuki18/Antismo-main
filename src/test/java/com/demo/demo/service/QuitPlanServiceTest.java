package com.demo.demo.service;

import com.demo.demo.entity.QuitPlan.QuitPlan;
import com.demo.demo.repository.QuitPlan.QuitPlanRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class QuitPlanServiceTest {

    @Mock
    private QuitPlanRepository repository;

    @Mock
    private UserService userService;

    @InjectMocks
    private QuitPlanService quitPlanService;

    @Test
    void testGetQuitPlansByUserId_Success() {
        // Arrange
        Integer userId = 1;
        Object userInfo = new Object() {
            public String getName() {
                return "John Doe";
            }

            public String getEmail() {
                return "john@example.com";
            }
        };
        List<QuitPlan> userPlans = Arrays.asList(
                createTestQuitPlan(1, userId, 101, "Plan 1"),
                createTestQuitPlan(2, userId, 101, "Plan 2"));

        when(userService.getUserNameAndMailById(userId)).thenReturn(userInfo);
        when(repository.findByUserId(userId)).thenReturn(userPlans);

        // Act
        List<Object> result = quitPlanService.getQuitPlansByUserId(userId);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(userInfo, result.get(0));
        assertEquals(userPlans, result.get(1));
        verify(userService, times(1)).getUserNameAndMailById(userId);
        verify(repository, times(1)).findByUserId(userId);
    }

    @Test
    void testGetQuitPlansByUserId_NoPlans() {
        // Arrange
        Integer userId = 999;
        Object userInfo = new Object() {
            public String getName() {
                return "Jane Doe";
            }

            public String getEmail() {
                return "jane@example.com";
            }
        };
        List<QuitPlan> emptyPlans = Arrays.asList();

        when(userService.getUserNameAndMailById(userId)).thenReturn(userInfo);
        when(repository.findByUserId(userId)).thenReturn(emptyPlans);

        // Act
        List<Object> result = quitPlanService.getQuitPlansByUserId(userId);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(userInfo, result.get(0));
        assertTrue(((List<?>) result.get(1)).isEmpty());
        verify(userService, times(1)).getUserNameAndMailById(userId);
        verify(repository, times(1)).findByUserId(userId);
    }

    @Test
    void testGetQuitPlansByCoachId_Success() {
        // Arrange
        Integer coachId = 101;
        List<QuitPlan> coachPlans = Arrays.asList(
                createTestQuitPlan(1, 1, coachId, "Plan for user 1"),
                createTestQuitPlan(2, 2, coachId, "Plan for user 2"),
                createTestQuitPlan(3, 3, coachId, "Plan for user 3"));

        when(repository.findByCoachId(coachId)).thenReturn(coachPlans);

        // Act
        List<QuitPlan> result = quitPlanService.getQuitPlansByCoachId(coachId);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(coachId, result.get(0).getCoachId());
        assertEquals(coachId, result.get(1).getCoachId());
        assertEquals(coachId, result.get(2).getCoachId());
        verify(repository, times(1)).findByCoachId(coachId);
    }

    @Test
    void testGetQuitPlansByCoachId_NoPlans() {
        // Arrange
        Integer coachId = 999;
        when(repository.findByCoachId(coachId)).thenReturn(Arrays.asList());

        // Act
        List<QuitPlan> result = quitPlanService.getQuitPlansByCoachId(coachId);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(repository, times(1)).findByCoachId(coachId);
    }

    @Test
    void testSaveQuitPlan_Success() {
        // Arrange
        QuitPlan quitPlan = createTestQuitPlan(null, 1, 101, "New Plan reason");
        QuitPlan savedPlan = createTestQuitPlan(1, 1, 101, "New Plan reason");
        when(repository.save(any(QuitPlan.class))).thenReturn(savedPlan);

        // Act
        QuitPlan result = quitPlanService.saveQuitPlan(quitPlan);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getPlanId());
        assertEquals(1, result.getUserId());
        assertEquals(101, result.getCoachId());
        assertEquals("New Plan reason", result.getReason());
        verify(repository, times(1)).save(quitPlan);
    }

    @Test
    void testSaveQuitPlan_UpdateExisting() {
        // Arrange
        QuitPlan existingPlan = createTestQuitPlan(5, 2, 102, "Updated Plan reason");
        when(repository.save(any(QuitPlan.class))).thenReturn(existingPlan);

        // Act
        QuitPlan result = quitPlanService.saveQuitPlan(existingPlan);

        // Assert
        assertNotNull(result);
        assertEquals(5, result.getPlanId());
        assertEquals(2, result.getUserId());
        assertEquals(102, result.getCoachId());
        assertEquals("Updated Plan reason", result.getReason());
        verify(repository, times(1)).save(existingPlan);
    }

    @Test
    void testSaveQuitPlan_MultiplePlans() {
        // Arrange
        QuitPlan plan1 = createTestQuitPlan(null, 1, 101, "Plan 1 reason");
        QuitPlan plan2 = createTestQuitPlan(null, 2, 102, "Plan 2 reason");
        QuitPlan savedPlan1 = createTestQuitPlan(1, 1, 101, "Plan 1 reason");
        QuitPlan savedPlan2 = createTestQuitPlan(2, 2, 102, "Plan 2 reason");

        when(repository.save(plan1)).thenReturn(savedPlan1);
        when(repository.save(plan2)).thenReturn(savedPlan2);

        // Act
        QuitPlan result1 = quitPlanService.saveQuitPlan(plan1);
        QuitPlan result2 = quitPlanService.saveQuitPlan(plan2);

        // Assert
        assertEquals(1, result1.getPlanId());
        assertEquals(1, result1.getUserId());
        assertEquals(2, result2.getPlanId());
        assertEquals(2, result2.getUserId());
        verify(repository, times(2)).save(any(QuitPlan.class));
    }

    @Test
    void testDeleteQuitPlan_Success() {
        // Arrange
        Integer planId = 1;
        doNothing().when(repository).deleteById(planId);

        // Act
        quitPlanService.deleteQuitPlan(planId);

        // Assert
        verify(repository, times(1)).deleteById(planId);
    }

    @Test
    void testDeleteQuitPlan_MultipleDeletions() {
        // Arrange
        Integer[] planIds = { 1, 2, 3, 4 };
        for (Integer id : planIds) {
            doNothing().when(repository).deleteById(id);
        }

        // Act
        for (Integer id : planIds) {
            quitPlanService.deleteQuitPlan(id);
        }

        // Assert
        verify(repository, times(4)).deleteById(any(Integer.class));
    }

    @Test
    void testGetAllQuitPlans_Success() {
        // Arrange
        List<QuitPlan> allPlans = Arrays.asList(
                createTestQuitPlan(1, 1, 101, "Plan 1 reason"),
                createTestQuitPlan(2, 2, 102, "Plan 2 reason"),
                createTestQuitPlan(3, 3, 101, "Plan 3 reason"),
                createTestQuitPlan(4, 1, 103, "Plan 4 reason"));
        when(repository.findAll()).thenReturn(allPlans);

        // Act
        List<QuitPlan> result = quitPlanService.getAllQuitPlans();

        // Assert
        assertNotNull(result);
        assertEquals(4, result.size());
        assertEquals("Plan 1 reason", result.get(0).getReason());
        assertEquals("Plan 2 reason", result.get(1).getReason());
        assertEquals("Plan 3 reason", result.get(2).getReason());
        assertEquals("Plan 4 reason", result.get(3).getReason());
        verify(repository, times(1)).findAll();
    }

    @Test
    void testGetAllQuitPlans_EmptyList() {
        // Arrange
        when(repository.findAll()).thenReturn(Arrays.asList());

        // Act
        List<QuitPlan> result = quitPlanService.getAllQuitPlans();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(repository, times(1)).findAll();
    }

    @Test
    void testGetQuitPlansByUserId_WithMultipleUsers() {
        // Arrange
        Integer userId1 = 1;
        Integer userId2 = 2;

        Object userInfo1 = new Object() {
            public String getName() {
                return "User 1";
            }

            public String getEmail() {
                return "user1@example.com";
            }
        };
        Object userInfo2 = new Object() {
            public String getName() {
                return "User 2";
            }

            public String getEmail() {
                return "user2@example.com";
            }
        };

        List<QuitPlan> user1Plans = Arrays.asList(createTestQuitPlan(1, userId1, 101, "User 1 Plan"));
        List<QuitPlan> user2Plans = Arrays.asList(createTestQuitPlan(2, userId2, 102, "User 2 Plan"));

        when(userService.getUserNameAndMailById(userId1)).thenReturn(userInfo1);
        when(userService.getUserNameAndMailById(userId2)).thenReturn(userInfo2);
        when(repository.findByUserId(userId1)).thenReturn(user1Plans);
        when(repository.findByUserId(userId2)).thenReturn(user2Plans);

        // Act
        List<Object> result1 = quitPlanService.getQuitPlansByUserId(userId1);
        List<Object> result2 = quitPlanService.getQuitPlansByUserId(userId2);

        // Assert
        assertNotNull(result1);
        assertNotNull(result2);
        assertEquals(2, result1.size());
        assertEquals(2, result2.size());
        assertEquals(userInfo1, result1.get(0));
        assertEquals(userInfo2, result2.get(0));
        assertEquals(user1Plans, result1.get(1));
        assertEquals(user2Plans, result2.get(1));

        verify(userService, times(1)).getUserNameAndMailById(userId1);
        verify(userService, times(1)).getUserNameAndMailById(userId2);
        verify(repository, times(1)).findByUserId(userId1);
        verify(repository, times(1)).findByUserId(userId2);
    }

    // Helper method to create test quit plans
    private QuitPlan createTestQuitPlan(Integer planId, Integer userId, Integer coachId, String reason) {
        QuitPlan plan = new QuitPlan();
        plan.setPlanId(planId);
        plan.setUserId(userId);
        plan.setCoachId(coachId);
        plan.setReason(reason);
        plan.setStatus(1); // Default status: ontrack
        plan.setStartDate(LocalDate.now());
        plan.setTargetDate(LocalDate.now().plusDays(30));
        plan.setIsEnded(false);
        plan.setStageTwoDuration(7);
        return plan;
    }
}
