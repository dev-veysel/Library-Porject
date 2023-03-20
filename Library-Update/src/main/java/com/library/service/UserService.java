package com.library.service;

import com.library.domain.Role;
import com.library.domain.User;
import com.library.domain.enums.RoleType;
import com.library.dto.LoanDTO;
import com.library.dto.RegisterDTO;
import com.library.dto.UserDTO;
import com.library.dto.request.RegisterRequest;
import com.library.dto.request.UserRequest;
import com.library.dto.response.UserResponse;
import com.library.exception.BadRequestException;
import com.library.exception.ConflictException;
import com.library.exception.ResourceNotFoundException;
import com.library.exception.message.ErrorMessage;
import com.library.mapper.UserMapper;
import com.library.repository.UserRepository;
import com.library.security.SecurityUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final LoanService loanService;


    public UserService(UserRepository userRepository, UserMapper userMapper, RoleService roleService, @Lazy PasswordEncoder passwordEncoder, LoanService loanService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.loanService = loanService;
    }


    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException(
                String.format(ErrorMessage.USER_NOT_FOUND_EXCEPTION, email)
        ));
    }


    public RegisterDTO register(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new ConflictException(String.format(ErrorMessage.EMAIL_CONFLICT_EXCEPTION, registerRequest.getEmail()));
        }
        User user = userMapper.registerRequestToUser(registerRequest);
        Role role = roleService.getRoleByType(RoleType.ROLE_MEMBER);
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);

        String encodedPassword = passwordEncoder.encode(registerRequest.getPassword());
        user.setPassword(encodedPassword);
        userRepository.save(user);
        user.setPassword(registerRequest.getPassword());
        return userMapper.userToRegisterDTO(user);

    }


    public UserDTO getAuthenticatedUser() {
        User user = getCurrentlyUser();
        return userMapper.userToUserDTO(user);
    }


    public User getCurrentlyUser() {
        String email = SecurityUtils.getCurrentUserEmail().orElseThrow(() -> new ResourceNotFoundException(
                ErrorMessage.PRINCIPAL_FOUND_MESSAGE));
        return getUserByEmail(email);
    }


    public Page<LoanDTO> getLoansByUser(Pageable pageable) {
        User user = getCurrentlyUser();
        return loanService.getLoansByPage(user, pageable);
    }


    public Page<UserDTO> getAllWithUser(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        return users.map(userMapper::userToUserDTO);
    }


    public UserDTO getUserById(Long id) {
        User user = findById(id);
        return userMapper.userToUserDTO(user);
    }
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(String.format(ErrorMessage.USER_NOT_FOUND_EXCEPTION, id)));
    }


    public UserDTO saveUserByStaff(UserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ConflictException(String.format(ErrorMessage.EMAIL_CONFLICT_EXCEPTION, request.getEmail()));
        }

        User user = userMapper.userRequestToUser(request);
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        user.setPassword(encodedPassword);
        User authenticatedUser = getCurrentlyUser();

        for (Role role : authenticatedUser.getRoles()) {
            if (role.getType().getName().equals(RoleType.ROLE_ADMIN.getName())) {
                user.setRoles(convertRoles(request.getRole()));
                break;
            } else {
                for(String str: request.getRole())
                    if (str.equals(RoleType.ROLE_ADMIN.getName()) || str.equals(RoleType.ROLE_STAFF.getName())) {
                        throw new BadRequestException(ErrorMessage.UNAUTHORIZED_ERROR_MESSAGE);
                    } else {
                        user.setRoles(convertRoles(request.getRole()));}
            }
        }
        userRepository.save(user);
        user.setPassword(request.getPassword());
        return userMapper.userToUserDTO(user);
    }


    private Set<Role> convertRoles(Set<String> pRoles) { // pRoles={"Customer","Administrator"}
        Set<Role> roles = new HashSet<>();

        pRoles.forEach(roleStr -> {
            if (roleStr.equals(RoleType.ROLE_ADMIN.getName())) {
                Role adminRole = roleService.getRoleByType(RoleType.ROLE_ADMIN);
                roles.add(adminRole);
            } else if (roleStr.equals(RoleType.ROLE_STAFF.getName())) {
                Role userRole = roleService.getRoleByType(RoleType.ROLE_STAFF);
                roles.add(userRole);
            } else {
                Role userRole = roleService.getRoleByType(RoleType.ROLE_MEMBER);
                roles.add(userRole);
            }
        });
        return roles;
    }


    public UserDTO updateUserById(Long id, UserRequest request) {
        User user=findById(id);
        String encodedPassword=passwordEncoder.encode(request.getPassword());
        if(userRepository.existsByEmail(request.getEmail())&&!user.getEmail().equals(request.getEmail())){
            throw new ConflictException(String.format(ErrorMessage.EMAIL_CONFLICT_EXCEPTION));
        }
        if(user.getBuiltIn()){
            throw new BadRequestException(ErrorMessage.NOT_PERMITED_METOD_MESSAGE);
        }
        User authenticatedUser = getCurrentlyUser();
        for(Role str: authenticatedUser.getRoles()){
            if(str.getType().getName().equals(RoleType.ROLE_ADMIN.getName())){

                user.setAddress(request.getAddress());
                user.setEmail(request.getEmail());
                user.setBirthDate(request.getBirthDate());
                user.setFirstName(request.getFirstName());
                user.setLastName(request.getLastName());
                user.setPhone(request.getPhone());
                user.setRoles(convertRoles(request.getRole()));
                user.setPassword(encodedPassword);
                userRepository.save(user);
                break;
            }else {
                for(Role role: user.getRoles()){
                    if(role.getType().getName().equals(RoleType.ROLE_MEMBER.getName())){

                        user.setAddress(request.getAddress());
                        user.setEmail(request.getEmail());
                        user.setBirthDate(request.getBirthDate());
                        user.setFirstName(request.getFirstName());
                        user.setLastName(request.getLastName());
                        user.setPhone(request.getPhone());
                        user.setRoles(convertRoles(request.getRole()));
                        user.setPassword(encodedPassword);
                        userRepository.save(user);
                    }else {
                        throw new BadRequestException(ErrorMessage.UNAUTHORIZED_ERROR_MESSAGE);
                    }
                }
            }
        }
        return userMapper.userToUserDTO(user);
    }


    public UserDTO deleteUser(Long id) {
        User user=findById(id);
        if(user.getBuiltIn()){
            throw new BadRequestException(ErrorMessage.NOT_PERMITED_METOD_MESSAGE);
        }
        if(!user.getLoans().isEmpty()){
            throw new BadRequestException(ErrorMessage.USER_HAS_LOAN_EXCEPTION);
        }
        UserDTO userdto =userMapper.userToUserDTO(user);
        userRepository.delete(user);
        return userdto;
    }


    public Long getUserCount() {
        return userRepository.count();
    }


    public void updateScore(User user) {
        userRepository.save(user);
    }


    public UserResponse userToUserResponse(User user) {
        if (user == null) {
            return null;
        }
        UserResponse userDTO=new UserResponse();
        userDTO.setId(user.getId());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setScore(user.getScore());
        userDTO.setAddress(user.getAddress());
        userDTO.setPhone(user.getPhone());
        userDTO.setBirthDate(user.getBirthDate());
        userDTO.setEmail(user.getEmail());
        userDTO.setCreateDate(user.getCreateDate());
        userDTO.setBookCount(user.getLoans().size());
        return userDTO;
    }
}