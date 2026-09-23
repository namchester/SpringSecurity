package vn.iotstar.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.iotstar.dto.UserDTO;
import vn.iotstar.entity.Role;
import vn.iotstar.entity.User;
import vn.iotstar.mapper.UserMapper;
import vn.iotstar.repository.ProductRepository;
import vn.iotstar.repository.RoleRepository;
import vn.iotstar.repository.UserRepository;
import vn.iotstar.service.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ProductRepository productRepository;
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public Page<UserDTO> findAll(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(
                Math.max(page, 0),
                Math.max(size, 1),
                Sort.by(Sort.Direction.DESC, "id")
        );

        String searchKeyword = keyword == null ? "" : keyword.trim();
        return userRepository.search(searchKeyword, pageable)
                .map(user -> {
                    UserDTO dto = mapper.toDTO(user);
                    dto.setProductCount(userRepository.countProductsByUserId(user.getId()));
                    return dto;
                });
    }

    @Override
    @Transactional(readOnly = true)
    public UserDTO findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User không tồn tại"));

        UserDTO dto = mapper.toDTO(user);
        dto.setProductCount(userRepository.countProductsByUserId(id));
        return dto;
    }

    @Override
    @Transactional
    public UserDTO create(UserDTO dto) {
        String username = dto.getUsername().trim();
        String email = dto.getEmail().trim().toLowerCase();

        if (userRepository.existsByUsernameIgnoreCase(username)) {
            throw new IllegalArgumentException("Username đã tồn tại");
        }
        if (userRepository.existsByEmailIgnoreCase(email)) {
            throw new IllegalArgumentException("Email đã tồn tại");
        }

        User user = mapper.toEntity(dto);
        Role role = resolveRole(dto.getRoleName());

        user.setUsername(username);
        user.setEmail(email);
        user.setFullName(dto.getFullName().trim());
        user.setRole(role);
        user.setPassword(passwordEncoder.encode("123456"));
        user.setEnabled(dto.isEnabled());

        return mapper.toDTO(userRepository.save(user));
    }

    @Override
    @Transactional
    public UserDTO update(Long id, UserDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User không tồn tại"));

        String username = dto.getUsername().trim();
        String email = dto.getEmail().trim().toLowerCase();

        userRepository.findByUsernameIgnoreCase(username)
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new IllegalArgumentException("Username đã tồn tại");
                });

        userRepository.findByEmailIgnoreCase(email)
                .filter(existing -> !existing.getId().equals(id))
                .ifPresent(existing -> {
                    throw new IllegalArgumentException("Email đã tồn tại");
                });

        user.setUsername(username);
        user.setEmail(email);
        user.setFullName(dto.getFullName().trim());
        user.setEnabled(dto.isEnabled());
        user.setRole(resolveRole(dto.getRoleName()));

        return mapper.toDTO(userRepository.save(user));
    }

    private Role resolveRole(String roleName) {
        String requestedRole = roleName == null || roleName.isBlank()
                ? "ROLE_USER"
                : roleName.trim();
        return roleRepository.findByName(requestedRole)
                .orElseThrow(() -> new IllegalArgumentException("Role không tồn tại"));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User không tồn tại"));

        if (productRepository.countByUserId(id) > 0) {
            throw new IllegalArgumentException("User còn sản phẩm, hãy xóa sản phẩm trước");
        }

        userRepository.delete(user);
    }

    @Override
    @Transactional(readOnly = true)
    public long countUsers() {
        return userRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public long countProducts(Long userId) {
        return userRepository.countProductsByUserId(userId);
    }
}
