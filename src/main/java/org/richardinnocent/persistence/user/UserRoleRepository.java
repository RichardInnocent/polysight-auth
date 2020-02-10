package org.richardinnocent.persistence.user;

import org.richardinnocent.models.user.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Component;

/**
 * Allows easier JPA searching for {@link UserRole} entities.
 */
@Component
public interface UserRoleRepository extends JpaRepository<UserRole, Long>,
    JpaSpecificationExecutor<UserRole> {

}
