# Ví dụ 2 - Custom Login bằng username hoặc email

## Nội dung

- Entity `User`, `Role`.
- Login bằng `username` hoặc `email`.
- `CustomUserDetails` + `CustomUserDetailsService`.
- Spring Security + BCrypt.
- MapStruct.
- Thymeleaf Layout Dialect.
- Header hiển thị avatar, fullname, username, email và role.
- Dữ liệu mẫu tự tạo bằng `DataInitializer`.
- SQL Server.

## Chạy project

1. Chạy `sql/create_database.sql` để tạo database `webst9`.
2. Copy `.env.example` thành `.env`.
3. Sửa `DB_PASSWORD` theo SQL Server trên máy.
4. Dùng JDK 26.
5. Chạy `mvn spring-boot:run`.
6. Mở `http://localhost:8081`.

## Tài khoản mẫu

- User: `user01` hoặc `user01@gmail.com` / `123456`
- Admin: `admin01` hoặc `admin01@gmail.com` / `123456`

Không commit file `.env` lên GitHub.
