package ph.edu.dlsu.greencanyonairbnb.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ph.edu.dlsu.greencanyonairbnb.exception.RoleAlreadyExistException;
import ph.edu.dlsu.greencanyonairbnb.model.Role;
import ph.edu.dlsu.greencanyonairbnb.model.User;
import ph.edu.dlsu.greencanyonairbnb.service.RoleServiceInt;

import java.util.List;

import static org.springframework.http.HttpStatus.FOUND;


@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleController {
    private final RoleServiceInt roleService;

    @GetMapping("/allroles")
    public ResponseEntity<List<Role>> getAllRoles(){
        return new ResponseEntity<>(roleService.getRoles(), FOUND);
    }

    @PostMapping("/create-newrole")
    public ResponseEntity<String> createRole(@RequestBody Role theRole){
        try{
            roleService.createRole(theRole);
            return ResponseEntity.ok("Role created successfully.");
        } catch(RoleAlreadyExistException re){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(re.getMessage());
        }
    }

    @DeleteMapping("/delete/{roleID}")
    public void deleteRole(@PathVariable("roleID") Long roleID){
        roleService.deleteRole(roleID);
    }

    @PostMapping("/remove-user-role")
    public User removeUserFromRole(@RequestParam("userID") long userID, @RequestParam("roleID") long roleID ){
        return roleService.removeUserFromRole(userID, roleID);
    }

    @PostMapping("/add-user-role")
    public User addUserToRole(@RequestParam("userID") long userID, @RequestParam("roleID") long roleID ){
        return roleService.addUserToRole(userID, roleID);
    }

    @PostMapping("/remove-all-users-from-role/{roleID}")
    public Role removeAllUsersFromRole(@RequestParam("roleID") long roleID ){
        return roleService.removeAllUsersFromRole(roleID);
    }
}
