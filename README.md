# GreenRide – Carpooling Platform

## Περιγραφή
Το GreenRide είναι μια web εφαρμογή carpooling που επιτρέπει σε χρήστες να
δημιουργούν και να μοιράζονται διαδρομές, μειώνοντας το κόστος και το
περιβαλλοντικό αποτύπωμα των μετακινήσεων.

## Τεχνολογίες
- Java 17
- Spring Boot
- Spring Security
- Spring Data JPA
- H2 Database
- Thymeleaf
- REST API (JSON)
- JWT Authentication
- Swagger / OpenAPI

## Ρόλοι Χρηστών
- USER: δημιουργία & κράτηση διαδρομών
- ADMIN: διαχείριση χρηστών & στατιστικά

## Εκτέλεση Εφαρμογής

### Προαπαιτούμενα
- Java 17+
- Maven

### Build
```bash
mvn clean package

## Εκτέλεση
mvn spring-boot:run

## URLs
Web UI: http://localhost:8080
Swagger UI: http://localhost:8080/swagger-ui.html

## Authentication
Web UI: session-based login
REST API: JWT tokens

##Εξωτερική Υπηρεσία
Η εφαρμογή χρησιμοποιεί OpenWeather API για την εμφάνιση καιρού
κατά την ημέρα της διαδρομής.

Σε περίπτωση αποτυχίας της εξωτερικής υπηρεσίας, η εφαρμογή
συνεχίζει να λειτουργεί κανονικά.

