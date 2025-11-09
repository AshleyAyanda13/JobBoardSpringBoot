Web service URL: https://jobboard-springboot.onrender.com/

This Job Board System is a full-stack platform designed to streamline recruitment and job applications through a secure, scalable architecture. 
The backend is developed using Spring Boot (Java 21) and PostgreSQL, featuring JWT-based authentication and role-based access control (RBAC) for job seekers, recruiters, and administrators. 

Users can register and log in according to their roles, with job seekers able to manage their education history, work experience, and upload CVs, while recruiters can post and manage vacancies.
Admins have full oversight of users and job listings.
The system includes RESTful APIs for user management, vacancy CRUD operations, and keyword-based search functionality.
CV files can be uploaded, viewed, and deleted securely, and an admin user is seeded automatically on startup for immediate access.
Vacancy management is fully implemented with role-specific permissions: recruiters and admins can create, update, and delete job listings, while job seekers can view and apply.
The search module is built using Spring Data JPA Specifications, allowing users to filter vacancies by keyword, location, category, salary range, and posted date. 
These filters are dynamically composed based on user input, enabling precise and flexible search capabilities across the platform. 
The backend is containerized using Docker, with PostgreSQL running in a separate container, and deployed to Render for live access.
CORS is configured to support integration with the Angular-based frontend.
Future enhancements include integrating Apache OpenNLP for natural language processing. 
This will enable intelligent resume parsing, keyword extraction. 
I also want a add email intergration for alerts
