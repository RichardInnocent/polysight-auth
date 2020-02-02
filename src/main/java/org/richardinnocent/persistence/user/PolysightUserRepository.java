package org.richardinnocent.persistence.user;

import org.richardinnocent.models.user.PolysightUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Component;

/**
 * ALlows easier JPA searching for {@link PolysightUser} entities.
 */
@Component
public interface PolysightUserRepository extends JpaRepository<PolysightUser, Long>,
    JpaSpecificationExecutor<PolysightUser> {

}
