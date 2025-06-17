// package com.example.crudapp.controller;

// import com.example.crudapp.dto.ServiceResponse;
// import com.example.crudapp.service.WorkplaceService;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.MockitoAnnotations;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;

// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.mockito.Mockito.when;

// class WorkplaceControllerTest {

//     @Mock
//     private WorkplaceService workplaceService;

//     @InjectMocks
//     private WorkplaceController workplaceController;

//     @BeforeEach
//     void setUp() {
//         MockitoAnnotations.openMocks(this);
//     }

//     @Test
//     void getAllWorkplacesByEmployeeId_Success() {
//         // Arrange
//         Long employeeId = 1L;
//         ServiceResponse expectedResponse = new ServiceResponse(true, "Workplaces found", null);
//         when(workplaceService.getAllWorkplacesByEmployeeId(employeeId)).thenReturn(expectedResponse);

//         // Act
//         ResponseEntity<ServiceResponse> responseEntity = workplaceController.getAllWorkplacesByEmployeeId(employeeId);

//         // Assert
//         assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
//         assertEquals(expectedResponse, responseEntity.getBody());
//     }

//     @Test
//     void getAllWorkplacesByEmployeeId_NotFound() {
//         // Arrange
//         Long employeeId = 1L;
//         ServiceResponse expectedResponse = new ServiceResponse(false, "Workplaces not found", null); 
//         when(workplaceService.getAllWorkplacesByEmployeeId(employeeId)).thenReturn(expectedResponse);

//         // Act
//         ResponseEntity<ServiceResponse> responseEntity = workplaceController.getAllWorkplacesByEmployeeId(employeeId);

//         // Assert
//         assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
//         assertEquals(expectedResponse, responseEntity.getBody());
//     }
// }
