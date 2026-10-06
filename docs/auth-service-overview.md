# Auth Service Overview

## Purpose
The Auth Service is responsible for managing user identity and access control for the ecommerce platform.

## Responsibilities
- Register new users
- Authenticate users during login
- Issue JWT access tokens
- Issue and manage refresh tokens
- Support logout through token revocation
- Provide the current authenticated user profile
- Enforce role-based access control

## Non-responsibilities
The Auth Service does **not** handle:
- product catalog data
- shopping cart data
- order processing
- payment processing
- recommendation logic
- frontend rendering

## Key Rules
- Each email must be unique
- Passwords must always be stored as hashes
- New users receive the `USER` role by default
- Protected endpoints must require a valid JWT
- Refresh tokens must be revocable
- The service should remain stateless for access-token validation

## Core Entities

### 1. User
Represents a registered account in the system.

Main fields:
- id
- fullName
- email
- passwordHash
- enabled
- createdAt
- updatedAt

Why it exists:
This stores the identity and credential data for login and authorization.

### 2. Role
Represents the access level assigned to a user.

Main fields:
- id
- name

Example values:
- USER
- ADMIN

Why it exists:
This allows role-based access control across the platform.

### 3. RefreshToken
Represents a long-lived token used to obtain a new access token.

Main fields:
- id
- user
- token
- expiresAt
- revoked
- createdAt

Why it exists:
This supports session continuity without forcing the user to log in repeatedly.

## Relationship Summary
- A User can have one or more Roles
- A Role can belong to many Users
- A User can have one or more RefreshTokens

## Suggested MVP Rules
- one user can have multiple roles
- one user starts with `USER`
- `ADMIN` is manually assigned later
- refresh tokens are stored in DB
- access tokens are validated using JWT signature only