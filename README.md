# Geology
#Spring #Java #Hibernate

## TODO:
Authentication is not realized yet.\
The next article will be used:\
https://auth0.com/blog/implementing-jwt-authentication-on-spring-boot/

## API
### Sections
 - `GET /sections` - returns all sections
 - `GET /sections/{name}` - returns section with the specified name
 - `GET /sections/by-code/{classCode}` - returns a list of all sections that have geo-classes with the specified code
 - `POST /sections [body { name: "string"}]` - create section with the specified name
 - `PUT /sections/{name}/update-name  [body { newName: "string"}]` - returns section with the updated name
 - `POST /sections/{name}/add-geo-class [body { className: "string"}]` - returns section with the added geo-class
 - `DELETE /sections/{name}` - delete section with the specified name
### Geological classes
 - `GET /geo-classes` - returns all geo-classes
 - `GET /geo-classes/name/{name}` - returns geo-class with the specified name
 - `GET /geo-classes/code/{code}` - returns geo-class with the specified code
 - `POST /geo-classes [body { name: "string", code: "string"}]` - create geo-class with the specified name and code
 - `PUT /geo-classes/{name}/update-name [body { newName: "string"}]` - returns geo-class with the updated name
 - `PUT /geo-classes/{code}/update-code [body { newCode: "string"}]` - returns geo-class with the updated code
 - `DELETE /geo-classes/{name}` - delete geo-class with the specified name
### Excel
 - `POST /files/import` - returns ID of the Async Job and launches importing.
 - `GET /files/import/{id}` - returns result of importing by Job ID ("DONE", "IN PROGRESS", "ERROR")
 - `GET /files/export` - returns ID of the Async Job and launches exporting.
 - `GET /files/export/{id}` - returns result of parsed file by Job ID ("DONE", "IN PROGRESS", "ERROR")
 - `GET /files/export/{id}/file` - returns a file by Job ID (return appropriate HTTP code in case in process/error)
