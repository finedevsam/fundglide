# FundGlide Documentation

## Table of Contents
- [Introduction](#introduction)
- [Features](#features)
- [Getting Started](#getting-started)
- [Support](#support)
- [Conclusion](#conclusion)
- [REST API Documentation](#rest-api-documentation)
    - [Authentication](#authentication)
        - [Admin-Staff Login](#admin-staff-login)
        - [Customer Login](#customer-login)
        - [Logged In Customer](#logged-in-customer)
        - [Logged In Staff](#logged-in-staff)
        - [Update Customer Profile](#update-customer-profile)
        - [Customer Reset Password](#customer-reset-password)
        - [Customer Reset Password Confirm](#customer-reset-password-confirm)
        - [Change Password](#change-password)
    - [Customer](#users)
    - [Register](#register)
    - [Activate QR Payment](#get-user-by-id)
    - [Logged In Customer](#create-user)
    - [Set Transaction Pin](#create-user)
    - [Bulk Payment](#bulk-payment)
        - [Bulk Transfer](#delete-user)
        - [My Bulk Transaction](#delete-user)
        - [View Bulk Details](#delete-user)
    - [Account](#account)
        - [Account Lookup](#delete-user)
        - [Transfer](#transfer)
        - [Validate Channel](#delete-user)
        - [Transaction Logs](#delete-user)
        - [My Account](#my-accounts)
    - [Admin](#admin)
    - [Customer](#admin-customer)
        - [All Customer](#get-user-by-id)
        - [All Transaction](#get-user-by-id)
        - [View Account Transaction](#get-user-by-id)
        - [View Customer Account](#get-user-by-id)
    - [Staff](#create-user)
        - [Create Staff](#update-user)
        - [Update Staff Records](#update-user)
        - [Assign Permission to Staff](#update-user)
        - [Revoke Permission from Staff](#update-user)
        - [All Staff](#update-user)
    - [Department](#delete-user)
        - [Create Department](#update-user)
        - [Update Department](#update-user)
        - [Delete Department](#update-user)
        - [All Department](#update-user)
    - [Permission](#posts)
        - [Create Permission](#get-all-posts)
        - [Update Permission](#get-all-posts)
        - [All Permission](#get-all-posts)
        - [Delete Permission](#get-all-posts)


### Introduction
Welcome to the documentation for our open-source financial application! This application is designed to provide a comprehensive set of features for managing financial transactions, including account tiering, fund transfers, QR payments, intra and inter transactions, and loan booking. This documentation will guide you through the various functionalities and help you understand how to use and contribute to the application.

### Features
Our financial application offers the following key features:

1. **Account Tiering:** The application supports account tiering, allowing users to categorize accounts into different tiers based on predefined criteria. This feature enables personalized account management and customized services for different account types.

2. **Fund Transfer:** Users can initiate fund transfers between their own accounts or to other users. The application supports various transfer methods such as bank transfers, e-wallet transfers, and online payment gateways, ensuring secure and convenient transactions.

3. **QR Payment:** The application integrates QR code payment functionality, enabling users to make payments by scanning QR codes. This feature simplifies the payment process and facilitates seamless transactions between individuals and businesses.

4. **Intra and Inter Transact:** Users can perform both intra- and inter-bank transactions. Intra-transactions allow transfers within the same financial institution, while inter-transactions enable transfers between different banks. The application handles the necessary protocols and security measures to ensure smooth and secure transactions.

5. **Loan Booking:** Users can apply for loans through the application. The loan booking feature provides a streamlined process for loan applications, including eligibility checks, document submission, and approval workflows. It helps users access financial assistance conveniently and efficiently.


### Getting Started

To get started with our financial application, follow these steps:

1. **System Requirements:** Before proceeding, ensure that your system meets the minimum requirements specified in the installation guide. This includes supported operating systems, dependencies, and software prerequisites.

2. **Installation:** Refer to the installation guide for detailed instructions on how to set up the application environment. It covers steps such as downloading the source code, configuring the database, and setting up any additional dependencies.

3. **Configuration:** Once installed, configure the application settings according to your specific requirements. This may include database connection details, API integrations, and security settings. The configuration guide will provide detailed instructions for this process.

4. **Usage and API Documentation:** Explore the user guide and API documentation to understand how to use the various features of the application. The user guide will walk you through the application's user interface, while the API documentation will provide insights into integrating with the application programmatically.

5. **Contributing:** If you're interested in contributing to the application's development, we encourage you to read the contribution guidelines. It outlines the process for submitting bug reports, feature requests, and code contributions. You can also find information about the development workflow, coding standards, and testing guidelines.

### Support

If you encounter any issues, have questions, or need assistance with our financial application, feel free to reach out to our support team. Details on how to contact support can be found in the support documentation.

### Conclusion

Thank you for choosing our open-source financial application. We hope this documentation provides you with a comprehensive understanding of the application's features and functionality. Whether you're a user or a contributor, we appreciate your engagement and welcome your feedback to help improve the application for everyone.

Happy banking and financial management with our open-source financial application!

**The FundGlide Team**


# REST API Documentation

Welcome to the documentation for our REST API. This API provides access to various endpoints for interacting with our system. Use the table of contents below to navigate through the documentation.


## Authentication

- [POST /auth/admin/login](#admin-staff-login): Authenticate Admin/Staff and obtain an access token.

    Authenticate admin/staff and obtain an access token.

    - Request:
    - Method: `POST`
    - Path: `/auth/admin/login`
    - Body:
        ```json
        {
            "email": "admin@fintabsolution.com",
            "password": "12345"
        }
        ```

    - Response:
    - Status: `200 OK`
    - Body:
        ```json
        {
            "profile": {
                "firstName": "System",
                "lastName": "Administrator",
                "imageUrl": null,
                "permission": [
                    "713abf3f-d595-4774-a4e3-5b6c3bf83e96"
                ],
                "department": "9387fc1e-cd58-49ba-823f-bd114c1fabf0",
                "email": "admin@fintabsolution.com"
            },
            "user": {
                "accessToken": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbkBmaW50YWJzb2x1dGlvbi5jb20iLCJleHAiO...."
            }
        }
        ```

- [POST /auth/login](#customer-login): Authenticate customer and obtain an access token. Customer username can either be email or the account number.

    - Request:
    - Method: `POST`
    - Path: `/auth/login`
    - Body:
        ```json
        {
            "username": "admin@fintabsolution.com",
            "password": "12345"
        }
        ```

    - Response:
    - Status: `200 OK`
    - Body:
        ```json
        {
            "profile": {
                "firstName": "Akin",
                "lastName": "Sam",
                "middleName": null,
                "email": "akinsam@gmail.com"
            },
            "user": {
                "accessToken": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJha2lu..........."
            }
        }
        ```


## Users

### Logged In Customer

- [GET /auth/me](#logged-in-customer-details): Retrieve authenticated customer.

    - Request:
    - Method: `GET`
    - Headers: 
        - Authorization: `{{Bearer token}}`
    - Path: `/auth/me`

    - Response:
    - Status: `200 OK`
    - Body:
        ```json
        {
            "firstName": "Akin",
            "isCustomer": true,
            "lastName": "Sam",
            "address": null,
            "modeOfId": null,
            "middleName": null,
            "idData": null,
            "profileImage": null,
            "accounts": {
                "accountNo": "3000000000",
                "balance": "9980000.0",
                "lockBalance": "0",
                "tier": "tier1",
                "code": "000",
                "active": true,
                "isQr": true,
                "qrodeUrl": ""
            },
            "email": "akinsam@gmail.com",
            "dateRegistered": "2023-06-28T00:58:54.724+00:00"
        }
        ```

- [GET /auth/admin/isme](#logged-in-admin-details): Retrieve authenticated admin/staff.

    - Request:
    - Method: `GET`
    - Headers: 
        - Authorization: `{{Bearer token}}`
    - Path: `/auth/admin/isme`

    - Response:
    - Status: `200 OK`
    - Body:
        ```json
        {
            "profile": {
                "firstName": "System",
                "lastName": "Administrator",
                "imageUrl": null,
                "permission": [
                    "ddc42b94-3e86-4396-8217-bdcf60e64e37"
                ],
                "department": "Admin",
                "email": "admin@fintabsolution.com"
            }
        }
        ```

### Update Customer Profile

- [GET /auth/profile](#update-customer-profile-details): Update customer profile.

    - Request:
    - Method: `PUT`
    - Headers: 
        - Authorization: `{{Bearer token}}`
        - `Content-Type: multipart/form-data`
    - Path: `/auth/profile`
    - Body:
        ```json
            --boundary

                --boundary
                Content-Disposition: form-data; name="idData"; filename="profile.jpg"
                Content-Type: image/jpeg

                --boundary
                Content-Disposition: form-data; name="modeOfId"
                passport

                --boundary
                Content-Disposition: form-data; name="address"

                Lagos Nigeria

                --boundary
                Content-Disposition: form-data; name="firstName"
                Firstname

                --boundary
                Content-Disposition: form-data; name="lastName"
                Lastname
                
                --boundary
                Content-Disposition: form-data; name="middleName"
                Middlename

                --boundary
                Content-Disposition: form-data; name="mobileNumber"
                mobileNumber

                --boundary  
                Content-Disposition: form-data; name="profileImage"; filename="profile.jpg"
                Content-Type: image/jpeg

                [Binary Image Data]
                --boundary--
        ```

    - Response:
    - Status: `200 OK`
    - Body:
        ```json
        {
            "code": 200,
            "message": "Profile updated successfully",
            "status": "successful"
        }
        ```


### Customer Password Reset

- [POST /auth/reset/password](#customer-reset-password-details): Customer Initiate password reset.

    Customer reset password can take either the customer account number or email as ``username`` value.

    - Request:
    - Method: `POST`
    - Path: `auth/reset/password`
    - Body:
        ```json
        {
            "username": "3000000000"
        }
        ```

    - Response:
    - Status: `200 OK`
    - Body:
        ```json
        {
            "reference": "sjyirzbw4crr",
            "message": "Your code has been sent to your register email and mobile number"
        }
        ```
- [POST /auth/reset/confirm](#customer-reset-password-confirm-details): Customer confirm the password reset with the code sent to the registered email.

- Request:
  - Method: `POST`
  - Path: `/auth/reset/confirm`
  - Body:
    ```json
    {
        "reference": "sjyirzbw4crr",
        "code": "396258",
        "newPassword": "123456",
        "confirmPassword": "123456"
    }
    ```

- Response:
  - Status: `200 OK`
  - Body:
    ```json
    {
        "code": 200,
        "message": "Password reset successfully",
        "status": "successful"
    }
    ```

### Customer Change Password

- [PUT /auth/change_password](#change-password-details): Change user password.

    - Request:
    - Method: `POST`
    - Path: `/auth/change_password`
    - Headers: 
        - Authorization: `{{Bearer token}}`
    - Body:
        ```json
        {
            "oldPassword": "12345",
            "newPassword": "123456",
            "confirmPassword": "123456"
        }
        ```

    - Response:
    - Status: `200 OK`
    - Body:
        ```json
        {
            "code": 200,
            "message": "password change successfully",
            "status": "successful"
        }
        ```
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



... and so on for each endpoint.

