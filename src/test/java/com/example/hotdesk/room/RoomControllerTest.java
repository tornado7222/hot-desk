package com.example.hotdesk.room;

import com.example.hotdesk.common.configuration.CustomPageImpl;
import com.example.hotdesk.office.OfficeRepository;
import com.example.hotdesk.office.dto.AddressDto;
import com.example.hotdesk.office.dto.OfficeCreateDto;
import com.example.hotdesk.office.dto.OfficeResponseDto;
import com.example.hotdesk.office.entity.Office;
import com.example.hotdesk.room.dto.RoomCreateDto;
import com.example.hotdesk.room.dto.RoomResponseDto;
import com.example.hotdesk.room.dto.RoomUpdateDto;
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

import java.util.Optional;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RoomControllerTest {
    @Autowired
    TestRestTemplate testRestTemplate;
    @Autowired
    RoomRepository roomRepository;
    @Autowired
    OfficeRepository officeRepository;
    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:16.0")
            .withUsername("postgres")
            .withPassword("postgres");

    @Test
    @Order(value = 1)
    void createRoom() {
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
    }

    @Test
    @Order(value = 2)
    void getRooms() {
        ResponseEntity<CustomPageImpl<RoomResponseDto>> response = testRestTemplate.exchange("/room?predicate=floorNumber==1", HttpMethod.GET, HttpEntity.EMPTY, new ParameterizedTypeReference<>() {
        });
        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);

        PageImpl<RoomResponseDto> body = response.getBody();

        Assertions.assertNotNull(body);
        Assertions.assertEquals(body.getNumberOfElements(), 1);
        RoomResponseDto roomResponseDto = body.getContent().get(0);

        Assertions.assertEquals(roomResponseDto.getFloorNumber(), 1);
    }

    @Test
    @Order(value = 3)
    void getRoomById() {
        Optional<Room> optionalRoom = roomRepository.findAll().stream().findAny();

        Assertions.assertTrue(optionalRoom.isPresent());
        Room room = optionalRoom.get();

        ResponseEntity<RoomResponseDto> response = testRestTemplate.getForEntity("/room/%s".formatted(room.getId()), RoomResponseDto.class);

        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
    }

    @Test
    @Order(value = 4)
    void updateRoom() {
        Optional<Room> optionalRoom = roomRepository.findAll().stream().findAny();

        Assertions.assertTrue(optionalRoom.isPresent());
        Room room = optionalRoom.get();

        RoomUpdateDto roomUpdateDto = new RoomUpdateDto();
        roomUpdateDto.setRoomType(RoomType.KITCHEN);
        roomUpdateDto.setNumber("101");
        roomUpdateDto.setFloorNumber(1);
        roomUpdateDto.setOfficeId(1);

        testRestTemplate.put("/room/%s".formatted(room.getId()), roomUpdateDto, room.getId());

        ResponseEntity<RoomResponseDto> response = testRestTemplate
                .getForEntity("/room/%s".formatted(room.getId()), RoomResponseDto.class);

        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);

        RoomResponseDto roomResponseDto = response.getBody();
        Assertions.assertNotNull(roomResponseDto);
    }

    @Test
    @Order(value = 5)
    void deleteRoom() {
        Optional<Room> optionalRoom = roomRepository.findAll().stream().findAny();

        Assertions.assertTrue(optionalRoom.isPresent());
        Room room = optionalRoom.get();

        testRestTemplate.delete("/room/%s".formatted(room.getId()));
        ResponseEntity<RoomResponseDto> response = testRestTemplate
                .getForEntity("/room/%s".formatted(room.getId()), RoomResponseDto.class);

        Assertions.assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }
}