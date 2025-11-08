# User


## Properties

Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**uid** | **number** |  | [optional] [default to undefined]
**authorities** | **Array&lt;string&gt;** |  | [optional] [default to undefined]
**gender** | **number** |  | [optional] [default to undefined]
**email** | **string** |  | [optional] [default to undefined]
**phone** | **string** |  | [optional] [default to undefined]
**nickname** | **string** |  | [optional] [default to undefined]
**avatar** | **string** |  | [optional] [default to undefined]
**credentialsExpiredAt** | **string** |  | [optional] [default to undefined]
**accountExpiredAt** | **string** |  | [optional] [default to undefined]
**unlockedAt** | **string** |  | [optional] [default to undefined]
**enabled** | **boolean** |  | [optional] [default to undefined]
**username** | **string** |  | [optional] [default to undefined]
**accountNonExpired** | **boolean** |  | [optional] [default to undefined]
**credentialsNonExpired** | **boolean** |  | [optional] [default to undefined]
**accountNonLocked** | **boolean** |  | [optional] [default to undefined]
**createdAt** | **string** |  | [optional] [default to undefined]
**updatedAt** | **string** |  | [optional] [default to undefined]
**roles** | [**Array&lt;UserRole&gt;**](UserRole.md) |  | [optional] [default to undefined]

## Example

```typescript
import { User } from './api';

const instance: User = {
    uid,
    authorities,
    gender,
    email,
    phone,
    nickname,
    avatar,
    credentialsExpiredAt,
    accountExpiredAt,
    unlockedAt,
    enabled,
    username,
    accountNonExpired,
    credentialsNonExpired,
    accountNonLocked,
    createdAt,
    updatedAt,
    roles,
};
```

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)
