**Compara prețuri, identifică reduceri și recomandă cele mai bune opțiuni de cumpărături din Lidl, Kaufland și Profi.**
---
## Funcționalități implementate

- Căutare produse după nume
- Optimizare coș de cumpărături (cel mai ieftin per produs)
- Istoric prețuri (pentru grafic)
- Recomandări bazate pe `valoare/unitate`
- Cele mai bune reduceri pentru o zi dată
- Reduceri adăugate în ultimele 24h
- Alarme de preț personalizate

---

## Tehnologii utilizate

- Java 17
- Spring Boot
- Spring Data JPA
- PostgreSQL
- Maven
- OpenCSV

---

## Structura fișierelor .csv

### Exemplu `lidl_2025-05-10.csv`:
```csv
product_id;product_name;product_category;brand;package_quantity;package_unit;price;currency
P001;lapte zuzu;lactate;Zuzu;1;l;9.99;RON
```

### Exemplu `lidl_discounts_2025-05-10.csv`:
```csv
product_id;product_name;brand;package_quantity;package_unit;product_category;from_date;to_date;percentage_of_discount
P001;lapte zuzu;Zuzu;1;l;lactate;2025-05-10;2025-05-14;10
```

---

## Endpointuri API

### Import fișiere

- **POST** `/products/import-csv?file=filename.csv`
    - Importă fișiere CSV cu produse sau reduceri.

### Optimizare coș de cumpărături

- **POST** `/products/optimized-by-name`
    - Primește: `productNames` (listă de nume) și `date`
    - Returnează cel mai ieftin produs per nume
      - ex: 
      {
        "productNames": ["lapte", "paine", "unt"],
        "date": "2025-05-08"
        }

### Istoric prețuri

- **GET** `/products/history?productId=P001[&store=...][&brand=...][&category=...]`
    - Returnează lista de prețuri în timp pentru produsul dat
    - ex: /products/history?productId=P001&store=Lidl

### Recomandări (value per unit)

- **GET** `/products/best-buy?name=lapte&date=2025-05-10`
    - Returnează lista de produse sortate după valoarea per unitate
    - ex: /products/best-buy?name=lapte&date=2025-05-08

### Cele mai bune reduceri

- **GE** `/discounts/best?date=2025-05-10`
    - Returnează top reduceri active la data respectivă
    - ex: /discounts/best?date=2025-05-08

### Reduceri nou adăugate

- **GET** `/discounts/new?date=2025-05-10`
    - Returnează reduceri care au `from_date` în ultimele 24h față de `date`
    - ex: /discounts/new?date=2025-05-08

### Alarme de preț

- **POST** `/products/create?productId=...&store=...&targetPrice=...`
    - Creează o alertă de preț personalizată
    - ex: /products/create?productId=P001&store=Lidl&targetPrice=8.50

- **GET** `/products/check?date=2025-05-10`
    - Verifică dacă alertele sunt declanșate pentru o zi
    - ex: /products/check?date=2025-05-10

---