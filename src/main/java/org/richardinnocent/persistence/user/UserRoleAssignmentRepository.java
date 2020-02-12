package org.richardinnocent.persistence.user;

import org.richardinnocent.models.user.UserRoleAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Component;

/**
 * Allows easier JPA searching for {@link UserRoleAssignment} entities.
 */
@Component
public interface UserRoleAssignmentRepository extends
    JpaRepository<UserRoleAssignment, Long>,
    JpaSpecificationExecutor<UserRoleAssignment> {

}
