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
        - [Activate QR Payment](#activate-qr-payment)
        - [Set Transaction Pin](#set-transaction-pin)
        - [All Bulk Payment](#all-bulk-payment)
        - [Bulk Transfer](#bulk-transfer)
        - [View Bulk Details](#view-bulk-details)
    - [Account](#account)
        - [Account Lookup](#account-lookup)
        - [Transfer](#transfer)
        - [Validate Channel](#validate-channel)
        - [Transaction Logs](#transaction-logs)
        - [My Account](#my-accounts)
    - [Admin](#admin)
        - [Customer](#customer)
            - [All Customer](#all-customer)
            - [All Transaction](#all-transaction)
            - [View Account Transaction](#view-account-transaction)
            - [View Customer Account](#view-customer-account)
        - [Staff](#staff)
            - [Create Staff](#create-staff)
            - [Update Staff Record](#update-staff-record)
            - [Assign Permission to Staff](#assign-permission-to-staff)
            - [Revoke Permission from Staff](#revoke-permission-from-staff)
            - [All Staff](#all-staff)
        - [Department](#department)
            - [Create Department](#create-department)
            - [Update Department](#update-department)
            - [Delete Department](#delete-department)
            - [All Department](#all-department)
        - [Permission](#permission)
            - [Create Permission](#create-permission)
            - [Update Permission](#update-permission)
            - [All Permission](#all-permission)
            - [Delete Permission](#delete-permission)
        - [Loan](#loan)
            - [Create Loan Type](#create-loan-type)
            - [Update Loan Type](#update-loan-type)
            - [All Loan Type](#all-loan-type)


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
        - Content-Type: `multipart/form-data`
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

- [POST /auth/change_password](#change-password-details): Change user password.

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
### Register

- [POST /auth/register](#register): Customer Registration
    - Request:
    - Method: `POST`
    - Path: `/auth/register`
    - Body:
    ```json
        {
            "firstName": "Akin",
            "lastName": "Sam",
            "email": "akinsam@gmail.com",
            "phoneNumber": "080223014---",
            "password": "12345"
        }
    ```

    - Response:
    - Status: `200 OK`
    - Body:
        ```json
        {
            "code": 200,
            "message": "successfully",
            "status": "successful"
        }
        ```

### Activate QR Code
- [GET /auth/activate/qr](#activate-qr-payment): Customer activate QR Payment


    - Request:
    - Method: `POST`
    - Path: `/auth/activate/qr`
    - Headers: 
        - Authorization: `{{Bearer token}}`

    - Response:
    - Status: `200 OK`
    - Body:
    ```json
    {
        "code": 200,
        "message": "QR Code Activate Successful",
        "status": "successful"
    }
    ```

### Set Transaction Pin

- [POST /account/set-pin](#set-transaction-pin): Customer set transaction pin.
    - Request:
    - Method: `POST`
    - Path: `/account/set-pin`
    - Headers: 
        - Authorization: `{{Bearer token}}`
    - Body:
        ```json
        {
            "pin": "1792",
            "password": "12345"
        }
        ```

    - Response:
    - Status: `200 OK`
    - Body:
        ```json
        {
            "code": 200,
            "message": "Transaction Pin set successfully",
            "status": "successful"
        }
        ```

### Bulk Transfer

- [POST /account/payment/bulk}](#bulk-transfer): Bulk payment that allow staff to pay instant bulk payment or future payment.
    - Request:
    - Method: `POST`
    - Headers: 
        - Authorization: `{{Bearer token}}`
        - Content-Type: `multipart/form-data`
    - Path: `/account/payment/bulk`
    - Body:
        ```json
            --boundary

                --boundary
                Content-Disposition: form-data; name="file"; filename="payment.xlsx"
                Content-Type: file/jpeg

                --boundary
                Content-Disposition: form-data; name="paymentType"
                instant|schedule

                --boundary
                Content-Disposition: form-data; name="transactionPin"

                transactionPin

                --boundary
                Content-Disposition: form-data; name="description"
                description

                --boundary
                Content-Disposition: form-data; name="date"
                2023-06-04 22:42

                [Binary Image Data]
                --boundary--
        ```

    - Response:
    - Status: `200 OK`
    - Body:
        ```json
        {
            "code": 200,
            "message": "Payment successfull",
            "status": "successful"
        }
        ```

### All Bulk Payment

- [GET /account/payment/bulk](#all-bulk-payment): List all Customer Bulk Payment.


    - Request:
    - Method: `GET`
    - Path: `/account/payment/bulk`
    - Headers: 
        - Authorization: `{{Bearer token}}`

    - Response:
    - Status: `200 OK`
    - Body:
    ```json
    [
        {
            "list all the bulk payment"
        }
    ]
    ```

### Update Post

- [GET /account/payment/bulk/{id}](#view-bulk-details): View bulk payment with the batch id
    - Request:
        - Method: `GET`
        - Path: `/account/payment/bulk/{id}`
        - Headers: 
            - Authorization: `{{Bearer token}}`

        - Response:
        - Status: `200 OK`
        - Body:
        ```json
        [
            {
                "list all the bulk payment"
            }
        ]
        ```

### Account Lookup

- [GET /account/lookup/{accountNo}](#account-lookup): Delete a post.
     - Request:
        - Method: `GET`
        - Path: `/account/lookup/{accountNo}`
        - Headers: 
            - Authorization: `{{Bearer token}}`

        - Response:
        - Status: `200 OK`
        - Body:
        ```json
            {
                "accountName": "SAM AKIN",
                "accountNo": "3000000000",
                "accountType": "SAVINGS ACCOUNT"
            }
        ```


### Transfer

- [POST /account/transfer](#transfer): Fund Transfer
     - Request:
        - Method: `POST`
        - Path: `/account/transfer`
        - Headers: 
            - Authorization: `{{Bearer token}}`
        - Body:
            ```json
            {
                "debitAccount": "3000000000",
                "creditAccount": "3000000001",
                "destinationBankCode": "100",
                "amount": "2000",
                "description": "Test Payment",
                "pin": "1792"
            }
            ```
        - Response:
        - Status: `200 OK`
        - Body:
        ```json
            {
                "code": 200,
                "message": "Transaction successfully",
                "status": "successful"
            }
        ```

### Validate Channel

- [POST /account/channel/validate](#validate-channel): After using scanner to scan the QRCode or use NFC to get the record, pass the encrypted data to the request body.
     - Request:
        - Method: `POST`
        - Path: `/account/channel/validate`
        - Headers: 
            - Authorization: `{{Bearer token}}`
        - Body:
            ```json
            {
                "channel": "qrpay",
                "data": "Sb+C0IayOigJvdSCPUJjVCtdeCuYKYsvogzOjRC...."
            }
            ```
        - Response:
        - Status: `200 OK`
        - Body:
        ```json
            {
                "accountName": "SAM AKIN",
                "accountNo": "3000000000",
                "accountType": "SAVINGS ACCOUNT"
            }
        ```

### Transaction Log

- [GET /account/logs](#transaction-history): Pull all customer transaction history
    - Request:
        - Method: `GET`
        - Path: `/account/logs`
        - Headers: 
            - Authorization: `{{Bearer token}}`

        - Response:
        - Status: `200 OK`
        - Body:
        ```json
        [
            {
                "list of all transactions"
            }
        ]
        ```

### My Account

- [GET /account](#my-accounts): Show customer account
    - Request:
        - Method: `GET`
        - Path: `/account`
        - Headers: 
            - Authorization: `{{Bearer token}}`

        - Response:
        - Status: `200 OK`
        - Body:
        ```json
            {
                "balance": "9858000",
                "tier": "tier1",
                "qrCode": "https://res.cloudinary.qrcode/qrcode_q0no3v.png",
                "accountType": "SAVINGS ACCOUNT",
                "lockBalance": "0",
                "accountNumber": "3000000000"
            }
        ```

# Admin

### Customer

- [GET /admin/customer](#all-customer): Admin get all customer 
    - Request:
        - Method: `GET`
        - Path: `/admin/customer`
        - Headers: 
            - Authorization: `{{Bearer token}}`

        - Response:
        - Status: `200 OK`
        - Body:
        ```json
            [
                {
                    "id": "470f907e-c3e2-41d1-8643-3b501b4d4a16",
                    "firstName": "Akin",
                    "lastName": "Sam",
                    "middleName": null,
                    "address": null,
                    "phoneNumber": "08022301473",
                    "profileImage": null,
                    "modeOfId": null,
                    "idData": null,
                    "verified": false,
                    "user": {
                        "id": "03a70177-08f3-4434-aa25-f2e9e493e5e4",
                        "email": "akinsam@gmail.com",
                        "isCustomer": true,
                        "isAdmin": false,
                        "createdAt": "2023-06-28T00:58:54.724+00:00"
                    },
                    "createdAt": "2023-06-28T00:58:54.738+00:00",
                    "updatedAt": "2023-06-28T00:58:54.738+00:00"
                }
            ]
        ```

### All Transaction

- [GET /admin/customer/transactions](#all-transaction): Admin get all transactions 
    - Request:
        - Method: `GET`
        - Path: `/admin/customer/transactions`
        - Headers: 
            - Authorization: `{{Bearer token}}`

        - Response:
        - Status: `200 OK`
        - Body:
        ```json
            [
                {
                    "id": "5a67fd4e-5e20-451b-952c-bc5b51b7a9eb",
                    "reference": "5V1ER396PTLWJCXS",
                    "source": "3000000000",
                    "sourceBank": "100",
                    "destination": "3000000001",
                    "destinationBank": "100",
                    "amount": "20000",
                    "description": "SAM|Test Payment|SAMSON",
                    "create_at": "2023-06-28T09:43:15.798+00:00"
                },
                {
                    "id": "100cd1e0-64a4-467c-8b47-209fbd339fb5",
                    "reference": "QI88AA2ODS5DAYKY",
                    "source": "3000000000",
                    "sourceBank": "100",
                    "destination": "3000000001",
                    "destinationBank": "100",
                    "amount": "20000",
                    "description": "SAM|Test Payment|SAMSON",
                    "create_at": "2023-06-28T09:47:28.801+00:00"
                },
            ]
        ```

### View Account Transaction

- [GET /admin/customer/account/logs?accountNo={{accountNo}}](#view-account-transaction): Admin view all transactions that happen on a single account.

    - Request:
        - Method: `GET`
        - Path: `/admin/customer/account/logs?accountNo={{accountNo}}`
        - Headers: 
            - Authorization: `{{Bearer token}}`

        - Response:
        - Status: `200 OK`
        - Body:
        ```json
            [
                
                {
                    "id": "5a67fd4e-5e20-451b-952c-bc5b51b7a9eb",
                    "reference": "5V1ER396PTLWJCXS",
                    "source": "3000000000",
                    "sourceBank": "100",
                    "destination": "3000000001",
                    "destinationBank": "100",
                    "amount": "20000",
                    "description": "SAM|Test Payment|SAMSON",
                    "create_at": "2023-06-28T09:43:15.798+00:00"
                },
                {
                    "id": "100cd1e0-64a4-467c-8b47-209fbd339fb5",
                    "reference": "QI88AA2ODS5DAYKY",
                    "source": "3000000000",
                    "sourceBank": "100",
                    "destination": "3000000001",
                    "destinationBank": "100",
                    "amount": "20000",
                    "description": "SAM|Test Payment|SAMSON",
                    "create_at": "2023-06-28T09:47:28.801+00:00"
                },
            ]
        ```

### View Customer Account

- [GET /admin/customer/{{customerId}}](#view-customer-account): Admin view customer's account.

    - Request:
        - Method: `GET`
        - Path: `/admin/customer/{{customerId}}`
        - Headers: 
            - Authorization: `{{Bearer token}}`

        - Response:
        - Status: `200 OK`
        - Body:
        ```json
            {
                "accountNo": "3000000000",
                "balance": "9860000.0",
                "lockBalance": "0",
                "tier": "tier1",
                "code": "000",
                "active": true,
                "isQr": true,
                "qrodeUrl": "https://res.cloudinary.com/dhkde_q0no3v.png"
            }
        ```

## Staff

### Create Staff

- [POST /admin/staff](#create-staff): Admin Create staff.
     - Request:
        - Method: `POST`
        - Path: `/admin/staff`
        - Headers: 
            - Authorization: `{{Bearer token}}`
        - Body:
            ```json
            {
                "firstName": "John",
                "lastName": "Doe",
                "email": "test@gmail.com",
                "department": "cdd6475e-8ede-4ccb-a87f-cdadaa593324",
                "permission": ["33b606e8-5925-412f-aff1-f1e63d07de82"]
            }
            ```
        - Response:
        - Status: `200 OK`
        - Body:
        ```json
            {
                "code": 200,
                "message": "Staff created successfully",
                "status": "successful"
            }
        ```

### Update Staff Record

- [PUT /admin/staff/{{staffid}}](#update-staff-record): Admin update staff record.
     - Request:
        - Method: `PUT`
        - Path: `/admin/staff/{{staffid}}`
        - Headers: 
            - Authorization: `{{Bearer token}}`
        - Body:
            ```json
            {
                "firstName": "John",
                "lastName": "Doe",
                "department": "cdd6475e-8ede-4ccb-a87f-cdadaa593324"
            }
            ```
        - Response:
        - Status: `200 OK`
        - Body:
        ```json
            {
                "code": 200,
                "message": "Record updated successfully",
                "status": "successful"
            }
        ```

### Assign Permission to Staff

- [PUT /staff/permission/{{staffid}}](#assign-permission-to-staff): Admin grant staff permission.
     - Request:
        - Method: `PUT`
        - Path: `/staff/permission/{{staffid}}`
        - Headers: 
            - Authorization: `{{Bearer token}}`
        - Body:
            ```json
            {
                "permission": "713abf3f-d595-4774-a4e3-5b6c3bf83e96"
            }
            ```
        - Response:
        - Status: `200 OK`
        - Body:
        ```json
            {
                "code": 200,
                "message": "Permission granted successfully",
                "status": "successful"
            }
        ```

### Revoke Permission from Staff

- [GET /admin/staff/permission/{{customerId}}/revoke/{{permissionId}}](#revoke-permission-from-staff): Admin revoke a permission from staff.

    - Request:
        - Method: `GET`
        - Path: `/admin/staff/permission/{{customerId}}/revoke/{{permissionId}}`
        - Headers: 
            - Authorization: `{{Bearer token}}`

        - Response:
        - Status: `200 OK`
        - Body:
        ```json
            {
                "code": 200,
                "message": "Permission revoked successfullly",
                "status": "success"
            }
        ```

### All Staff

- [GET /admin/staff](#all-staff): Admin get all staff.

    - Request:
        - Method: `GET`
        - Path: `/admin/staff`
        - Headers: 
            - Authorization: `{{Bearer token}}`

        - Response:
        - Status: `200 OK`
        - Body:
        ```json
            [
                {
                    "id": "777f6a3e-72a4-4bac-873b-7e443f34174d",
                    "firstName": "System",
                    "lastName": "Administrator",
                    "department": "9387fc1e-cd58-49ba-823f-bd114c1fabf0",
                    "profileImage": null,
                    "permission": [
                        "713abf3f-d595-4774-a4e3-5b6c3bf83e96"
                    ],
                    "user": {
                        "id": "89e0b8c0-ced2-4bac-920f-368a5df321f8",
                        "email": "admin@fintabsolution.com",
                        "isCustomer": false,
                        "isAdmin": true,
                        "createdAt": "2023-06-28T21:15:51.233+00:00"
                    },
                    "updatedAt": "2023-06-28T21:15:51.245+00:00",
                    "createdAt": "2023-06-28T21:15:51.245+00:00"
                },
                {
                    "id": "51590060-f1a7-44b6-8816-b1056bb37c18",
                    "firstName": "John",
                    "lastName": "Doe",
                    "department": "cdd6475e-8ede-4ccb-a87f-cdadaa593324",
                    "profileImage": null,
                    "permission": [
                        "33b606e8-5925-412f-aff1-f1e63d07de82"
                    ],
                    "user": {
                        "id": "560436ad-6f19-4f71-bf26-dc5ad58f3146",
                        "email": "jdoe@gmail.com",
                        "isCustomer": false,
                        "isAdmin": true,
                        "createdAt": "2023-06-29T07:35:06.725+00:00"
                    },
                    "updatedAt": "2023-06-29T07:35:06.728+00:00",
                    "createdAt": "2023-06-29T07:35:06.728+00:00"
                },
            ]
        ```

# Department

### Admin Create Department

- [POST /admin/department](#create-department): Admin create department.
     - Request:
        - Method: `POST`
        - Path: `/admin/department`
        - Headers: 
            - Authorization: `{{Bearer token}}`
        - Body:
            ```json
            {
                "name": "Test Department"
            }
            ```
        - Response:
        - Status: `200 OK`
        - Body:
        ```json
            {
                "code": 200,
                "message": "Department created",
                "status": "successful"
            }
        ```

### Admin Update Department

- [PUT /admin/department/{{departmentId}}](#update-department): Admin update department.
     - Request:
        - Method: `POST`
        - Path: `/admin/department/{{departmentId}}`
        - Headers: 
            - Authorization: `{{Bearer token}}`
        - Body:
            ```json
            {
                "name": "New Department Name"
            }
            ```
        - Response:
        - Status: `200 OK`
        - Body:
        ```json
            {
                "code": 200,
                "message": "Department updated successfull",
                "status": "successful"
            }
        ```

### Admin Delete Department

- [DELETE /admin/department/{{customerId}}](#delete-department): Admin delete department.

    - Request:
        - Method: `DELETE`
        - Path: `/admin/department/{{customerId}}`
        - Headers: 
            - Authorization: `{{Bearer token}}`

        - Response:
        - Status: `200 OK`
        - Body:
        ```json
            {
                "code": 200,
                "message": "Department deleted successfull",
                "status": "successful"
            }
        ```


### Admin Get All Departments

- [GET /admin/department](#all-department): Admin get all departments.

    - Request:
        - Method: `GET`
        - Path: `/admin/department`
        - Headers: 
            - Authorization: `{{Bearer token}}`

        - Response:
        - Status: `200 OK`
        - Body:
        ```json
            [
                {
                    "id": "9387fc1e-cd58-49ba-823f-bd114c1fabf0",
                    "name": "admin",
                    "updatedAt": "2023-06-28T21:15:51.242+00:00",
                    "createdAt": "2023-06-28T21:15:51.242+00:00"
                },
                {
                    "id": "cdd6475e-8ede-4ccb-a87f-cdadaa593324",
                    "name": "Test Department",
                    "updatedAt": "2023-06-29T07:29:45.221+00:00",
                    "createdAt": "2023-06-29T07:29:45.221+00:00"
                }
            ]
        ```

# Permission

### Admin Create Permission
- [POST /admin/permission](#create-permission): Admin create permission.
     - Request:
        - Method: `POST`
        - Path: `/admin/permission`
        - Headers: 
            - Authorization: `{{Bearer token}}`
        - Body:
            ```json
            {
                "name": "Create",
                "role": "create"
            }
            ```
        - Response:
        - Status: `200 OK`
        - Body:
        ```json
            {
                "code": 200,
                "message": "Permission created successfully",
                "status": "successful"
            }
        ```

### Admin Get All Permission

- [GET /admin/permission](#all-permission): Admin get all permission.
     - Request:
        - Method: `GET`
        - Path: `/admin/permission`
        - Headers: 
            - Authorization: `{{Bearer token}}`

        - Response:
        - Status: `200 OK`
        - Body:
        ```json
            [
                {
                    "id": "713abf3f-d595-4774-a4e3-5b6c3bf83e96",
                    "name": "admin",
                    "role": "all",
                    "updatedAt": "2023-06-28T21:15:51.241+00:00",
                    "createdAt": "2023-06-28T21:15:51.241+00:00"
                },
                {
                    "id": "33b606e8-5925-412f-aff1-f1e63d07de82",
                    "name": "Create",
                    "role": "create",
                    "updatedAt": "2023-06-29T07:30:59.146+00:00",
                    "createdAt": "2023-06-29T07:30:59.146+00:00"
                }
            ]
        ```

### Admin Update Permission

- [POST /admin/permission/{{permissionId}}](#update-permission): Admin update permission.
     - Request:
        - Method: `POST`
        - Path: `/admin/permission/{{permissionId}}`
        - Headers: 
            - Authorization: `{{Bearer token}}`
        - Body:
            ```json
            {
                "name": "Create"
            }
            ```
        - Response:
        - Status: `200 OK`
        - Body:
        ```json
            {
                "code": 200,
                "message": "Permission updated successfully",
                "status": "successful"
            }
        ```

### Admin Delete Permission

- [DELETE /admin/permission/{{permissionId}}](#delete-permission): Admin delete permission.
     - Request:
        - Method: `DELETE`
        - Path: `/admin/permission/{{permissionId}}`
        - Headers: 
            - Authorization: `{{Bearer token}}`

        - Response:
        - Status: `200 OK`
        - Body:
        ```json
            {
                "code": 200,
                "message": "Permission deleted successfully",
                "status": "successful"
            }
        ```

# Loan

### Create Loan Type
- [POST /loan](#create-loan-type): Admin create Loan Type.
     - Request:
        - Method: `POST`
        - Path: `/loan`
        - Headers: 
            - Authorization: `{{Bearer token}}`
        - Body:
            ```json
            {
                "name": "Quick Loan",
                "tenure": "1",
                "rate": "4"
            }
            ```
        - Response:
        - Status: `200 OK`
        - Body:
        ```json
            {
                "code": 200,
                "message": "Loan type created",
                "status": "successful"
            }
        ```

### Update Loan Type

- [PUT /loan/{{id}}](#update-loan-type): Admin update loan Type.
     - Request:
        - Method: `PUT`
        - Path: `/loan/{{id}}`
        - Headers: 
            - Authorization: `{{Bearer token}}`
        - Body:
            ```json
            {
                "name": "Quick Loan Update",
                "tenure": "1",
                "rate": "4"
            }
            ```
        - Response:
        - Status: `200 OK`
        - Body:
        ```json
            {
                "code": 200,
                "message": "Loan type updated successfully",
                "status": "successful"
            }
        ```

### All Loan Type

- [GET /loan](#all-loan-type): All Loan Type.
     - Request:
        - Method: `GET`
        - Path: `/loan`
        - Headers: 
            - Authorization: `{{Bearer token}}`
        
        - Response:
        - Status: `200 OK`
        - Body:
        ```json
            [
                {
                    "id": "b7aaccea-5762-4089-b700-e0da7676458b",
                    "name": "Quick Loan Update",
                    "code": "J925H70",
                    "tenure": "1",
                    "rate": "4",
                    "updatedAt": "2023-07-02T06:56:45.436+00:00",
                    "createdAt": "2023-07-02T06:28:31.110+00:00"
                }
            ]
        ```
---


... and so on for each endpoint.

