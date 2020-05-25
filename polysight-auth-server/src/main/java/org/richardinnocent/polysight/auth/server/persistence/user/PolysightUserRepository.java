package org.richardinnocent.polysight.auth.server.persistence.user;

import org.richardinnocent.polysight.auth.server.models.user.PolysightUser;
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
