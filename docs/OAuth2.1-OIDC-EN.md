# OAuth 2.1 and OpenID Connect Support

This document describes the OAuth 2.1 and OpenID Connect (OIDC) features in the Auth service.

## OAuth 2.1 Support

### PKCE (Proof Key for Code Exchange)

PKCE is a core security feature of OAuth 2.1 that protects the authorization code flow from authorization code interception attacks.

#### Usage

1. **Authorization Request**: Add `code_challenge` and `code_challenge_method` parameters

```http
GET /v1/oauth/authorization?
  response_type=code&
  client_id=YOUR_CLIENT_ID&
  redirect_uri=YOUR_REDIRECT_URI&
  scope=openid%20profile%20email&
  code_challenge=E9Melhoa2OwvFrEMTJguCHaoeK1t8URWbuGJSstw-cM&
  code_challenge_method=S256&
  state=xyz
```

Parameters:
- `code_challenge`: Challenge code computed from `code_verifier`
- `code_challenge_method`: Challenge method, supports `S256` (recommended) or `plain`

2. **Token Request**: Add `code_verifier` parameter

```http
POST /v1/oauth/token
Content-Type: application/x-www-form-urlencoded
Authorization: Basic BASE64(client_id:client_secret)

grant_type=authorization_code&
code=AUTHORIZATION_CODE&
redirect_uri=YOUR_REDIRECT_URI&
code_verifier=dBjftJeZ4CVP-mB92K27uhbUJU1p1r_wW1gFWFOEjXk
```

#### Code Verifier and Code Challenge Generation

**Code Verifier**:
- Length: 43-128 characters
- Character set: `[A-Z] / [a-z] / [0-9] / "-" / "." / "_" / "~"`

**Code Challenge Computation** (S256 method):
```
code_challenge = BASE64URL(SHA256(ASCII(code_verifier)))
```

Example code (JavaScript):
```javascript
// Generate code verifier
function generateCodeVerifier() {
  const array = new Uint8Array(32);
  crypto.getRandomValues(array);
  return base64URLEncode(array);
}

// Compute code challenge
async function generateCodeChallenge(verifier) {
  const encoder = new TextEncoder();
  const data = encoder.encode(verifier);
  const digest = await crypto.subtle.digest('SHA-256', data);
  return base64URLEncode(new Uint8Array(digest));
}

function base64URLEncode(buffer) {
  return btoa(String.fromCharCode(...buffer))
    .replace(/\+/g, '-')
    .replace(/\//g, '_')
    .replace(/=/g, '');
}
```

## OpenID Connect (OIDC) Support

### Discovery Endpoint

Retrieve OpenID Provider configuration:

```http
GET /.well-known/openid-configuration
```

Response example:
```json
{
  "issuer": "https://accounts.dustlight.cn",
  "authorization_endpoint": "https://api.dustlight.cn/v1/oauth/authorize",
  "token_endpoint": "https://api.dustlight.cn/v1/oauth/token",
  "userinfo_endpoint": "https://api.dustlight.cn/v1/userinfo",
  "jwks_uri": "https://api.dustlight.cn/v1/jwk",
  "scopes_supported": ["openid", "profile", "email", "phone"],
  "response_types_supported": ["code", "token", "id_token", ...],
  "grant_types_supported": ["authorization_code", "implicit", "refresh_token", "client_credentials"],
  "subject_types_supported": ["public"],
  "id_token_signing_alg_values_supported": ["RS256"],
  "claims_supported": ["sub", "name", "email", "picture", ...],
  "code_challenge_methods_supported": ["S256", "plain"]
}
```

### UserInfo Endpoint

Retrieve information about the authenticated user:

```http
GET /v1/userinfo
Authorization: Bearer ACCESS_TOKEN
```

Or using POST method:

```http
POST /v1/userinfo
Authorization: Bearer ACCESS_TOKEN
```

