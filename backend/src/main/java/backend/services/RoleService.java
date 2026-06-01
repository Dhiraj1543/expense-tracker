package backend.services;

import backend.enums.ERole;
import backend.models.Role;
import org.springframework.stereotype.Service;

@Service
public interface RoleService {

    Role findByName(ERole eRole);
}