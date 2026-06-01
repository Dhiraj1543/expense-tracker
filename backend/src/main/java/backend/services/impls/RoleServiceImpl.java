package backend.services.impls;

import backend.enums.ERole;
import backend.models.Role;
import backend.repository.RoleRepository;
import backend.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Role findByName(ERole eRole) {

        return roleRepository.findByName(eRole)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Role is not found."
                        )
                );
    }
}