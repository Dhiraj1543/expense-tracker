package backend.factories;

import backend.enums.ERole;
import backend.exceptions.RoleNotFoundException;
import backend.models.Role;
import backend.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RoleFactory {

    @Autowired
    private RoleService roleService;

    public Role getInstance(String role) throws RoleNotFoundException {

        if (role.equalsIgnoreCase("admin")) {
            return roleService.findByName(ERole.ROLE_ADMIN);
        }

        else if (role.equalsIgnoreCase("user")) {
            return roleService.findByName(ERole.ROLE_USER);
        }

        throw new RoleNotFoundException("Invalid role name: " + role);
    }
}