# TP IngSoft 1 Grupo 13 Curso Montaldo - Facultad de Ingeniería UBA

- TP 1 **E-Commerce**

- [Ver Consigna TP](consigna_TP.pdf)

- El trabajo se realizo durante un mes y medio en grupos de 6 personas. A medida que se avanzaba en el proyecto se iban modificando los requerimientos finales según tiempos y avance de los mismos.

- Repositorio (Privado) donde se realizo el trabajo:
    - https://gitlab.com/dmarianetti/ingsoft-02montaldo-tp-grupo13
    -       
    - ![image](https://github.com/user-attachments/assets/396730fc-fe17-42c9-a90d-bb66f8adb8d0)


## Integrantes

| Nombre | Padrón |
| ------ | ------ |
| Ezequiel Lazarte | 108063 |
| Daniel Agustin Marianetti | 106256 |
| Santiago Testa | 108301 | 
| Luna Dufour | 110438 |
| Ignacio Agustín Fernández | 111019 |
| Lucia Almanza | 110598 |

# Demo

## 1. Clonación
```bash
git clone git@github.com:ezelzrt/E-Commerce.git
cd E-Commerce
```
## 2. Puesta en marcha

### 2.1. Programa completo (front y back)
a. Levantar servicios
```
docker compose up --build
```
b. Insertar la info hardcodeada para la demo

- b.1. Ingresar a http://localhost:20003/ con las credenciales root : root

- b.2. Dirigirse al tab SQL

- b.3. Pegar el contenido de [db_initial_info.sql](backend\db_initial_info.sql) y ejecutar la query

### 2.2. Solo backend 
a. Levantar SOLO servicios del backend
```
docker compose up --scale frontend=0
```
> No ejecuta el contenedor correspondiente al frontend

b. Realizar punto **b** del paso anterior para insertar datos iniciales

c. (Opcional) Puesta en marcha del frontend localmente

```bash
cd frontend
npm i --force
npm run start
```

## 3. Uso

Existen dos tipos de usuario: 
- `Admin:` Usuario con acceso a las interfaces **/secure**. Puede dar de alta atributos, productos, controlar stock, etc. No puede darse de alta mediante interfaz.

    Credenciales de un usuario admin existente: 

    - **Email:** a@a

    - **Contraseña:** 123


- `Customer:` Usuario con restricciones. No puede acceder a las interfaces **/secure**. Puede registrarse en /register.


    Credenciales de un usuario customer existente: 

    - **Email:** q@q

    - **Contraseña:** 123
