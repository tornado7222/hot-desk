package com.example.hotdesk.office;

import com.example.hotdesk.common.configuration.CustomPageImpl;
import com.example.hotdesk.office.dto.AddressDto;
import com.example.hotdesk.office.dto.OfficeCreateDto;
import com.example.hotdesk.office.dto.OfficeResponseDto;
import com.example.hotdesk.office.dto.OfficeUpdateDto;
import com.example.hotdesk.office.entity.Office;
import com.example.hotdesk.room.dto.RoomResponseDto;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class OfficeControllerTest {
    @Autowired
    TestRestTemplate testRestTemplate;
    @Autowired
    OfficeRepository officeRepository;
    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16.0")
            .withUsername("postgres")
            .withPassword("postgres");

    @Test
    @Order(value = 1)
    void createOffice() {
        AddressDto addressDto = new AddressDto();
        addressDto.setCountry("Uzbekistan");
        addressDto.setCity("Toshkent");
        addressDto.setStreet("Amir Temur");
        addressDto.setBuilding("5A");
        OfficeCreateDto officeCreateDto = new OfficeCreateDto();
        officeCreateDto.setName("Academy");
        officeCreateDto.setAddress(addressDto);
        ResponseEntity<OfficeResponseDto> responseEntity = testRestTemplate.postForEntity("/office", officeCreateDto, OfficeResponseDto.class);
        Assertions.assertEquals(responseEntity.getStatusCode(), HttpStatus.CREATED);

        OfficeResponseDto officeResponseDto = responseEntity.getBody();
        Assertions.assertNotNull(officeResponseDto);
        Assertions.assertEquals(officeCreateDto.getName(), officeResponseDto.getName());
        Assertions.assertEquals(officeCreateDto.getAddress().getCountry(), officeResponseDto.getAddress().getCountry());
        Assertions.assertEquals(officeCreateDto.getAddress().getCity(), officeResponseDto.getAddress().getCity());
        Assertions.assertEquals(officeCreateDto.getAddress().getStreet(), officeResponseDto.getAddress().getStreet());
        Assertions.assertEquals(officeCreateDto.getAddress().getBuilding(), officeResponseDto.getAddress().getBuilding());

        Optional<Office> optionalOffice = officeRepository.findById(officeResponseDto.getId());

        Assertions.assertTrue(optionalOffice.isPresent());
        Office office = optionalOffice.get();
        Assertions.assertEquals(office.getName(), officeCreateDto.getName());

    }

    @Test
    @Order(value = 2)
    void getOffices() {
        ResponseEntity<CustomPageImpl<OfficeResponseDto>> response = testRestTemplate.exchange("/office?predicate=name==Academy", HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<>() {
        });
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);

        PageImpl<OfficeResponseDto> body = response.getBody();

        Assertions.assertNotNull(body);
        Assertions.assertEquals(body.getNumberOfElements(), 1);
        OfficeResponseDto officeResponseDto = body.getContent().get(0);

        Assertions.assertEquals(officeResponseDto.getName(), "Academy");
    }

    @Test
    @Order(value = 3)
    void getRoomById() {
        Optional<Office> optionalOffice = officeRepository.findAll().stream().findAny();

        Assertions.assertTrue(optionalOffice.isPresent());
        Office office = optionalOffice.get();

        ResponseEntity<OfficeResponseDto> response = testRestTemplate.getForEntity("/office/%s".formatted(office.getId()), OfficeResponseDto.class);

        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    @Order(value = 4)
    void updateOffice() {
        Optional<Office> optionalOffice = officeRepository.findAll().stream().findAny();

        Assertions.assertTrue(optionalOffice.isPresent());
        Office office = optionalOffice.get();

        AddressDto addressDto = new AddressDto();
        addressDto.setCountry("Uzbekistan");
        addressDto.setCity("Toshkent");
        addressDto.setStreet("Amir Temur");
        addressDto.setBuilding("10A");
        OfficeUpdateDto officeUpdateDto = new OfficeUpdateDto();
        officeUpdateDto.setName("Academy");
        officeUpdateDto.setAddress(addressDto);

        testRestTemplate.put("/office/%s".formatted(office.getId()), officeUpdateDto, office.getId());

        ResponseEntity<OfficeResponseDto> response = testRestTemplate
                .getForEntity("/office/%s".formatted(office.getId()), OfficeResponseDto.class);

        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);

        OfficeResponseDto officeResponseDto = response.getBody();
        Assertions.assertNotNull(officeResponseDto);
    }

    @Test
    @Order(value = 5)
    void deleteOffice() {
        Optional<Office> optionalOffice = officeRepository.findAll().stream().findAny();

        Assertions.assertTrue(optionalOffice.isPresent());
        Office office = optionalOffice.get();

        testRestTemplate.delete("/office/%s".formatted(office.getId()));
        ResponseEntity<RoomResponseDto> response = testRestTemplate
                .getForEntity("/office/%s".formatted(office.getId()), RoomResponseDto.class);

        Assertions.assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }
}