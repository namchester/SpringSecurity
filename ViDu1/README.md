# Ví dụ 1 - Login bằng Spring Security

## Nội dung

- Entity `User`, `Role`.
- Đăng nhập bằng email.
- Spring Security + session + BCrypt.
- Phân quyền USER/ADMIN.
- MapStruct DTO/Entity.
- Thymeleaf fragment/layout, không dùng Layout Dialect.
- Thông tin tài khoản hiển thị ở `header.html`.
- SQL Server.

## Chạy project

1. Chạy `sql/create_database.sql` để tạo database `webst4`.
2. Copy `.env.example` thành `.env`.
3. Sửa `DB_PASSWORD` theo SQL Server trên máy.
4. Dùng JDK 26.
5. Chạy `mvn spring-boot:run`.
6. Mở `http://localhost:8080`.

## Tài khoản mẫu

- User: `user@ute.edu.vn` / `123456`
- Admin: `admin@ute.edu.vn` / `123456`

Không commit file `.env` lên GitHub.
