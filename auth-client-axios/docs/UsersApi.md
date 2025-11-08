# UsersApi

All URIs are relative to *http://localhost:8080*

|Method | HTTP request | Description|
|------------- | ------------- | -------------|
|[**createUser**](#createuser) | **POST** /v1/users | 创建用户（用户名、邮箱、手机号码不可重复）|
|[**deleteUser**](#deleteuser) | **DELETE** /v1/users/{uid} | 删除用户（永久删除）|
|[**getUser**](#getuser) | **GET** /v1/users/{uid} | 获取用户信息|
|[**getUserAvatar**](#getuseravatar) | **GET** /v1/users/{uid}/avatar | 获取用户头像|
|[**getUsers**](#getusers) | **GET** /v1/users | 查找用户|
|[**updateUserAvatar**](#updateuseravatar) | **PUT** /v1/users/{uid}/avatar | 更新用户头像|
|[**updateUserEmail**](#updateuseremail) | **PUT** /v1/users/{uid}/email | 更新用户邮箱|
|[**updateUserEnabled**](#updateuserenabled) | **PUT** /v1/users/{uid}/ban | 设置用户封禁或解封|
|[**updateUserExpiredAt**](#updateuserexpiredat) | **PUT** /v1/users/{uid}/expired-at | 设置用户解锁日期|
|[**updateUserGender**](#updateusergender) | **PUT** /v1/users/{uid}/gender | 更新用户性别|
|[**updateUserNickname**](#updateusernickname) | **PUT** /v1/users/{uid}/nickname | 更新用户昵称|
|[**updateUserPassword**](#updateuserpassword) | **PUT** /v1/users/{uid}/password | 更新用户密码|
|[**updateUserPhone**](#updateuserphone) | **PUT** /v1/users/{uid}/phone | 更新用户手机号码|
|[**updateUserUnlockAt**](#updateuserunlockat) | **PUT** /v1/users/{uid}/unlock-at | 设置用户解锁日期|

# **createUser**
> User createUser()

应用和用户需要 CREATE_USER 权限。

### Example

```typescript
import {
    UsersApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new UsersApi(configuration);

let username: string; // (default to undefined)
let password: string; // (default to undefined)
let email: string; // (optional) (default to undefined)
let phone: string; // (optional) (default to undefined)

const { status, data } = await apiInstance.createUser(
    username,
    password,
    email,
    phone
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **username** | [**string**] |  | defaults to undefined|
| **password** | [**string**] |  | defaults to undefined|
| **email** | [**string**] |  | (optional) defaults to undefined|
| **phone** | [**string**] |  | (optional) defaults to undefined|


### Return type

**User**

### Authorization

[AccessToken](../README.md#AccessToken)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json; charset=UTF-8


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **deleteUser**
> deleteUser()

应用和用户需要 DELETE_USER 权限。

### Example

```typescript
import {
    UsersApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new UsersApi(configuration);

let uid: number; // (default to undefined)

const { status, data } = await apiInstance.deleteUser(
    uid
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **uid** | [**number**] |  | defaults to undefined|


### Return type

void (empty response body)

### Authorization

[AccessToken](../README.md#AccessToken)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **getUser**
> User getUser()

获取用户的公开信息。如果应用与用户拥有 READ_USER 权限，则获取完整信息。

### Example

```typescript
import {
    UsersApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new UsersApi(configuration);

let uid: number; // (default to undefined)

const { status, data } = await apiInstance.getUser(
    uid
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **uid** | [**number**] |  | defaults to undefined|


### Return type

**User**

### Authorization

[AccessToken](../README.md#AccessToken)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json; charset=UTF-8


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **getUserAvatar**
> getUserAvatar()


### Example

```typescript
import {
    UsersApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new UsersApi(configuration);

let uid: number; // (default to undefined)

const { status, data } = await apiInstance.getUserAvatar(
    uid
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **uid** | [**number**] |  | defaults to undefined|


### Return type

void (empty response body)

### Authorization

[AccessToken](../README.md#AccessToken)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **getUsers**
> QueryResultsUser getUsers()

查询或者列出用户（取决于有无关键字(q)或者用户ID(uid)），获取公开信息。若应用和用户拥有 READ_USER 权限，则获取完整信息。

### Example

```typescript
import {
    UsersApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new UsersApi(configuration);

let uid: Array<number>; // (optional) (default to undefined)
let q: string; // (optional) (default to undefined)
let offset: number; // (optional) (default to undefined)
let limit: number; // (optional) (default to 10)
let order: Array<string>; // (optional) (default to undefined)

const { status, data } = await apiInstance.getUsers(
    uid,
    q,
    offset,
    limit,
    order
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **uid** | **Array&lt;number&gt;** |  | (optional) defaults to undefined|
| **q** | [**string**] |  | (optional) defaults to undefined|
| **offset** | [**number**] |  | (optional) defaults to undefined|
| **limit** | [**number**] |  | (optional) defaults to 10|
| **order** | **Array&lt;string&gt;** |  | (optional) defaults to undefined|


### Return type

**QueryResultsUser**

### Authorization

[AccessToken](../README.md#AccessToken)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: application/json; charset=UTF-8


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **updateUserAvatar**
> updateUserAvatar(body)

应用和用户（修改自身信息除外）需要拥有 WRITE_USER 权限。

### Example

```typescript
import {
    UsersApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new UsersApi(configuration);

let uid: number; // (default to undefined)
let body: File; //

const { status, data } = await apiInstance.updateUserAvatar(
    uid,
    body
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **body** | **File**|  | |
| **uid** | [**number**] |  | defaults to undefined|


### Return type

void (empty response body)

### Authorization

[AccessToken](../README.md#AccessToken)

### HTTP request headers

 - **Content-Type**: image/*
 - **Accept**: Not defined


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **updateUserEmail**
> updateUserEmail()

应用和用户需拥有 WRITE_USER_EMAIL 权限。

### Example

```typescript
import {
    UsersApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new UsersApi(configuration);

let uid: number; // (default to undefined)
let code: string; // (default to undefined)

const { status, data } = await apiInstance.updateUserEmail(
    uid,
    code
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **uid** | [**number**] |  | defaults to undefined|
| **code** | [**string**] |  | defaults to undefined|


### Return type

void (empty response body)

### Authorization

[AccessToken](../README.md#AccessToken)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **updateUserEnabled**
> updateUserEnabled()

封禁或解封用户。应用和用户需拥有 LOCK_USER 权限。

### Example

```typescript
import {
    UsersApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new UsersApi(configuration);

let uid: number; // (default to undefined)
let enabled: boolean; // (default to undefined)

const { status, data } = await apiInstance.updateUserEnabled(
    uid,
    enabled
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **uid** | [**number**] |  | defaults to undefined|
| **enabled** | [**boolean**] |  | defaults to undefined|


### Return type

void (empty response body)

### Authorization

[AccessToken](../README.md#AccessToken)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **updateUserExpiredAt**
> updateUserExpiredAt()

设置用户账号过期日期。设置为 NULL 则无过期时间。应用和用户需拥有 LOCK_USER 权限。

### Example

```typescript
import {
    UsersApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new UsersApi(configuration);

let uid: number; // (default to undefined)
let expiredAt: string; // (default to undefined)

const { status, data } = await apiInstance.updateUserExpiredAt(
    uid,
    expiredAt
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **uid** | [**number**] |  | defaults to undefined|
| **expiredAt** | [**string**] |  | defaults to undefined|


### Return type

void (empty response body)

### Authorization

[AccessToken](../README.md#AccessToken)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **updateUserGender**
> updateUserGender()

应用和用户（修改自身信息除外）需要拥有 WRITE_USER 权限。

### Example

```typescript
import {
    UsersApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new UsersApi(configuration);

let uid: number; // (default to undefined)
let gender: number; // (default to undefined)

const { status, data } = await apiInstance.updateUserGender(
    uid,
    gender
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **uid** | [**number**] |  | defaults to undefined|
| **gender** | [**number**] |  | defaults to undefined|


### Return type

void (empty response body)

### Authorization

[AccessToken](../README.md#AccessToken)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **updateUserNickname**
> updateUserNickname()

应用和用户（修改自身信息除外）需要拥有 WRITE_USER 权限。

### Example

```typescript
import {
    UsersApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new UsersApi(configuration);

let uid: number; // (default to undefined)
let nickname: string; // (default to undefined)

const { status, data } = await apiInstance.updateUserNickname(
    uid,
    nickname
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **uid** | [**number**] |  | defaults to undefined|
| **nickname** | [**string**] |  | defaults to undefined|


### Return type

void (empty response body)

### Authorization

[AccessToken](../README.md#AccessToken)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **updateUserPassword**
> updateUserPassword()

应用和用户需拥有 WRITE_USER_PASSWORD 权限。

### Example

```typescript
import {
    UsersApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new UsersApi(configuration);

let uid: number; // (default to undefined)
let password: string; // (default to undefined)

const { status, data } = await apiInstance.updateUserPassword(
    uid,
    password
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **uid** | [**number**] |  | defaults to undefined|
| **password** | [**string**] |  | defaults to undefined|


### Return type

void (empty response body)

### Authorization

[AccessToken](../README.md#AccessToken)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **updateUserPhone**
> updateUserPhone()

应用和用户需拥有 WRITE_USER_PHONE 权限。

### Example

```typescript
import {
    UsersApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new UsersApi(configuration);

let uid: number; // (default to undefined)
let code: string; // (default to undefined)

const { status, data } = await apiInstance.updateUserPhone(
    uid,
    code
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **uid** | [**number**] |  | defaults to undefined|
| **code** | [**string**] |  | defaults to undefined|


### Return type

void (empty response body)

### Authorization

[AccessToken](../README.md#AccessToken)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

# **updateUserUnlockAt**
> updateUserUnlockAt()

设置用户账号的解锁日期，在此日期日前账号不能使用。设置为 NULL 则不锁定。应用和用户需拥有 LOCK_USER 权限。

### Example

```typescript
import {
    UsersApi,
    Configuration
} from './api';

const configuration = new Configuration();
const apiInstance = new UsersApi(configuration);

let uid: number; // (default to undefined)
let unlockAt: string; // (default to undefined)

const { status, data } = await apiInstance.updateUserUnlockAt(
    uid,
    unlockAt
);
```

### Parameters

|Name | Type | Description  | Notes|
|------------- | ------------- | ------------- | -------------|
| **uid** | [**number**] |  | defaults to undefined|
| **unlockAt** | [**string**] |  | defaults to undefined|


### Return type

void (empty response body)

### Authorization

[AccessToken](../README.md#AccessToken)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: Not defined


### HTTP response details
| Status code | Description | Response headers |
|-------------|-------------|------------------|
|**200** | OK |  -  |

[[Back to top]](#) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to Model list]](../README.md#documentation-for-models) [[Back to README]](../README.md)

