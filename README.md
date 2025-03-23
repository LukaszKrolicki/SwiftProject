# SwiftCodesProject

SwiftCodesProject to aplikacja napisana w **Spring Boot**, która korzysta z **PostgreSQL** jako bazy danych.  
Projekt jest w pełni skonteneryzowany.  

## 📦 Wymagania  
Przed uruchomieniem upewnij się, że masz zainstalowane:  
- [Docker](https://www.docker.com/get-started) 

## 🚀 Jak uruchomić aplikację?
1. **Sklonuj repozytorium**  
 ```sh
 git clone https://github.com/twoj-username/swiftcodesproject.git
 cd swiftcodesproject
 Uruchom aplikację za pomocą Docker Compose
 ```

2. **Uruchom aplikację za pomocą Docker Compose**  
```sh
docker-compose up --build
```
To polecenie zbuduje i uruchomi aplikację Spring Boot oraz bazę danych PostgreSQL.

Aplikacja będzie dostępna pod adresem http://localhost:8080.

PostgreSQL działa na porcie 5430 (przekierowanym z 5432).

po poprawnym uruchomieniu powinno to wyglądać w następujący sposób:

![image](https://github.com/user-attachments/assets/fb9376bf-dabe-494a-b1fc-e09dbd24c2c2)


🏗 **Technologie**

-Spring Boot – Backend

-PostgreSQL – Baza danych

-Docker – Konteneryzacja


# **Struktura**

![image](https://github.com/user-attachments/assets/9415e98c-68ec-4ef8-b3b3-d28e9b800f6e)

📂 config/

➡ Konfiguracja aplikacji – zawiera klasy ładujące dane z csv do bazy danych oraz ustawiające mappery

📂 controllers/

➡ Warstwa kontrolerów – obsługuje żądania HTTP i przekazuje je do serwisów.

📂 domain/

➡ Modele danych (encje) – klasy reprezentujące tabele w bazie danych oraz DTO.

📂 exceptions/

➡ Obsługa wyjątków – niestandardowe wyjątki i ich obsługa w aplikacji.

📂 mappers/

➡ Konwersja encji do DTO – mapowanie obiektów domenowych na DTO i odwrotnie.

📂 repositories/

➡ Warstwa dostępu do danych (DAO) – interfejsy do komunikacji z bazą danych przy użyciu Spring Data JPA.

📂 services/

➡ Logika biznesowa aplikacji – operacje na danych i komunikacja z repozytoriami.

# **Analiza danych, walidacja oraz wczytywanie z csv do postgre**

Wstępna analiza danych wykazała:

![image](https://github.com/user-attachments/assets/df3ec7ed-a621-4511-9a03-b9d9cd2385f4)

Klasa config/Dataloader wczytuje dane i przeprowadza ich walidacje za pomocą klasy services/SwiftCodeLoadService. Dodatkowo przeprowadza test czy liczba wgranych wierszy / oddziałów itp się zgadza.
Na początku wgrywane są bazy typu "Headquarters" , aby następnie można było dokonać powiązania dla branches

# **Endpointy**

Endpointy zdefiniowane są w /controllers/SwiftCodesController, korzystają z /services/SwiftCodeService

**Get Swift-Code details**

Dla "branch":

![image](https://github.com/user-attachments/assets/8817b779-a317-458d-b541-8b94378dde8d)

Dla "headquarters" dołączane są również jego odziały: 

![image](https://github.com/user-attachments/assets/9bb99768-36b0-4c18-b72b-cc9a36f8d1aa)

**Get Swift-Codes by Country**

![image](https://github.com/user-attachments/assets/651b0035-2804-492c-8886-f102440af8df)

**Add Swift-Code**

W metodzie dodającej Swift-codes zachodzi kilka etapów walidacji:

- Metoda sprawdza, czy dany kod SWIFT już istnieje w bazie.

- Metoda sprawdza, czy kod SWIFT ma dokładnie 11 znaków
  
- Pobierane są ostatnie 3 znaki kodu SWIFT i konwertowane na wielkie litery. Jeśli to "XXX", to oznacza, że jest to główna siedziba banku (isHeadquarter = true).

- Jeśli którekolwiek z pól (address, bankName, countryISO2, countryName) są puste, rzuca wyjątek.

- Kody krajów są konwertowane na wielkie litery, by zachować spójność.

**Dodawanie "headquarter"**: 

![image](https://github.com/user-attachments/assets/59156032-a59c-4d03-b212-9bc88ce4ada5)


**Dodawanie "branch:**:

![image](https://github.com/user-attachments/assets/3a067f84-4b64-4f94-93fe-9f700403cf83)

Wynik:

![image](https://github.com/user-attachments/assets/bfff6721-6e20-45a7-9c23-f7e7145662dd)


**Usuwanie SWIFT-CODE:**

usunięcie swift-code, aktualizuje również wartości jego odziałów na null

![image](https://github.com/user-attachments/assets/7513e52a-3b37-41bf-9422-56d220e635eb)

Wynik:

![image](https://github.com/user-attachments/assets/0bf9a9a7-b9f9-4582-8fb6-b66f2d5f2805)


### Projekt zawiera również testy jednostkowe oraz integracyjne

![image](https://github.com/user-attachments/assets/813e5506-7cfa-4163-bcca-24caf716ab8a)


![image](https://github.com/user-attachments/assets/d18fad8e-1540-47ff-a080-67a82a3106ef)









