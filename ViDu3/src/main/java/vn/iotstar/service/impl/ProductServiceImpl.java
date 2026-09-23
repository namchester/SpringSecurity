package vn.iotstar.service.impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import vn.iotstar.dto.ProductDTO;
import vn.iotstar.entity.Product;
import vn.iotstar.entity.User;
import vn.iotstar.mapper.ProductMapper;
import vn.iotstar.repository.ProductRepository;
import vn.iotstar.repository.UserRepository;
import vn.iotstar.service.CloudinaryService;
import vn.iotstar.service.CloudinaryUploadResult;
import vn.iotstar.service.ProductService;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private static final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ProductMapper mapper;
    private final CloudinaryService cloudinaryService;

    @Override
    @Transactional(readOnly = true)
    public Page<ProductDTO> findAll(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(
                Math.max(page, 0),
                Math.max(size, 1),
                Sort.by(Sort.Direction.DESC, "id")
        );

        String searchKeyword = keyword == null ? "" : keyword.trim();
        return productRepository.search(searchKeyword, pageable)
                .map(mapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product không tồn tại"));
        return mapper.toDTO(product);
    }

    @Override
    @Transactional
    public ProductDTO create(ProductDTO dto, MultipartFile image) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User không tồn tại"));

        Product product = mapper.toEntity(dto);
        product.setName(dto.getName().trim());
        product.setDescription(dto.getDescription() == null ? null : dto.getDescription().trim());
        product.setUser(user);

        if (image != null && !image.isEmpty()) {
            CloudinaryUploadResult uploadResult = cloudinaryService.upload(image);
            product.setImageUrl(packImage(uploadResult));
        }

        return mapper.toDTO(productRepository.save(product));
    }

    @Override
    @Transactional
    public ProductDTO update(Long id, ProductDTO dto, MultipartFile image) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product không tồn tại"));

        product.setName(dto.getName().trim());
        product.setDescription(dto.getDescription() == null ? null : dto.getDescription().trim());
        product.setPrice(dto.getPrice());

        if (image != null && !image.isEmpty()) {
            String oldImage = product.getImageUrl();
            CloudinaryUploadResult uploadResult = cloudinaryService.upload(image);
            product.setImageUrl(packImage(uploadResult));
            deleteOldImageQuietly(oldImage);
        }

        return mapper.toDTO(productRepository.save(product));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product không tồn tại"));

        String oldImage = product.getImageUrl();
        productRepository.delete(product);
        deleteOldImageQuietly(oldImage);
    }

    private String packImage(CloudinaryUploadResult result) {
        return result.url() + "|" + result.publicId();
    }

    private void deleteOldImageQuietly(String imageValue) {
        if (imageValue == null || !imageValue.contains("|")) {
            return;
        }

        String publicId = imageValue.substring(imageValue.indexOf('|') + 1);
        try {
            cloudinaryService.delete(publicId);
        } catch (RuntimeException e) {
            log.warn("Không thể xóa ảnh cũ trên Cloudinary: {}", publicId, e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public long countProducts() {
        return productRepository.count();
    }

    @Override
    @Transactional(readOnly = true)
    public long countByUser(Long userId) {
        return productRepository.countByUserId(userId);
    }
}
