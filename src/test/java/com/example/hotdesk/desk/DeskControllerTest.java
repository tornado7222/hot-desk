package com.example.hotdesk.desk;

import com.example.hotdesk.common.configuration.CustomPageImpl;
import com.example.hotdesk.desk.dto.DeskCreateDto;
import com.example.hotdesk.desk.dto.DeskResponseDto;
import com.example.hotdesk.desk.dto.DeskUpdateDto;
import com.example.hotdesk.desk.entity.Accessories;
import com.example.hotdesk.desk.entity.Desk;
import com.example.hotdesk.office.OfficeRepository;
import com.example.hotdesk.office.dto.AddressDto;
import com.example.hotdesk.office.dto.OfficeCreateDto;
import com.example.hotdesk.office.dto.OfficeResponseDto;
import com.example.hotdesk.office.entity.Office;
import com.example.hotdesk.room.RoomRepository;
import com.example.hotdesk.room.dto.RoomCreateDto;
import com.example.hotdesk.room.dto.RoomResponseDto;
import com.example.hotdesk.room.entity.Room;
import com.example.hotdesk.room.entity.RoomType;
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

import java.util.List;
import java.util.Optional;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DeskControllerTest {
    @Autowired
    TestRestTemplate testRestTemplate;
    @Autowired
    DeskRepository deskRepository;
    @Autowired
    OfficeRepository officeRepository;
    @Autowired
    RoomRepository roomRepository;
    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16.0")
            .withUsername("postgres")
            .withPassword("postgres");

    @Test
    @Order(value = 1)
    void createDesk() {
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

        RoomCreateDto roomCreateDto = new RoomCreateDto();
        roomCreateDto.setNumber("101");
        roomCreateDto.setRoomType(RoomType.GAME_ROOM);
        roomCreateDto.setFloorNumber(1);
        roomCreateDto.setOfficeId(officeResponseDto.getId());

        ResponseEntity<RoomResponseDto> response = testRestTemplate.postForEntity("/room", roomCreateDto, RoomResponseDto.class);

        Assertions.assertEquals(response.getStatusCode(), HttpStatus.CREATED);
        RoomResponseDto responseDto = response.getBody();
        Assertions.assertNotNull(responseDto);
        Assertions.assertNotNull(roomCreateDto.getOfficeId());
        Assertions.assertEquals(roomCreateDto.getNumber(), responseDto.getNumber());
        Assertions.assertEquals(roomCreateDto.getFloorNumber(), responseDto.getFloorNumber());
        Assertions.assertEquals(roomCreateDto.getRoomType(), responseDto.getRoomType());
        Assertions.assertEquals(roomCreateDto.getOfficeId(), responseDto.getOfficeId());

        Optional<Room> optionalRoom = roomRepository.findById(responseDto.getId());

        Assertions.assertTrue(optionalRoom.isPresent());
        Room room = optionalRoom.get();
        Assertions.assertEquals(room.getFloorNumber(), roomCreateDto.getFloorNumber());

        DeskCreateDto deskCreateDto = new DeskCreateDto();
        deskCreateDto.setAccessories(List.of(Accessories.ETHERNET, Accessories.MONITOR));
    }

    @Test
    @Order(value = 2)
    void getDesks() {
        ResponseEntity<CustomPageImpl<DeskResponseDto>> response = testRestTemplate.exchange("/desk", HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<>() {
        });
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);

        PageImpl<DeskResponseDto> body = response.getBody();

        Assertions.assertNotNull(body);
    }

    @Test
    @Order(value = 3)
    void getRoomById() {
        Optional<Desk> optionalDesk = deskRepository.findAll().stream().findAny();

        Assertions.assertTrue(optionalDesk.isPresent());
        Desk desk = optionalDesk.get();

        ResponseEntity<DeskResponseDto> response = testRestTemplate.getForEntity("/desk/%s".formatted(desk.getId()), DeskResponseDto.class);

        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    @Order(value = 4)
    void updateOffice() {
        Optional<Desk> deskOptional = deskRepository.findAll().stream().findAny();

        Assertions.assertTrue(deskOptional.isPresent());
        Desk desk = deskOptional.get();

        DeskUpdateDto deskUpdateDto = new DeskUpdateDto();
        deskUpdateDto.setAccessories(List.of(Accessories.MONITOR));
        deskUpdateDto.setRoomId(1);

        testRestTemplate.put("/desk/%s".formatted(desk.getId()), deskUpdateDto, desk.getId());

        ResponseEntity<DeskResponseDto> response = testRestTemplate
                .getForEntity("/desk/%s".formatted(desk.getId()), DeskResponseDto.class);

        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);

        DeskResponseDto deskResponseDto = response.getBody();
        Assertions.assertNotNull(deskResponseDto);
    }

    @Test
    @Order(value = 5)
    void deleteOffice() {
        Optional<Desk> optionalDesk = deskRepository.findAll().stream().findAny();

        Assertions.assertTrue(optionalDesk.isPresent());
        Desk desk = optionalDesk.get();

        testRestTemplate.delete("/desk/%s".formatted(desk.getId()));
        ResponseEntity<DeskResponseDto> response = testRestTemplate
                .getForEntity("/desk/%s".formatted(desk.getId()), DeskResponseDto.class);

        Assertions.assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }
}