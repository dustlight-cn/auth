# Kubernetes 部署
## 一键部署
使用 ```kubectl``` 一键部署服务，集群中需要至少三个持久卷（```PersistentVolume```）用于 MySQL 数据库 与 Redis 以及上传文件的持久化。

执行：
```bash
kubectl apply -f https://raw.githubusercontent.com/dustlight-cn/auth/master/docs/deploy/k8s/deploy.yaml
```

## HELM 部署

[HELM 部署文档](https://dustlight-cn.github.io/auth-helm)

[HELM Chart 仓库](https://github.com/dustlight-cn/auth-helm)
