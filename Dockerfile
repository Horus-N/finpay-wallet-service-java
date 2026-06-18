# Giai đoạn 1: Biên dịch code (Build Stage)
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app
# Sao chép file cấu hình pom.xml và toàn bộ mã nguồn vào container
COPY pom.xml .
COPY src ./src
# Thực hiện biên dịch mã nguồn, bỏ qua bước chạy thử test để build nhanh hơn
RUN mvn clean package -DskipTests

# Giai đoạn 2: Tạo môi trường chạy sản phẩm (Run Stage)
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
# Chỉ bốc duy nhất file .jar đã compile từ giai đoạn 1 sang
COPY --from=build /app/target/*.jar app.jar
# Lệnh kích hoạt chạy ứng dụng Spring Boot 4.x
ENTRYPOINT ["java", "-jar", "app.jar"]