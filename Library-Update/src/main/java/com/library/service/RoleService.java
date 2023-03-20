package com.library.service;

import com.library.domain.Role;
import com.library.domain.enums.RoleType;
import com.library.exception.ResourceNotFoundException;
import com.library.exception.message.ErrorMessage;
import com.library.repository.RoleRepository;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role getRoleByType(RoleType type) {
        return roleRepository.findByType(type).orElseThrow(()
                ->new ResourceNotFoundException(String.format
                (ErrorMessage.ROL_NOT_FOUND_EXCEPTION,type.name())));
    }
}