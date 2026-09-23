# Ví dụ 3 - Spring Boot Security + OTP + CRUD + Cloudinary

Project thực hiện Ví dụ 3 theo tài liệu tuần 14.

## Chức năng

### Authentication

- Register tài khoản.
- Gửi OTP đăng ký qua email.
- Xác nhận OTP và kích hoạt tài khoản.
- Resend OTP đăng ký.
- Login bằng Spring Security Session.
- Logout và hủy session.
- Forgot Password.
- Gửi OTP reset password.
- Verify OTP reset password.
- Đổi mật khẩu bằng BCrypt.

### User

- CRUD User.
- Search theo username, email, họ tên.
- Pagination.
- Role `ROLE_USER` / `ROLE_ADMIN`.
- Đếm tổng User.
- Hiển thị số Product của từng User.

### Product

- CRUD Product.
- Search theo tên/mô tả.
- Pagination.
- Product thuộc User.
- Hiển thị User sở hữu Product.
- Upload ảnh Cloudinary.
- Thay ảnh và xóa ảnh Cloudinary cũ.
- Xóa Product.

## Công nghệ

- Spring Boot 4.1.1.
- Spring Security 7.x.
- JDK 26.
- SQL Server.
- Spring Data JPA / Hibernate.
- Thymeleaf.
- MapStruct 1.6.3.
- Spring Mail.
- Cloudinary.
- Jakarta Validation.
- BCrypt.
- Maven.

## Cấu hình nhanh

1. Chạy `sql/create_database.sql` trong DBeaver/SSMS để tạo database `webst3`.
2. Copy `.env.example` thành `.env`.
3. Sửa `DB_PASSWORD` theo SQL Server trên máy.
4. Dùng JDK 26.
5. Chạy `mvn clean package`.
6. Chạy `mvn spring-boot:run`.
7. Mở `http://localhost:8082`.

## Tài khoản mẫu

- Admin: `admin` / `123456`.
- User: `user01` / `123456`.

`DataInitializer` tự tạo `ROLE_USER`, `ROLE_ADMIN` và hai tài khoản mẫu khi database chưa có dữ liệu tương ứng.

## Test OTP

### Test nhanh bằng console

Giữ:

```text
MAIL_ENABLED=false
```

Khi Register/Forgot Password, OTP sẽ được in ở console Spring Boot.

### Test email thật

Dùng Gmail App Password và cấu hình:

```text
MAIL_ENABLED=true
MAIL_USERNAME=your_email@gmail.com
MAIL_PASSWORD=your_gmail_app_password
```

Sau đó Register bằng một email bạn có thể nhận thư và xác nhận OTP từ email.

## Test Cloudinary

Điền:

```text
CLOUDINARY_CLOUD_NAME=...
CLOUDINARY_API_KEY=...
CLOUDINARY_API_SECRET=...
```

Đăng nhập, tạo Product và chọn một ảnh. Sau khi lưu, ảnh phải hiển thị từ URL Cloudinary.

## Lưu ý GitHub

- Không commit `.env`.
- Không commit `target/`, `.idea/`, `.class`.
- Chỉ commit `.env.example`.

Xem `TESTING.md` để test toàn bộ chức năng theo checklist.
