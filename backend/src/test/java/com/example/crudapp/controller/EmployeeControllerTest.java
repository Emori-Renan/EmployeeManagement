// package com.example.crudapp.controller;

// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.test.web.servlet.MockMvc;

// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
// import org.springframework.http.MediaType;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

// import com.example.crudapp.model.Employee;
// import com.example.crudapp.model.UserLogin;
// import com.example.crudapp.repository.EmployeeRepository;
// import com.example.crudapp.repository.UserLoginRepository;

// import jakarta.transaction.Transactional;

// @SpringBootTest
// @AutoConfigureMockMvc(addFilters = false)
// @Transactional
// class EmployeeControllerTest {

//     @Autowired
//     private MockMvc mockMvc;

//     @Autowired
//     private EmployeeRepository employeeRepository;

//     @Autowired
//     private UserLoginRepository userLoginRepository;

//     private UserLogin user;

//     @BeforeEach
//     void setUp() {
//         employeeRepository.deleteAll();
//         userLoginRepository.deleteAll();

//         user = new UserLogin("testuser", "test@example.com", "password", "employee");
//         userLoginRepository.save(user);
//     }

//     @Test
//     void testGetEmployeeById_whenExists_returnsEmployee() throws Exception {
//         Employee emp = new Employee();
//         emp.setEmployeeName("Alice");
//         emp.setRole("employee");
//         emp.setUserLogin(user);
//         employeeRepository.save(emp);

//         mockMvc.perform(get("/employee/" + emp.getId()))
//             .andExpect(status().isOk())
//             .andExpect(jsonPath("$.data.employeeName").value("Alice"))
//             .andExpect(jsonPath("$.data.role").value("employee"));
//     }

//     @Test
//     void testUpdateEmployee_onlyUpdatesName() throws Exception {
//         Employee emp = new Employee();
//         emp.setEmployeeName("Bob");
//         emp.setRole("employee");
//         emp.setUserLogin(user);
//         employeeRepository.save(emp);

//         String json = """
//             {
//                 "employeeName": "Bob Updated",
//                 "role": "admin"
//             }
//         """;

//         mockMvc.perform(put("/employee/" + emp.getId())
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content(json))
//             .andExpect(status().isOk())
//             .andExpect(jsonPath("$.data.employeeName").value("Bob Updated"));

//         Employee updated = employeeRepository.findById(emp.getId()).orElseThrow();
//         assertEquals("Bob Updated", updated.getEmployeeName());
//     }

// }
