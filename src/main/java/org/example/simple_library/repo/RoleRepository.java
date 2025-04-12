package org.example.simple_library.repo;

import org.example.simple_library.entity.Role;
import org.example.simple_library.entity.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByRoleName(RoleName roleName);
}