Response example:
```json
{
  "sub": "123456",
  "name": "John Doe",
  "preferred_username": "johndoe",
  "email": "john.doe@example.com",
  "email_verified": true,
  "phone_number": "+14155551234",
  "phone_number_verified": true,
  "picture": "https://example.com/avatar.jpg",
  "gender": "male",
  "updated_at": 1699411200
}
```

### OpenID Connect Scopes

#### openid (Required)

Enables OpenID Connect functionality. This scope must be included to obtain an ID Token and access the UserInfo endpoint.

#### profile

Returns basic profile information:
- `name`: User's nickname
- `preferred_username`: Username
- `picture`: Avatar URL
- `gender`: Gender (male, female, other)
- `updated_at`: Update timestamp

#### email

Returns email information:
- `email`: Email address
- `email_verified`: Whether the email is verified

#### phone

Returns phone information:
- `phone_number`: Phone number
- `phone_number_verified`: Whether the phone number is verified

### ID Token

When the request includes the `openid` scope, the JWT Access Token will contain standard OIDC claims:

```json
{
  "iss": "https://accounts.dustlight.cn",
  "sub": "123456",
  "aud": "client_id",
  "exp": 1699414800,
  "iat": 1699411200,
  "name": "John Doe",
  "email": "john.doe@example.com",
  "picture": "https://example.com/avatar.jpg",
  "nonce": "n-0S6_WzA2Mj"
}
```

Claims description:
- `iss`: Issuer
- `sub`: Subject Identifier - User UID
- `aud`: Audience - Client ID
- `exp`: Expiration Time
- `iat`: Issued At
- `nonce`: One-time random value (if provided in the request)

### Complete Authorization Flow Example

Complete flow using PKCE and OpenID Connect:

1. **Generate PKCE parameters**
```javascript
const codeVerifier = generateCodeVerifier();
const codeChallenge = await generateCodeChallenge(codeVerifier);
// Save codeVerifier for subsequent token request
```

2. **Initiate authorization request**
```http
GET /v1/oauth/authorization?
  response_type=code&
  client_id=YOUR_CLIENT_ID&
  redirect_uri=https://your-app.com/callback&
  scope=openid%20profile%20email&
  code_challenge=CODE_CHALLENGE&
  code_challenge_method=S256&
  state=RANDOM_STATE&
  nonce=RANDOM_NONCE
```

3. **Obtain authorization code after user authorization**
```
https://your-app.com/callback?code=AUTHORIZATION_CODE&state=RANDOM_STATE
```

4. **Exchange token**
```http
POST /v1/oauth/token
Content-Type: application/x-www-form-urlencoded
Authorization: Basic BASE64(client_id:client_secret)

grant_type=authorization_code&
code=AUTHORIZATION_CODE&
redirect_uri=https://your-app.com/callback&
code_verifier=CODE_VERIFIER
```

5. **Retrieve user information**
```http
GET /v1/userinfo
Authorization: Bearer ACCESS_TOKEN
```

## Configuration

Configure the OIDC issuer in `application.yaml`:

```yaml
dustlight:
  auth:
    oidc:
      issuer: "https://accounts.dustlight.cn"  # Change to your actual domain
```

## Security Recommendations

1. **Always use PKCE**: For public clients (e.g., SPAs, mobile apps), PKCE is mandatory
2. **Use S256 method**: Prefer `S256` over `plain` method
3. **Verify nonce**: Verify that the nonce in the ID Token matches the nonce sent in the request
4. **Verify state**: Prevent CSRF attacks
5. **Use HTTPS**: All communication must use HTTPS

## References

- [OAuth 2.1](https://datatracker.ietf.org/doc/html/draft-ietf-oauth-v2-1-07)
- [RFC 7636: PKCE](https://datatracker.ietf.org/doc/html/rfc7636)
- [OpenID Connect Core 1.0](https://openid.net/specs/openid-connect-core-1_0.html)
- [OpenID Connect Discovery 1.0](https://openid.net/specs/openid-connect-discovery-1_0.html)
