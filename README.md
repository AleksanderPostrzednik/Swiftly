# Swiftly

Aplikacja REST API do zarządzania kodami SWIFT.

## Jak uruchomić projekt lokalnie

1. Sklonuj repozytorium: git clone https://github.com/AleksanderPostrzednik/Swiftly.git
2. Wejdź do katalogu projektu: cd Swiftly
3. Uruchom aplikację: mvn spring-boot:run

## Uruchomienie przy użyciu Docker

1. Upewnij się, że Docker i Docker Compose są zainstalowane.
2. Uruchom polecenie: docker-compose up -d
3. Aplikacja będzie dostępna na [http://localhost:8080](http://localhost:8080).

## Testowanie endpointów

### Przykład użycia Postmana

- **GET /v1/swift-codes/{swiftCode}**
- Ustaw metodę GET i adres: `http://localhost:8080/v1/swift-codes/TESTPL01`

- **POST /v1/swift-codes**
- Ustaw metodę POST, adres: `http://localhost:8080/v1/swift-codes`
- W zakładce Body wybierz raw JSON i wpisz przykładowy JSON:
 ```json
 {
   "swiftCode": "TESTPL02",
   "bankName": "Test Bank",
   "countryISO2": "PL",
   "countryName": "POLAND",
   "address": "Test Address",
   "isHeadquarter": true
 }
 ```
- **DELETE /v1/swift-codes/TESTPL02**
- Ustaw metodę DELETE i adres: `http://localhost:8080/v1/swift-codes/TESTPL02`


