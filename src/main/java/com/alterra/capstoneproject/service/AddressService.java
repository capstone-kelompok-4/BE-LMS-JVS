package com.alterra.capstoneproject.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alterra.capstoneproject.domain.dao.Address;
import com.alterra.capstoneproject.domain.dao.User;
import com.alterra.capstoneproject.domain.dto.AddressDto;
import com.alterra.capstoneproject.repository.AddressRepository;
import com.alterra.capstoneproject.repository.UserRepository;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
@NoArgsConstructor
@Transactional
public class AddressService {
    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    public Address postAddress(User user, AddressDto request) {
        try {
            log.info("Post user address");
            Address address = new Address();
            
            address.setDetailAddress(request.getDetailAddress());
            address.setCountry(request.getCountry());
            address.setStateProvince(request.getStateProvince());
            address.setCity(request.getCity());
            address.setZipCode(request.getZipCode());  
            address.setUserAddress(user);

            addressRepository.save(address);

            log.info("Update user");
            user.setAddress(address);

            userRepository.save(user);

            return address;
        } catch (Exception e) {
            log.error("Post user address error");
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public Address updateAddress(AddressDto request) {
        try {
            log.info("Get user");
            User user = userRepository.findUsername(request.getEmail());

            log.info("Get address");
            Address address = addressRepository.getById(user.getId());

            address.setDetailAddress(request.getDetailAddress());
            address.setCountry(request.getCountry());
            address.setStateProvince(request.getStateProvince());
            address.setCity(request.getCity());
            address.setZipCode(request.getZipCode());
            
            addressRepository.save(address);

            return address;
        } catch (Exception e) {
            log.error("Update user address error");
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
