# UserRoleClient


## Properties

Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**name** | **string** |  | [optional] [default to undefined]
**description** | **string** |  | [optional] [default to undefined]
**logo** | **string** |  | [optional] [default to undefined]
**createdAt** | **string** |  | [optional] [default to undefined]
**updatedAt** | **string** |  | [optional] [default to undefined]
**status** | **number** |  | [optional] [default to undefined]
**uid** | **number** |  | [optional] [default to undefined]
**resources** | **Set&lt;string&gt;** |  | [optional] [default to undefined]
**members** | **Array&lt;string&gt;** |  | [optional] [default to undefined]
**scopes** | [**Array&lt;ClientScope&gt;**](ClientScope.md) |  | [optional] [default to undefined]
**types** | [**Array&lt;GrantType&gt;**](GrantType.md) |  | [optional] [default to undefined]
**authorities** | **Array&lt;string&gt;** |  | [optional] [default to undefined]
**count** | **number** |  | [optional] [default to undefined]
**clientSecret** | **string** |  | [optional] [default to undefined]
**extra** | **{ [key: string]: object; }** |  | [optional] [default to undefined]
**cid** | **string** |  | [optional] [default to undefined]
**grantTypes** | **Set&lt;string&gt;** |  | [optional] [default to undefined]
**refreshTokenValidity** | **number** |  | [optional] [default to undefined]
**accessTokenValidity** | **number** |  | [optional] [default to undefined]
**redirectUri** | **Set&lt;string&gt;** |  | [optional] [default to undefined]

## Example

```typescript
import { UserRoleClient } from './api';

const instance: UserRoleClient = {
    name,
    description,
    logo,
    createdAt,
    updatedAt,
    status,
    uid,
    resources,
    members,
    scopes,
    types,
    authorities,
    count,
    clientSecret,
    extra,
    cid,
    grantTypes,
    refreshTokenValidity,
    accessTokenValidity,
    redirectUri,
};
```

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)
