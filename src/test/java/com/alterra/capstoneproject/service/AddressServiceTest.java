package com.alterra.capstoneproject.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.alterra.capstoneproject.domain.dao.Address;
import com.alterra.capstoneproject.domain.dao.User;
import com.alterra.capstoneproject.domain.dto.AddressDto;
import com.alterra.capstoneproject.repository.AddressRepository;
import com.alterra.capstoneproject.repository.UserRepository;

@SpringBootTest(classes = AddressService.class)
public class AddressServiceTest {
    private final EasyRandom EASY_RANDOM = new EasyRandom();
    private User user;
    private Address address;
    private AddressDto addressDto;

    @MockBean
    private AddressRepository addressRepository;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private AddressService addressService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = EASY_RANDOM.nextObject(User.class); 
        address = EASY_RANDOM.nextObject(Address.class);
        addressDto = EASY_RANDOM.nextObject(AddressDto.class);
    }
    
    @Test
    void postAddressSuccessTest() {
        Address testAddress = new Address();

        testAddress.setDetailAddress(addressDto.getDetailAddress());
        testAddress.setCountry(addressDto.getCountry());
        testAddress.setStateProvince(addressDto.getStateProvince());
        testAddress.setCity(addressDto.getCity());
        testAddress.setZipCode(addressDto.getZipCode());
        testAddress.setUserAddress(user);
        
        when(addressRepository.save(testAddress)).thenReturn(testAddress);

        user.setAddress(testAddress);

        when(userRepository.save(user)).thenReturn(user);

        var result = addressService.postAddress(user, addressDto);

        assertEquals(testAddress, result);
    }

    @Test
    void postAddressExceptionTest() {
        Address testAddress = new Address();

        testAddress.setDetailAddress(addressDto.getDetailAddress());
        testAddress.setCountry(addressDto.getCountry());
        testAddress.setStateProvince(addressDto.getStateProvince());
        testAddress.setCity(addressDto.getCity());
        testAddress.setZipCode(addressDto.getZipCode());
        testAddress.setUserAddress(user);
        
        when(addressRepository.save(testAddress))
            .thenThrow(RuntimeException.class);

        assertThrows(RuntimeException.class, () -> {
            addressService.postAddress(user, addressDto);
        });
    }

    @Test
    void updateAddressSuccessTest() {
        when(userRepository.findUsername(addressDto.getEmail())).thenReturn(user);

        when(addressRepository.getById(user.getId())).thenReturn(address);

        when(addressRepository.save(address)).thenReturn(address);

        var result = addressService.updateAddress(addressDto);

        assertEquals(address, result);
    }

    @Test
    void updateAddressExceptionTest() {
        when(userRepository.findUsername(addressDto.getEmail())).thenReturn(user);

        when(addressRepository.getById(user.getId())).thenReturn(address);

        when(addressRepository.save(address))
            .thenThrow(RuntimeException.class);

        assertThrows(RuntimeException.class, () -> {
            addressService.updateAddress(addressDto);
        });
    }
}
