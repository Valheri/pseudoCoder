# PseudoCoder

PseudoCoder is a web application designed to help users manage and organize pseudo-code projects. It allows users to create, view, edit, and delete pseudo-codes, categories, and pseudo-blocks. The application is built using a React frontend and a Spring Boot backend.

## Author

- **Valheri**  
  [GitHub Profile](https://github.com/Valheri)  

## Features

- **User Authentication**: Secure login functionality using Spring Security.
- **PseudoCode Management**: Create, view, edit, and delete pseudo-codes.
- **Category Management**: Organize pseudo-codes into categories with customizable colors.
- **PseudoBlock Management**: Add, edit, and delete pseudo-blocks within categories.
- **Export to JSON**: Export pseudo-code data to JSON format for easy sharing or backup.
- **Responsive Design**: Optimized for both desktop and mobile devices.

## Tech Stack

### Frontend
- **React**: For building the user interface.
- **TypeScript**: For type safety and better developer experience.
- **Vite**: For fast development and build tooling.
- **TailwindCSS**: For styling the application.
- **Axios**: For making HTTP requests to the backend.
- **React Router**: For client-side routing.

### Backend
- **Spring Boot**: For building the RESTful API.
- **Spring Security**: For authentication and authorization.
- **JPA/Hibernate**: For database interaction.
- **H2 Database**: For development and testing (can be replaced with other databases like MySQL or PostgreSQL).
- **Jackson**: For JSON serialization and deserialization.
- **MySQL/JawsDB**: used in deploy-heroku branch




### API Endpoints
Authentication
POST /api/login: Authenticate a user.
PseudoCodes
GET /api/pseudoCodes: Retrieve all pseudo-codes.
POST /api/pseudoCodes: Create a new pseudo-code.
GET /api/pseudoCodes/{id}: Retrieve a pseudo-code by ID.
DELETE /api/pseudoCodes/{id}: Delete a pseudo-code.
Categories
POST /api/pseudoCodes/{id}/categories: Create a category for a pseudo-code.
DELETE /api/categories/{id}: Delete a category.
PseudoBlocks
POST /api/pseudoCodes/{id}/addPseudoBlock: Add a pseudo-block to a category.
PUT /api/pseudoBlocks/{id}: Edit a pseudo-block.
DELETE /api/pseudoBlocks/{id}: Delete a pseudo-block.


License
This project is licensed under the MIT License. See the LICENSE file for details.
