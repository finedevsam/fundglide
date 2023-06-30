# FundGlide Documentation

### Introduction
Welcome to the documentation for our open-source financial application! This application is designed to provide a comprehensive set of features for managing financial transactions, including account tiering, fund transfers, QR payments, intra and inter transactions, and loan booking. This documentation will guide you through the various functionalities and help you understand how to use and contribute to the application.

### Features
Our financial application offers the following key features:

1. ``Account Tiering:`` The application supports account tiering, allowing users to categorize accounts into different tiers based on predefined criteria. This feature enables personalized account management and customized services for different account types.

2. ``Fund Transfer:`` Users can initiate fund transfers between their own accounts or to other users. The application supports various transfer methods such as bank transfers, e-wallet transfers, and online payment gateways, ensuring secure and convenient transactions.

3. ``QR Payment:`` The application integrates QR code payment functionality, enabling users to make payments by scanning QR codes. This feature simplifies the payment process and facilitates seamless transactions between individuals and businesses.

4. ``Intra and Inter Transact:`` Users can perform both intra- and inter-bank transactions. Intra-transactions allow transfers within the same financial institution, while inter-transactions enable transfers between different banks. The application handles the necessary protocols and security measures to ensure smooth and secure transactions.

5. ``Loan Booking:`` Users can apply for loans through the application. The loan booking feature provides a streamlined process for loan applications, including eligibility checks, document submission, and approval workflows. It helps users access financial assistance conveniently and efficiently.


### Getting Started

To get started with our financial application, follow these steps:

1. ``System Requirements:`` Before proceeding, ensure that your system meets the minimum requirements specified in the installation guide. This includes supported operating systems, dependencies, and software prerequisites.

2. ``Installation:`` Refer to the installation guide for detailed instructions on how to set up the application environment. It covers steps such as downloading the source code, configuring the database, and setting up any additional dependencies.

3. ``Configuration:`` Once installed, configure the application settings according to your specific requirements. This may include database connection details, API integrations, and security settings. The configuration guide will provide detailed instructions for this process.

4. ``Usage and API Documentation:`` Explore the user guide and API documentation to understand how to use the various features of the application. The user guide will walk you through the application's user interface, while the API documentation will provide insights into integrating with the application programmatically.

5. ``Contributing:`` If you're interested in contributing to the application's development, we encourage you to read the contribution guidelines. It outlines the process for submitting bug reports, feature requests, and code contributions. You can also find information about the development workflow, coding standards, and testing guidelines.

### Support

If you encounter any issues, have questions, or need assistance with our financial application, feel free to reach out to our support team. Details on how to contact support can be found in the support documentation.

### Conclusion

Thank you for choosing our open-source financial application. We hope this documentation provides you with a comprehensive understanding of the application's features and functionality. Whether you're a user or a contributor, we appreciate your engagement and welcome your feedback to help improve the application for everyone.

Happy banking and financial management with our open-source financial application!

**The FundGlide Team**


# REST API Documentation

Welcome to the documentation for our REST API. This API provides access to various endpoints for interacting with our system. Use the table of contents below to navigate through the documentation.

## Table of Contents

- [Authentication](#authentication)
- [Users](#users)
  - [Get All Users](#get-all-users)
  - [Get User by ID](#get-user-by-id)
  - [Create User](#create-user)
  - [Update User](#update-user)
  - [Delete User](#delete-user)
- [Posts](#posts)
  - [Get All Posts](#get-all-posts)
  - [Get Post by ID](#get-post-by-id)
  - [Create Post](#create-post)
  - [Update Post](#update-post)
  - [Delete Post](#delete-post)

## Authentication

- [POST /auth/login](#post-authlogin): Authenticate a user and obtain an access token.

## Users

### Get All Users

- [GET /users](#get-users): Retrieve a list of all users.

### Get User by ID

- [GET /users/{id}](#get-usersid): Retrieve a specific user by ID.

### Create User

- [POST /users](#post-users): Create a new user.

### Update User

- [PUT /users/{id}](#put-usersid): Update an existing user.

### Delete User

- [DELETE /users/{id}](#delete-usersid): Delete a user.

## Posts

### Get All Posts

- [GET /posts](#get-posts): Retrieve a list of all posts.

### Get Post by ID

- [GET /posts/{id}](#get-postsid): Retrieve a specific post by ID.

### Create Post

- [POST /posts](#post-posts): Create a new post.

### Update Post

- [PUT /posts/{id}](#put-postsid): Update an existing post.

### Delete Post

- [DELETE /posts/{id}](#delete-postsid): Delete a post.

---

## Endpoint Details

### POST /auth/login

Authenticate a user and obtain an access token.

- Request:
  - Method: `POST`
  - Path: `/auth/login`
  - Body:
    ```json
    {
      "username": "user@example.com",
      "password": "secretpassword"
    }
    ```

- Response:
  - Status: `200 OK`
  - Body:
    ```json
    {
      "access_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
    }
    ```

### GET /users

Retrieve a list of all users.

- Request:
  - Method: `GET`
  - Path: `/users`

- Response:
  - Status: `200 OK`
  - Body:
    ```json
    [
      {
        "id": 1,
        "name": "John Doe",
        "email": "johndoe@example.com"
      },
      {
        "id": 2,
        "name": "Jane Smith",
        "email": "janesmith@example.com"
      }
    ]
    ```

... and so on for each endpoint.

