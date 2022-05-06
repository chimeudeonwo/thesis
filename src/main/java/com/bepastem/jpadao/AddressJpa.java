package com.bepastem.jpadao;

import com.bepastem.models.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressJpa extends JpaRepository<Address, String> {
}
