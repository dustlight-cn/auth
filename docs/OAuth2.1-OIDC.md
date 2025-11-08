# OAuth 2.1 和 OpenID Connect 支持

本文档介绍 Auth 服务的 OAuth 2.1 和 OpenID Connect (OIDC) 功能。

## OAuth 2.1 支持

### PKCE (Proof Key for Code Exchange)

PKCE 是 OAuth 2.1 的核心安全特性，用于保护授权码流程免受授权码拦截攻击。

#### 使用方法

1. **授权请求**：在授权请求中添加 `code_challenge` 和 `code_challenge_method` 参数

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

参数说明：
- `code_challenge`: 从 `code_verifier` 计算得出的挑战码
- `code_challenge_method`: 挑战方法，支持 `S256` (推荐) 或 `plain`

2. **令牌请求**：在令牌请求中添加 `code_verifier` 参数

```http
POST /v1/oauth/token
Content-Type: application/x-www-form-urlencoded
Authorization: Basic BASE64(client_id:client_secret)

grant_type=authorization_code&
code=AUTHORIZATION_CODE&
redirect_uri=YOUR_REDIRECT_URI&
code_verifier=dBjftJeZ4CVP-mB92K27uhbUJU1p1r_wW1gFWFOEjXk
```

#### Code Verifier 和 Code Challenge 生成

**Code Verifier**:
- 长度：43-128 个字符
- 字符集：`[A-Z] / [a-z] / [0-9] / "-" / "." / "_" / "~"`

**Code Challenge 计算** (S256 方法):
```
code_challenge = BASE64URL(SHA256(ASCII(code_verifier)))
```

示例代码 (JavaScript):
```javascript
// 生成 code verifier
function generateCodeVerifier() {
  const array = new Uint8Array(32);
  crypto.getRandomValues(array);
  return base64URLEncode(array);
}

// 计算 code challenge
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

## OpenID Connect (OIDC) 支持

### Discovery Endpoint

获取 OpenID Provider 配置信息：

```http
GET /.well-known/openid-configuration
```

响应示例：
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

获取已认证用户的信息：

```http
GET /v1/userinfo
Authorization: Bearer ACCESS_TOKEN
```

或使用 POST 方法：

```http
POST /v1/userinfo
Authorization: Bearer ACCESS_TOKEN
```

响应示例：
```json
{
  "sub": "123456",
  "name": "张三",
  "preferred_username": "zhangsan",
  "email": "zhangsan@example.com",
  "email_verified": true,
  "phone_number": "+8613800138000",
  "phone_number_verified": true,
  "picture": "https://example.com/avatar.jpg",
  "gender": "male",
  "updated_at": 1699411200
}
```

### OpenID Connect Scopes

#### openid (必需)

启用 OpenID Connect 功能。必须包含此 scope 才能获取 ID Token 和访问 UserInfo endpoint。

#### profile

返回用户基本信息：
- `name`: 用户昵称
- `preferred_username`: 用户名
- `picture`: 头像 URL
- `gender`: 性别 (male, female, other)
- `updated_at`: 更新时间戳

#### email

返回邮箱信息：
- `email`: 邮箱地址
- `email_verified`: 邮箱是否已验证

#### phone

返回手机号信息：
- `phone_number`: 手机号码
- `phone_number_verified`: 手机号是否已验证

### ID Token

当请求包含 `openid` scope 时，JWT Access Token 中会包含标准的 OIDC claims：

```json
{
  "iss": "https://accounts.dustlight.cn",
  "sub": "123456",
  "aud": "client_id",
  "exp": 1699414800,
  "iat": 1699411200,
  "name": "张三",
  "email": "zhangsan@example.com",
  "picture": "https://example.com/avatar.jpg",
  "nonce": "n-0S6_WzA2Mj"
}
```

Claims 说明：
- `iss`: 签发者 (Issuer)
- `sub`: 主体标识符 (Subject Identifier) - 用户 UID
- `aud`: 受众 (Audience) - Client ID
- `exp`: 过期时间 (Expiration Time)
- `iat`: 签发时间 (Issued At)
- `nonce`: 一次性随机值（如果请求中提供）

### 完整授权流程示例

使用 PKCE 和 OpenID Connect 的完整流程：

1. **生成 PKCE 参数**
```javascript
const codeVerifier = generateCodeVerifier();
const codeChallenge = await generateCodeChallenge(codeVerifier);
// 保存 codeVerifier 用于后续令牌请求
```

2. **发起授权请求**
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

3. **用户授权后获取授权码**
```
https://your-app.com/callback?code=AUTHORIZATION_CODE&state=RANDOM_STATE
```

4. **交换令牌**
```http
POST /v1/oauth/token
Content-Type: application/x-www-form-urlencoded
Authorization: Basic BASE64(client_id:client_secret)

grant_type=authorization_code&
code=AUTHORIZATION_CODE&
redirect_uri=https://your-app.com/callback&
code_verifier=CODE_VERIFIER
```

5. **获取用户信息**
```http
GET /v1/userinfo
Authorization: Bearer ACCESS_TOKEN
```

## 配置

在 `application.yaml` 中配置 OIDC issuer：

```yaml
dustlight:
  auth:
    oidc:
      issuer: "https://accounts.dustlight.cn"  # 修改为您的实际域名
```

## 安全建议

1. **始终使用 PKCE**：对于公共客户端（如 SPA、移动应用），PKCE 是必需的
2. **使用 S256 方法**：优先使用 `S256` 而不是 `plain` 方法
3. **验证 nonce**：在客户端验证 ID Token 中的 nonce 与请求时发送的 nonce 一致
4. **验证 state**：防止 CSRF 攻击
5. **使用 HTTPS**：所有通信必须通过 HTTPS

## 参考资料

- [OAuth 2.1](https://datatracker.ietf.org/doc/html/draft-ietf-oauth-v2-1-07)
- [RFC 7636: PKCE](https://datatracker.ietf.org/doc/html/rfc7636)
- [OpenID Connect Core 1.0](https://openid.net/specs/openid-connect-core-1_0.html)
- [OpenID Connect Discovery 1.0](https://openid.net/specs/openid-connect-discovery-1_0.html)
