# Checklist test Ví dụ 3

## 1. Môi trường

```powershell
java -version
javac -version
mvn -version
```

Cả Java/Javac/Maven phải dùng JDK 26.

## 2. Database

Chạy `sql/create_database.sql` và xác nhận database `webst3` tồn tại.

Sau khi app chạy, kiểm tra:

```sql
USE webst3;
SELECT * FROM roles;
SELECT * FROM users;
SELECT * FROM products;
SELECT * FROM otp_tokens;
```

## 3. Build

```powershell
mvn clean package
```

Kết quả phải là `BUILD SUCCESS`.

## 4. Run

```powershell
mvn spring-boot:run
```

Mở `http://localhost:8082`.

## 5. Security / Session

- Login `user01 / 123456` thành công.
- USER vào `/products` được.
- USER vào `/users` phải bị từ chối.
- Logout thành công và session bị hủy.
- Login `admin / 123456` thành công.
- ADMIN vào `/users` được.

## 6. Register + OTP

- Register username/email mới.
- User mới phải có `enabled = false` trước khi verify OTP.
- OTP được gửi email khi `MAIL_ENABLED=true`, hoặc in console khi `MAIL_ENABLED=false`.
- Nhập sai OTP phải báo lỗi.
- Resend OTP tạo OTP mới.
- Nhập OTP đúng thì user được kích hoạt.
- Login user vừa đăng ký thành công.

## 7. Forgot Password

- Chọn Quên mật khẩu.
- Nhập email tồn tại.
- Nhận OTP reset.
- Nhập OTP + mật khẩu mới + confirm.
- Login bằng mật khẩu cũ phải thất bại.
- Login bằng mật khẩu mới phải thành công.

## 8. CRUD User (ADMIN)

- Tạo User mới.
- Search User.
- Pagination User.
- Edit username/email/fullName/role/enabled.
- Product count hiển thị đúng.
- User có Product thì không cho xóa cho tới khi xóa Product.

## 9. CRUD Product

- Tạo Product không ảnh.
- Tạo Product có ảnh Cloudinary.
- Search Product.
- Pagination Product.
- Edit Product.
- Thay ảnh Cloudinary.
- Xóa Product.
- Product hiển thị username của User sở hữu.

## 10. Git trước khi nộp

```powershell
git status
git ls-files | findstr /i ".env"
```

Chỉ được có `.env.example`, không được có `.env` thật.
