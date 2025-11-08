# AuthorizationResponse


## Properties

Name | Type | Description | Notes
------------ | ------------- | ------------- | -------------
**client** | [**AuthorizationClient**](AuthorizationClient.md) |  | [optional] [default to undefined]
**owner** | [**PublicUser**](PublicUser.md) |  | [optional] [default to undefined]
**redirect** | **string** |  | [optional] [default to undefined]
**count** | **number** |  | [optional] [default to undefined]
**approved** | **boolean** |  | [optional] [default to undefined]

## Example

```typescript
import { AuthorizationResponse } from './api';

const instance: AuthorizationResponse = {
    client,
    owner,
    redirect,
    count,
    approved,
};
```

[[Back to Model list]](../README.md#documentation-for-models) [[Back to API list]](../README.md#documentation-for-api-endpoints) [[Back to README]](../README.md)
